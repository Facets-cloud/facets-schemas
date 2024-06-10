#!/bin/bash

# Exit if any command fails
set -e

# Check for required parameters
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <account_name> <callback_uri> <webhookId>"
    exit 1
fi

# Assign parameters to variables
account_name="$1"
callback_uri="$2"
webhookId="$3"

# Function to validate Azure location
validate_location() {
    local location=$1
    local valid_locations=$(az account list-locations --query "[].name" --output tsv)
    if echo "$valid_locations" | grep -w -q "$location"; then
        return 0
    else
        return 1
    fi
}

# Function to validate ACR name availability
validate_acr_name() {
    local acr_name=$1
    az acr check-name --name "$acr_name" --query "nameAvailable" --output tsv
}

# Ask the user which subscription they would like to use
subscriptions_list=$(az account list --query "[].{name:name, id:id}" --output json)
subscriptions_count=$(echo "$subscriptions_list" | jq length)

echo "Available Azure Subscriptions:"
for i in $(seq 0 $(($subscriptions_count - 1))); do
    subscription_name=$(echo "$subscriptions_list" | jq -r ".[$i].name")
    subscription_id=$(echo "$subscriptions_list" | jq -r ".[$i].id")
    echo "$(($i + 1))) $subscription_name ($subscription_id)"
done

read -p "Select the number of the Azure Subscription you want to use: " selected_subscription_number
selected_subscription_index=$(($selected_subscription_number - 1))

# Validate selection
if [ $selected_subscription_index -lt 0 ] || [ $selected_subscription_index -ge $subscriptions_count ]; then
    echo "Invalid selection."
    exit 1
fi

selected_subscription_id=$(echo "$subscriptions_list" | jq -r ".[$selected_subscription_index].id")

# Set the selected subscription
az account set --subscription "$selected_subscription_id"

# Ask the user if they want to create a new ACR or use an existing one
read -p "Do you want to create a new Azure Container Registry (N/y)? " create_new_acr

if [[ $create_new_acr =~ ^[Yy]$ ]]; then
    # User wants to create a new ACR

    # List available resource groups
    resource_groups_list=$(az group list --query "[].{name:name}" --output tsv)
    resource_groups=($resource_groups_list)

    echo "Available Azure Resource Groups:"
    for i in "${!resource_groups[@]}"; do
        echo "$((i + 1))) ${resource_groups[$i]}"
    done

    read -p "Select the number of the Azure Resource Group you want to use: " selected_resource_group_number
    selected_resource_group_index=$((selected_resource_group_number - 1))

    # Validate selection
    if [ $selected_resource_group_index -lt 0 ] || [ $selected_resource_group_index -ge ${#resource_groups[@]} ]; then
        echo "Invalid selection."
        exit 1
    fi

    selected_resource_group=${resource_groups[$selected_resource_group_index]}

    # Ask the user for location and validate it
    while true; do
        read -p "Enter the location for the new ACR: " location
        if validate_location "$location"; then
            break
        else
            echo "Invalid location. Valid locations are:"
            az account list-locations --query "[].name" --output tsv | column
        fi
    done

    # Ask the user for ACR name and validate availability
    while true; do
        read -p "Enter a name for the new Azure Container Registry: " new_acr_name
        if [ "$(validate_acr_name $new_acr_name)" = "true" ]; then
            break
        else
            echo "ACR name '$new_acr_name' is not available. Please try another name."
        fi
    done

    # Provide a numbered list for SKU selection
    skus=("Basic" "Standard" "Premium")

    echo "Available SKUs:"
    for i in "${!skus[@]}"; do
        echo "$((i + 1))) ${skus[$i]}"
    done

    read -p "Select the number of the SKU you want to use: " selected_sku_number
    selected_sku_index=$((selected_sku_number - 1))

    # Validate selection
    if [ $selected_sku_index -lt 0 ] || [ $selected_sku_index -ge ${#skus[@]} ]; then
        echo "Invalid selection."
        exit 1
    fi

    acr_sku=${skus[$selected_sku_index]}

    # Create the new ACR and enable admin user
    az acr create --name "$new_acr_name" --resource-group "$selected_resource_group" --sku "$acr_sku" --location "$location" --admin-enabled true
    acr_name=$new_acr_name
else
    # User wants to use an existing ACR in the selected subscription
    acr_list=$(az acr list --subscription "$selected_subscription_id" --query "[].{name:name}" --output tsv)
    acr_names=($acr_list) # Convert to an array

    echo "Available Azure Container Registries:"
    for i in "${!acr_names[@]}"; do
        echo "$((i + 1))) ${acr_names[$i]}"
    done

    # Ask the user to select an ACR by number
    read -p "Select the number of the Azure Container Registry you want to use: " selected_number
    selected_index=$((selected_number - 1))

    # Validate selection
    if [ $selected_index -lt 0 ] || [ $selected_index -ge ${#acr_names[@]} ]; then
        echo "Invalid selection."
        exit 1
    fi

    # Get the chosen ACR name
    acr_name=${acr_names[$selected_index]}
fi

# Get the login server for the ACR
acr_login_server=$(az acr show --name "$acr_name" --query loginServer --output tsv)

# Get the credentials for the ACR
acr_username=$(az acr credential show --name "$acr_name" --query "username" --output tsv)
acr_password=$(az acr credential show --name "$acr_name" --query "passwords[0].value" --output tsv)

# Make a CURL request to the callback URI with the new JSON payload
# Please ensure that the callback URI uses HTTPS and is secure
curl -X POST "https://$callback_uri/public/v1/link-docker-registries" \
    -H "Content-Type: application/json" \
    -d '{
        "payload": {
            "artifactoryName": "'"$account_name"'",
            "artifactoryType": "AZURE_CONTAINER_REGISTRY",
            "password": "'"$acr_password"'",
            "uri": "'"$acr_login_server"'",
            "username": "'"$acr_username"'"
        },
        "webhookId": "'"$webhookId"'"
    }'

# Inform the user that the script is finished
echo "CURL request to the callback URI has been made with the ACR credentials."
