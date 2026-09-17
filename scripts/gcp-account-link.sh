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

# Prompt for access mode
echo "Select access mode:"
echo "  1) Write access (default) - Use this to provision environments in GCP using Facets"
echo "  2) Read-only access - Use this to discover Facets blueprint from existing setup"
read -p "Enter choice [1]: " ACCESS_MODE

# Default to write mode if empty
ACCESS_MODE=${ACCESS_MODE:-1}

# If read-only mode is selected, download and execute the reader script
if [ "$ACCESS_MODE" = "2" ]; then
    echo ""
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" 2>/dev/null && pwd)"
    if [ -n "$SCRIPT_DIR" ] && [ -f "$SCRIPT_DIR/gcp-account-link-reader.sh" ]; then
        echo "Executing local reader script..."
        echo ""
        "$SCRIPT_DIR/gcp-account-link-reader.sh" "$CP_URL" "$ACCOUNT_NAME" "$WEBHOOK_ID"
        exit $?
    fi

    echo "Read-only mode selected. Downloading reader script..."
    READER_SCRIPT_URL="https://facets-cloud.github.io/facets-schemas/scripts/gcp-account-link-reader.sh"
    TEMP_SCRIPT=$(mktemp /tmp/gcp-account-link-reader.XXXXXX.sh)

    curl -fsSL "$READER_SCRIPT_URL" -o "$TEMP_SCRIPT"

    if [ $? -ne 0 ]; then
        echo "Failed to download reader script from $READER_SCRIPT_URL"
        rm -f "$TEMP_SCRIPT"
        exit 1
    fi

    chmod +x "$TEMP_SCRIPT"

    echo "Executing reader script..."
    echo ""
    "$TEMP_SCRIPT" "$CP_URL" "$ACCOUNT_NAME" "$WEBHOOK_ID"
    EXIT_CODE=$?

    rm -f "$TEMP_SCRIPT"
    exit $EXIT_CODE
elif [ "$ACCESS_MODE" != "1" ]; then
    echo "Invalid choice. Defaulting to write access mode."
fi

echo ""
echo "Write access mode selected. Continuing with full permissions..."
echo ""

# Fetch all projects in JSON format, excluding sys- projects
echo "Fetching available projects..."
PROJECTS_JSON=$(gcloud projects list --filter="NOT projectId:sys-*" --format=json)

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

# Generate key for the Service Account.
# Write to a fresh temp path rather than a fixed name in the working directory: a
# leftover key file from an earlier run would otherwise be at risk of being sent
# instead of the key we just created, and an owner-grade key should not be left
# lying around after the script finishes.
KEY_FILE="$(mktemp).json"
trap 'rm -f "$KEY_FILE"' EXIT

gcloud iam service-accounts keys create "$KEY_FILE" --iam-account="$SA_EMAIL"

if [ $? -ne 0 ]; then
    echo "Failed to generate key for Service Account."
    exit 1
fi

echo "Service Account key generated successfully."

gcloud projects add-iam-policy-binding "$PROJECT_ID" --member="serviceAccount:$SA_EMAIL" --role="roles/owner" --condition=None --format=none --quiet

# A freshly created service account key is not usable immediately: GCP propagates
# the key to its auth backend asynchronously, and until that lands, signing a JWT
# with the key is rejected by oauth2.googleapis.com with
# "invalid_grant: Invalid JWT Signature". The control plane validates the key by
# minting a token the instant it receives it, so posting too early fails the link
# with a 400 even though the key, the project and the IAM bindings are all correct.
#
# So exercise the key here exactly the way the control plane will, and only post
# once it works. Note that `gcloud auth activate-service-account` changes the
# active gcloud account, so it runs against a throwaway CLOUDSDK_CONFIG to leave
# the caller's own gcloud configuration and active account untouched.
wait_for_key_to_become_usable() {
    local attempts=12
    local delay=5
    local probe_config
    probe_config=$(mktemp -d)

    local attempt
    for ((attempt = 1; attempt <= attempts; attempt++)); do
        if CLOUDSDK_CONFIG="$probe_config" gcloud auth activate-service-account --key-file="$KEY_FILE" &>/dev/null \
            && CLOUDSDK_CONFIG="$probe_config" gcloud auth print-access-token --account="$SA_EMAIL" &>/dev/null; then
            rm -rf "$probe_config"
            echo "Service Account key verified as active."
            return 0
        fi
        echo "Waiting for the Service Account key to propagate (attempt $attempt/$attempts)..."
        sleep "$delay"
    done

    rm -rf "$probe_config"
    return 1
}

echo "Verifying that the Service Account key is active..."
if ! wait_for_key_to_become_usable; then
    echo "The Service Account key did not become usable in time."
    echo "Google is still rejecting it, so linking would fail. Please re-run the script."
    exit 1
fi

# Base64 encode the service account key JSON
SERVICE_ACCOUNT_KEY_BASE64=$(base64 < "$KEY_FILE" | tr -d '\n')

# Prepare the curl request with base64 encoded key
CURL_DATA="{ \"payload\": { \"name\": \"$ACCOUNT_NAME\", \"serviceAccountKey\": \"$SERVICE_ACCOUNT_KEY_BASE64\", \"project\": \"$PROJECT_ID\" }, \"webhookId\": \"$WEBHOOK_ID\"}"

# Retrying the post is safe: the control plane accepts a callback for a webhook
# that is still WAITING or already FAILED, so a rejected attempt does not consume
# the webhook and the same WEBHOOK_ID can be reused.
POST_ATTEMPTS=3
for ((post_attempt = 1; post_attempt <= POST_ATTEMPTS; post_attempt++)); do
    CURL_OUTPUT=$(curl -k -s -w $'\n%{http_code}' -X POST "https://$CP_URL/public/v1/link-gcp" -H "accept: */*" -H "Content-Type: application/json; charset=utf-8" -d "$CURL_DATA")
    CURL_RESPONSE=$(echo "$CURL_OUTPUT" | tail -n 1)
    CURL_BODY=$(echo "$CURL_OUTPUT" | sed '$d')

    if [ "$CURL_RESPONSE" = "200" ]; then
        echo "Data successfully sent to the specified URL."
        exit 0
    fi

    echo "Failed to send data to the specified URL. HTTP response code: $CURL_RESPONSE"
    if [ -n "$CURL_BODY" ]; then
        echo "Response: $CURL_BODY"
    fi

    if [ "$post_attempt" -lt "$POST_ATTEMPTS" ]; then
        echo "Retrying in 10 seconds (attempt $post_attempt/$POST_ATTEMPTS)..."
        sleep 10
    fi
done

exit 1
