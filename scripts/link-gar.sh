#!/bin/bash

set -e

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <artifactory_name> <callback_uri> <webhookId>"
    exit 1
fi

artifactory_name="$1"
callback_uri="$2"
webhookId="$3"

# Prompt user to select a Google Cloud project
echo "Fetching list of Google Cloud projects..."
project_list=$(gcloud projects list --format="value(projectId)")

echo "Available Google Cloud Projects:"
PS3="Please select the project number: "
select project_id in $project_list; do
    if [ -n "$project_id" ]; then
        echo "You have selected project: $project_id"
        gcloud config set project "$project_id"
        break
    else
        echo "Invalid selection. Please try again."
    fi
done

valid_locations=$(gcloud artifacts locations list --format="value(name)")
available_formats=("DOCKER" "MAVEN" "NPM" "APT" "YUM")

read -p "Do you want to create a new Google Artifact Registry (N/y)? " create_new_gar

repository_name="$artifactory_name" # Default to the name provided by the user

if [[ $create_new_gar =~ ^[Yy]$ ]]; then
    read -p "Enter a name for the new Google Artifact Registry: " new_gar_name
    repository_name="$new_gar_name" # Use the new name

    while true; do
        read -p "Enter the location for the new Artifact Registry (e.g., us-central1): " location
        
        if echo "$valid_locations" | grep -qE "^$location$"; then
            break
        else
            echo "Invalid location. Valid locations are:"
            echo "$valid_locations"
            echo "Please try again."
        fi
    done

    read -p "Enter the description for the new Artifact Registry: " description

    echo "Available repository formats:"
    for i in "${!available_formats[@]}"; do
        echo "$((i + 1))) ${available_formats[$i]}"
    done

    read -p "Select the number for the repository format: " format_selection
    format_index=$((format_selection - 1))

    if [ $format_index -lt 0 ] || [ $format_index -ge ${#available_formats[@]} ]; then
        echo "Invalid selection."
        exit 1
    fi

    format=${available_formats[$format_index]}

    # Create the new Artifact Registry
    gcloud artifacts repositories create "$repository_name" \
        --repository-format="$format" \
        --location="$location" \
        --description="$description"
else
    # Assume that the location is provided for existing repositories
    gar_list_json=$(gcloud artifacts repositories list --format="json")
    
    # Parse the JSON and get an array of registry names
    IFS=$'\n' read -r -d '' -a gar_names < <(echo "$gar_list_json" | jq -r '.[].name' && printf '\0')
    
    echo "Available Google Artifact Registries:"
    for i in "${!gar_names[@]}"; do
        echo "$((i + 1))) ${gar_names[$i]}"
    done
    
    read -p "Select the number of the Google Artifact Registry you want to use: " selected_number
    selected_index=$((selected_number - 1))

    if [[ $selected_index -lt 0 ]] || [[ $selected_index -ge ${#gar_names[@]} ]]; then
        echo "Invalid selection."
        exit 1
    fi
    
    selected_registry_name="${gar_names[$selected_index]}"
    # Parse the location from the selected registry name
    location=$(echo "$selected_registry_name" | cut -d'/' -f4)
    repository_name=$(echo "$selected_registry_name" | cut -d'/' -f6)
fi

# Construct the URI for the Artifact Registry
project_id=$(gcloud config get-value core/project)
uri="${location}-docker.pkg.dev"

echo "The Google Artifact Registry URI is: $uri"

# Create a service account and fetch its credentials
service_account_name="$artifactory_name-sa"
gcloud iam service-accounts create "$service_account_name" --display-name "Artifact Registry Service Account"
gcloud projects add-iam-policy-binding "$project_id" \
    --member="serviceAccount:$service_account_name@$project_id.iam.gserviceaccount.com" \
    --role="roles/artifactregistry.writer"
gcloud projects add-iam-policy-binding "$project_id" \
    --member="serviceAccount:$service_account_name@$project_id.iam.gserviceaccount.com" \
    --role="roles/viewer"
key_path="$(mktemp).json"
gcloud iam service-accounts keys create "$key_path" --iam-account "$service_account_name@$project_id.iam.gserviceaccount.com"
encoded_key=$(base64 < "$key_path" | tr -d '\n')

# Send the encoded key with the curl command
curl -X POST "https://$callback_uri/public/v1/link-docker-registries" \
    -H "Content-Type: application/json" \
    -d '{
        "payload": {
            "artifactoryName": "'"$artifactory_name"'",
            "artifactoryType": "GOOGLE_ARTIFACT_REGISTRY",
            "password": "'"$encoded_key"'",
            "uri": "'"$uri"'",
            "username": "_json_key_base64"
        },
        "webhookId": "'"$webhookId"'"
    }'

echo "CURL request to the callback URI has been made with the Google Artifact Registry details."

# Clean up the service account key file
rm -f "$key_path"
