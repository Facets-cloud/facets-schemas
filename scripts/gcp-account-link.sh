#!/bin/bash

# Check if CP_URL, PRINCIPAL_NAME, and WEBHOOK_ID were provided as arguments
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <CP_URL> <PRINCIPAL_NAME> <WEBHOOK_ID>"
    exit 1
fi

CP_URL=$1
ACCOUNT_NAME="$2"
# Concatenate "facets-" with the provided principal name
PRINCIPAL_NAME="facets-$2"
ROLE_NAME=$(echo $PRINCIPAL_NAME | tr '-' '_')

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

##### Enabling APIs before creating service account #######

# List of APIs to enable
apis=(
  "alloydb.googleapis.com" "analyticshub.googleapis.com" "artifactregistry.googleapis.com" "autoscaling.googleapis.com" 
  "bigquery.googleapis.com" "bigqueryconnection.googleapis.com" "bigquerydatapolicy.googleapis.com" "bigquerymigration.googleapis.com" 
  "bigqueryreservation.googleapis.com" "bigquerystorage.googleapis.com" "certificatemanager.googleapis.com" "cloudapis.googleapis.com" 
  "cloudkms.googleapis.com" "cloudresourcemanager.googleapis.com" "cloudtrace.googleapis.com" "compute.googleapis.com" "container.googleapis.com" 
  "containerfilesystem.googleapis.com" "containerregistry.googleapis.com" "dataform.googleapis.com" "dataplex.googleapis.com" 
  "datastore.googleapis.com" "deploymentmanager.googleapis.com" "dns.googleapis.com" "gkebackup.googleapis.com" "iam.googleapis.com" 
  "iamcredentials.googleapis.com" "logging.googleapis.com" "monitoring.googleapis.com" "networkconnectivity.googleapis.com" "oslogin.googleapis.com" 
  "pubsub.googleapis.com" "redis.googleapis.com" "servicemanagement.googleapis.com" "servicenetworking.googleapis.com" "serviceusage.googleapis.com" 
  "sql-component.googleapis.com" "sqladmin.googleapis.com" "storage-api.googleapis.com" "storage-component.googleapis.com" "storage.googleapis.com"
)

# Get the list of enabled APIs
echo "Fetching the list of enabled APIs..."
enabled_apis=$(gcloud services list --enabled --format="value(config.name)")

# Function to enable an API
enable_api() {
  local api_name=$1
  echo "Enabling API: ${api_name}"
  gcloud services enable "${api_name}"
}

# Iterate over the APIs and enable them if not already enabled
for api in "${apis[@]}"; do
  if echo "${enabled_apis}" | grep -q "${api}"; then
    echo "API : ${api} is already enabled"
  else
    enable_api "${api}" &
  fi
done

# Wait for all background jobs to complete
wait

echo "All specified APIs have been enabled."

# Check if the service account already exists
SA_EMAIL="$PRINCIPAL_NAME@$PROJECT_ID.iam.gserviceaccount.com"
EXISTING_SA=$(gcloud iam service-accounts list --filter="email:$SA_EMAIL" --format="value(email)")

if [ -z "$EXISTING_SA" ]; then
    # Create the Service Account with Owner role
    gcloud iam service-accounts create "$PRINCIPAL_NAME" --display-name="$PRINCIPAL_NAME"

    if [ $? -ne 0 ]; then
        echo "Failed to create Service Account."
        exit 1
    fi

    # Wait for a short period to ensure the service account is fully created
    sleep 10
else
    echo "Service Account $SA_EMAIL already exists."
fi

# Generate key for the Service Account
gcloud iam service-accounts keys create "$PRINCIPAL_NAME-key.json" --iam-account="$SA_EMAIL"

if [ $? -ne 0 ]; then
    echo "Failed to generate key for Service Account."
    exit 1
fi

echo "Service Account key generated successfully."
echo "Key file saved as $PRINCIPAL_NAME-key.json"

gcloud projects add-iam-policy-binding "$PROJECT_ID" --member="serviceAccount:$SA_EMAIL" --role="roles/owner"

# Base64 encode the service account key JSON
SERVICE_ACCOUNT_KEY_BASE64=$(base64 -w 0 "$PRINCIPAL_NAME-key.json")

# Prepare the curl request with base64 encoded key
CURL_DATA="{ \"payload\": { \"name\": \"$ACCOUNT_NAME\", \"serviceAccountKey\": \"$SERVICE_ACCOUNT_KEY_BASE64\", \"project\": \"$PROJECT_ID\" }, \"webhookId\": \"$WEBHOOK_ID\"}"
CURL_RESPONSE=$(curl -k -s -o /dev/null -w "%{http_code}" -X POST "https://$CP_URL/public/v1/link-gcp" -H "accept: */*" -H "Content-Type: application/json; charset=utf-8" -d "$CURL_DATA")

if [ "$CURL_RESPONSE" -ne 200 ]; then
    echo "Failed to send data to the specified URL. HTTP response code: $CURL_RESPONSE"
    exit 1
fi

echo "Data successfully sent to the specified URL."
