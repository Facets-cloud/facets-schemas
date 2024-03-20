#!/bin/bash

# Check if CP_URL, PRINCIPAL_NAME, and WEBHOOK_ID were provided as arguments
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <CP_URL> <PRINCIPAL_NAME> <WEBHOOK_ID>"
    exit 1
fi

CP_URL=$1
ACCOUNT_NAME="$2"
# Concatenate "facets-" with the provided principal name
PRINCIPAL_NAME="facets-$2",

WEBHOOK_ID=$3

# Fetch all projects in JSON format
echo "Fetching available projects..."
PROJECTS_JSON=$(gcloud projects list --format=json)

if [ $? -ne 0 ]; then
    echo "Failed to fetch projects. Ensure you're logged in and try again."
    exit 1
fi

# Convert JSON to "CSV"
PROJECTS=$(echo "$PROJECTS_JSON" | jq -r '.[] | "\(.projectId):\(.projectId)"')

if [ -z "$PROJECTS" ]; then
    echo "No projects are available."
    exit 1
fi

# Display projects and prompt for choice
echo "Please select a project by number:"
IFS=$'\n' # Ensure we split lines on newlines
select PROJECT_LINE in $PROJECTS; do
    PROJECT_ID=$(echo $PROJECT_LINE | cut -d":" -f2)
    if [ -n "$PROJECT_ID" ]; then
        echo "You have selected project '$PROJECT_ID'"
        break
    else
        echo "Invalid selection. Please select a number from the list."
    fi
done

# Set the project context
gcloud config set project "$PROJECT_ID" &>/dev/null

if [ $? -ne 0 ]; then
    echo "Failed to set the project context."
    exit 1
fi

# Create the Service Account with Owner role
gcloud iam service-accounts create "$PRINCIPAL_NAME" --display-name="$PRINCIPAL_NAME"

if [ $? -ne 0 ]; then
    echo "Failed to create Service Account."
    exit 1
fi

# Generate key for the Service Account
gcloud iam service-accounts keys create "$PRINCIPAL_NAME-key.json" --iam-account="$PRINCIPAL_NAME@$PROJECT_ID.iam.gserviceaccount.com"

if [ $? -ne 0 ]; then
    echo "Failed to generate key for Service Account."
    exit 1
fi

echo "Service Account created successfully."
echo "Key file saved as $PRINCIPAL_NAME-key.json"

# Base64 encode the service account key JSON
SERVICE_ACCOUNT_KEY_BASE64=$(base64 -w 0 "$PRINCIPAL_NAME-key.json")

# Prepare the curl request with base64 encoded key
CURL_DATA="{ \"payload\": { \"name\": \"$ACCOUNT_NAME\", \"serviceAccountKey\": \"$SERVICE_ACCOUNT_KEY_BASE64\", \"project\": \"$PROJECT_ID\" }, \"webhookId\": \"$WEBHOOK_ID\"}"
CURL_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "https://$CP_URL/public/v1/link-gcp" -H "accept: */*" -H "Content-Type: application/json; ; charset=utf-8" -d "$CURL_DATA")

if [ "$CURL_RESPONSE" -ne 200 ]; then
    echo "Failed to send data to the specified URL. HTTP response code: $CURL_RESPONSE"
    exit 1
fi

echo "Data successfully sent to the specified URL."
