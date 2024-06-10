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

# Ask the user if they want to create a new ACR or use an existing one
read -p "Do you want to create a new Azure Container Registry (N/y)? " create_new_acr

if [[ $create_new_acr =~ ^[Yy]$ ]]; then
	# User wants to create a new ACR
	read -p "Enter a name for the new Azure Container Registry: " new_acr_name
	read -p "Enter the resource group for the new ACR: " resource_group
	read -p "Enter the SKU for the new ACR (Basic/Standard/Premium): " acr_sku

	# Create the new ACR and enable admin user
	az acr create --name "$new_acr_name" --resource-group "$resource_group" --sku "$acr_sku" --admin-enabled true
	acr_name=$new_acr_name
else
	# User wants to use an existing ACR, list the existing ones
	acr_list=$(az acr list --query "[].{name:name}" --output tsv)
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
