#!/bin/bash

# Check if CP_URL, PRINCIPAL_NAME, and WEBHOOK_ID were provided as arguments
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <CP_URL> <PRINCIPAL_NAME> <WEBHOOK_ID>"
    exit 1
fi

CP_URL=$1
PRINCIPAL_NAME="$2"
WEBHOOK_ID=$3

# Track if aks-preview extension was removed
EXTENSION_REMOVED=false

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
    AKS_CLUSTERS_JSON=$(az aks list --subscription "$SUBSCRIPTION_ID" --query "[].{name:name, resourceGroup:resourceGroup, id:id, enableAzureRbac:aadProfile.enableAzureRbac}" --output json 2>&1)
    AKS_LIST_EXIT_CODE=$?

    # Check if it's the msrestazure error and offer to fix it
    if [ $AKS_LIST_EXIT_CODE -ne 0 ] && echo "$AKS_CLUSTERS_JSON" | grep -q "No module named 'msrestazure'"; then
        echo ""
        echo "Detected an Azure CLI extension issue with 'aks-preview'."
        echo "The extension has a broken dependency that prevents AKS commands from working."
        echo ""
        read -p "Temporarily remove 'aks-preview' extension to continue? (It will be reinstalled at the end) [y/N]: " REMOVE_EXTENSION

        if [[ "$REMOVE_EXTENSION" =~ ^[Yy]$ ]]; then
            echo "Removing aks-preview extension..."
            az extension remove --name aks-preview &>/dev/null

            if [ $? -eq 0 ]; then
                EXTENSION_REMOVED=true
                echo "Extension removed. Retrying..."
                echo ""

                # Retry fetching clusters
                echo "Fetching AKS clusters in subscription..."
                AKS_CLUSTERS_JSON=$(az aks list --subscription "$SUBSCRIPTION_ID" --query "[].{name:name, resourceGroup:resourceGroup, id:id, enableAzureRbac:aadProfile.enableAzureRbac}" --output json 2>&1)
                AKS_LIST_EXIT_CODE=$?
            else
                echo "Failed to remove extension. Skipping AKS configuration..."
            fi
        else
            echo "Skipping AKS configuration..."
        fi
    fi

    if [ $AKS_LIST_EXIT_CODE -ne 0 ]; then
        echo "Failed to fetch AKS clusters. Skipping AKS configuration..."
    elif [ "$(echo "$AKS_CLUSTERS_JSON" | jq 'length')" -eq 0 ]; then
        echo "No AKS clusters found in this subscription."
    else
        # Filter clusters with Azure RBAC enabled
        RBAC_ENABLED_CLUSTERS=$(echo "$AKS_CLUSTERS_JSON" | jq '[.[] | select(.enableAzureRbac == true)]')
        RBAC_CLUSTER_COUNT=$(echo "$RBAC_ENABLED_CLUSTERS" | jq 'length')

        if [ "$RBAC_CLUSTER_COUNT" -eq 0 ]; then
            echo ""
            echo "No AKS clusters with Azure RBAC enabled found in this subscription."
            echo "To enable Azure RBAC on a cluster, run:"
            echo "  az aks update --resource-group <RG> --name <CLUSTER> --enable-azure-rbac"
            echo ""
            echo "Skipping AKS configuration..."
        else
            # Display clusters with Azure RBAC enabled
            echo ""
            echo "Available AKS clusters (with Azure RBAC enabled):"
            echo "$RBAC_ENABLED_CLUSTERS" | jq -r '.[] | "  - \(.name) (RG: \(.resourceGroup))"'
            echo ""
            echo "Enter cluster names separated by spaces (or 'all' for all clusters):"
            read -p "Clusters: " CLUSTER_INPUT

            SELECTED_CLUSTERS=()
            if [[ "$CLUSTER_INPUT" == "all" ]]; then
                SELECTED_CLUSTERS=($(echo "$RBAC_ENABLED_CLUSTERS" | jq -r '.[].name'))
            else
                read -ra SELECTED_CLUSTERS <<< "$CLUSTER_INPUT"
            fi

            # Verify all selected clusters exist in the RBAC-enabled list
            CLUSTERS_TO_GRANT=()

            for CLUSTER_NAME in "${SELECTED_CLUSTERS[@]}"; do
                CLUSTER_INFO=$(echo "$RBAC_ENABLED_CLUSTERS" | jq -r --arg name "$CLUSTER_NAME" '.[] | select(.name == $name)')

                if [ -z "$CLUSTER_INFO" ]; then
                    echo "Warning: Cluster '$CLUSTER_NAME' not found or does not have Azure RBAC enabled. Skipping..."
                    continue
                fi

                CLUSTER_ID=$(echo "$CLUSTER_INFO" | jq -r '.id')
                CLUSTERS_TO_GRANT+=("$CLUSTER_ID:$CLUSTER_NAME")
            done

            if [ ${#CLUSTERS_TO_GRANT[@]} -eq 0 ]; then
                echo "No valid clusters selected. Skipping AKS role assignment."
            else
            echo ""
            echo "Granting AKS read access to ${#CLUSTERS_TO_GRANT[@]} cluster(s)..."

            for CLUSTER_DATA in "${CLUSTERS_TO_GRANT[@]}"; do
                IFS=':' read -r CLUSTER_ID CLUSTER_NAME <<< "$CLUSTER_DATA"

                echo "  - Granting access to '$CLUSTER_NAME'..."

                # Assign Azure Kubernetes Service RBAC Cluster Reader role
                RBAC_READER_ERROR=$(az role assignment create \
                    --assignee "$CLIENT_ID" \
                    --role "Azure Kubernetes Service RBAC Cluster Reader" \
                    --scope "$CLUSTER_ID" 2>&1)

                if [ $? -ne 0 ]; then
                    echo "    Warning: Failed to assign RBAC Cluster Reader role to '$CLUSTER_NAME'"
                    if echo "$RBAC_READER_ERROR" | grep -q "already exists"; then
                        echo "    (Role assignment already exists)"
                    else
                        echo "    Error: $RBAC_READER_ERROR"
                    fi
                fi

                # Assign Cluster User role for credential access
                CLUSTER_USER_ERROR=$(az role assignment create \
                    --assignee "$CLIENT_ID" \
                    --role "Azure Kubernetes Service Cluster User Role" \
                    --scope "$CLUSTER_ID" 2>&1)

                if [ $? -ne 0 ]; then
                    echo "    Warning: Failed to assign Cluster User role to '$CLUSTER_NAME'"
                    if echo "$CLUSTER_USER_ERROR" | grep -q "already exists"; then
                        echo "    (Role assignment already exists)"
                    else
                        echo "    Error: $CLUSTER_USER_ERROR"
                    fi
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
                CUSTOM_ROLE_ERROR=$(az role assignment create \
                    --assignee "$CLIENT_ID" \
                    --role "$CUSTOM_ROLE_NAME" \
                    --scope "$CLUSTER_ID" 2>&1)

                if [ $? -eq 0 ]; then
                    echo "    ✓ Successfully granted read access to '$CLUSTER_NAME'"
                else
                    echo "    Warning: Failed to assign remote access role to '$CLUSTER_NAME'"
                    if echo "$CUSTOM_ROLE_ERROR" | grep -q "already exists"; then
                        echo "    (Role assignment already exists)"
                        echo "    ✓ Read access already configured for '$CLUSTER_NAME'"
                    else
                        echo "    Error: $CUSTOM_ROLE_ERROR"
                    fi
                fi
            done

                echo ""
                echo "AKS cluster access configuration complete."
            fi
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

# Reinstall aks-preview extension if it was removed
if [ "$EXTENSION_REMOVED" = true ]; then
    echo ""
    echo "Reinstalling aks-preview extension..."
    az extension add --name aks-preview &>/dev/null

    if [ $? -eq 0 ]; then
        echo "Extension reinstalled successfully."
    else
        echo "Warning: Failed to reinstall aks-preview extension."
        echo "You can manually reinstall it with: az extension add --name aks-preview"
    fi
fi
