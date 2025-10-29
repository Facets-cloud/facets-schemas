#!/bin/bash

# Check if CP_URL, PRINCIPAL_NAME, and WEBHOOK_ID were provided as arguments
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <CP_URL> <PRINCIPAL_NAME> <WEBHOOK_ID>"
    exit 1
fi

CP_URL=$1
PRINCIPAL_NAME="$2"
WEBHOOK_ID=$3

# Fetch all subscriptions in JSON format
echo "Fetching available subscriptions..."
SUBSCRIPTIONS_JSON=$(az account list --query "[].{name:name, id:id}" --output json)

if [ $? -ne 0 ]; then
    echo "Failed to fetch subscriptions. Ensure you're logged in and try again."
    exit 1
fi

# Convert JSON to "CSV"
SUBSCRIPTIONS=$(echo "$SUBSCRIPTIONS_JSON" | jq -r '.[] | "\(.name):\(.id)"')

if [ -z "$SUBSCRIPTIONS" ]; then
    echo "No subscriptions are available."
    exit 1
fi

# Display subscriptions and prompt for choice
echo "Please select a subscription by number:"
IFS=$'\n' # Ensure we split lines on newlines
select SUBSCRIPTION_LINE in $SUBSCRIPTIONS; do
    SUBSCRIPTION_NAME=$(echo "$SUBSCRIPTION_LINE" | cut -d":" -f1)
    SUBSCRIPTION_ID=$(echo "$SUBSCRIPTION_LINE" | cut -d":" -f2)
    if [ -n "$SUBSCRIPTION_ID" ]; then
        echo "You have selected subscription '$SUBSCRIPTION_NAME' with ID '$SUBSCRIPTION_ID'"
        break
    else
        echo "Invalid selection. Please select a number from the list."
    fi
done

# Set the subscription context
az account set --subscription "$SUBSCRIPTION_ID" &>/dev/null

if [ $? -ne 0 ]; then
    echo "Failed to set the subscription context."
    exit 1
fi

# Create the Service Principal with Reader role
SP_JSON=$(az ad sp create-for-rbac --name "facets-$PRINCIPAL_NAME" --role "Reader" --scopes /subscriptions/"$SUBSCRIPTION_ID")

if [ $? -ne 0 ]; then
    echo "Failed to create Service Principal."
    exit 1
fi

# Extract necessary data from SP_JSON
CLIENT_ID=$(echo "$SP_JSON" | jq -r .appId)
CLIENT_SECRET=$(echo "$SP_JSON" | jq -r .password)
TENANT_ID=$(echo "$SP_JSON" | jq -r .tenant)

echo "Service Principal created successfully."
echo "$SP_JSON"

# Ask if user wants to add AKS cluster read access
echo ""
read -p "Do you want to add read access to AKS clusters? (y/n): " ADD_AKS_ACCESS

if [[ "$ADD_AKS_ACCESS" =~ ^[Yy]$ ]]; then
    echo "Fetching AKS clusters in subscription..."
    AKS_CLUSTERS_JSON=$(az aks list --subscription "$SUBSCRIPTION_ID" --query "[].{name:name, resourceGroup:resourceGroup, id:id, enableAzureRbac:aadProfile.enableAzureRbac}" --output json)

    if [ $? -ne 0 ]; then
        echo "Failed to fetch AKS clusters."
    elif [ "$(echo "$AKS_CLUSTERS_JSON" | jq 'length')" -eq 0 ]; then
        echo "No AKS clusters found in this subscription."
    else
        # Display clusters
        echo ""
        echo "Available AKS clusters:"
        echo "$AKS_CLUSTERS_JSON" | jq -r '.[] | "  - \(.name) (RG: \(.resourceGroup), Azure RBAC: \(.enableAzureRbac // false))"'
        echo ""
        echo "Enter cluster names separated by spaces (or 'all' for all clusters):"
        read -p "Clusters: " CLUSTER_INPUT

        SELECTED_CLUSTERS=()
        if [[ "$CLUSTER_INPUT" == "all" ]]; then
            SELECTED_CLUSTERS=($(echo "$AKS_CLUSTERS_JSON" | jq -r '.[].name'))
        else
            read -ra SELECTED_CLUSTERS <<< "$CLUSTER_INPUT"
        fi

        # Verify all selected clusters have Azure RBAC enabled
        ALL_RBAC_ENABLED=true
        CLUSTERS_TO_GRANT=()

        for CLUSTER_NAME in "${SELECTED_CLUSTERS[@]}"; do
            CLUSTER_INFO=$(echo "$AKS_CLUSTERS_JSON" | jq -r --arg name "$CLUSTER_NAME" '.[] | select(.name == $name)')

            if [ -z "$CLUSTER_INFO" ]; then
                echo "Warning: Cluster '$CLUSTER_NAME' not found. Skipping..."
                continue
            fi

            RBAC_ENABLED=$(echo "$CLUSTER_INFO" | jq -r '.enableAzureRbac // false')
            CLUSTER_ID=$(echo "$CLUSTER_INFO" | jq -r '.id')

            if [ "$RBAC_ENABLED" != "true" ]; then
                echo "Error: Cluster '$CLUSTER_NAME' does not have Azure RBAC enabled."
                ALL_RBAC_ENABLED=false
            else
                CLUSTERS_TO_GRANT+=("$CLUSTER_ID:$CLUSTER_NAME")
            fi
        done

        if [ "$ALL_RBAC_ENABLED" = false ]; then
            echo ""
            echo "Some clusters do not have Azure RBAC enabled. Skipping AKS role assignment."
            echo "To enable Azure RBAC on a cluster, run:"
            echo "  az aks update --resource-group <RG> --name <CLUSTER> --enable-azure-rbac"
        elif [ ${#CLUSTERS_TO_GRANT[@]} -eq 0 ]; then
            echo "No valid clusters selected. Skipping AKS role assignment."
        else
            echo ""
            echo "Granting AKS read access to ${#CLUSTERS_TO_GRANT[@]} cluster(s)..."

            for CLUSTER_DATA in "${CLUSTERS_TO_GRANT[@]}"; do
                IFS=':' read -r CLUSTER_ID CLUSTER_NAME <<< "$CLUSTER_DATA"

                echo "  - Granting access to '$CLUSTER_NAME'..."

                # Assign Azure Kubernetes Service RBAC Cluster Reader role
                az role assignment create \
                    --assignee "$CLIENT_ID" \
                    --role "Azure Kubernetes Service RBAC Cluster Reader" \
                    --scope "$CLUSTER_ID" &>/dev/null

                if [ $? -ne 0 ]; then
                    echo "    Warning: Failed to assign RBAC Cluster Reader role to '$CLUSTER_NAME'"
                fi

                # Assign runcommand action for remote access
                az role assignment create \
                    --assignee "$CLIENT_ID" \
                    --role "Azure Kubernetes Service Cluster User Role" \
                    --scope "$CLUSTER_ID" &>/dev/null

                if [ $? -ne 0 ]; then
                    echo "    Warning: Failed to assign Cluster User role to '$CLUSTER_NAME'"
                fi

                # Create custom role definition for remote access if it doesn't exist
                CUSTOM_ROLE_NAME="AKS Remote Access Reader"
                ROLE_EXISTS=$(az role definition list --name "$CUSTOM_ROLE_NAME" --subscription "$SUBSCRIPTION_ID" --query "[].name" -o tsv)

                if [ -z "$ROLE_EXISTS" ]; then
                    az role definition create --role-definition "{
                        \"Name\": \"$CUSTOM_ROLE_NAME\",
                        \"Description\": \"Enables remote viewing of cluster resources without network access\",
                        \"Actions\": [
                            \"Microsoft.ContainerService/managedClusters/runcommand/action\",
                            \"Microsoft.ContainerService/managedClusters/commandResults/read\"
                        ],
                        \"AssignableScopes\": [\"/subscriptions/$SUBSCRIPTION_ID\"]
                    }" &>/dev/null
                fi

                # Assign the custom remote access role
                az role assignment create \
                    --assignee "$CLIENT_ID" \
                    --role "$CUSTOM_ROLE_NAME" \
                    --scope "$CLUSTER_ID" &>/dev/null

                if [ $? -eq 0 ]; then
                    echo "    ✓ Successfully granted read access to '$CLUSTER_NAME'"
                else
                    echo "    Warning: Failed to assign runcommand role to '$CLUSTER_NAME'"
                fi
            done

            echo ""
            echo "AKS cluster access configuration complete."
        fi
    fi
fi

# Sleep 10 seconds
echo ""
echo "Sleeping 10 seconds..."
sleep 10

# Prepare the curl request
CURL_DATA=$(jq -n \
  --arg name "$PRINCIPAL_NAME" \
  --arg clientId "$CLIENT_ID" \
  --arg clientSecret "$CLIENT_SECRET" \
  --arg subscriptionId "$SUBSCRIPTION_ID" \
  --arg tenantId "$TENANT_ID" \
  --arg webhookId "$WEBHOOK_ID" \
  '{payload: {name: $name, clientId: $clientId, clientSecret: $clientSecret, subscriptionId: $subscriptionId, tenantId: $tenantId}, webhookId: $webhookId}')

CURL_RESPONSE=$(curl -k -s -w "\n%{http_code}" -X POST "https://$CP_URL/public/v1/link-azure" -H "accept: */*" -H "Content-Type: application/json; charset=utf-8" -d "$CURL_DATA")

HTTP_CODE=$(echo "$CURL_RESPONSE" | tail -n1)
RESPONSE_BODY=$(echo "$CURL_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -ne 200 ]; then
    echo "Failed to send data to the specified URL. HTTP response code: $HTTP_CODE"
    echo "Response body: $RESPONSE_BODY"
    exit 1
fi

echo "Data successfully sent to the specified URL."
