#!/bin/bash

# Check if CP_URL, PRINCIPAL_NAME, and WEBHOOK_ID were provided as arguments
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <CP_URL> <PRINCIPAL_NAME> <WEBHOOK_ID>"
    exit 1
fi

CP_URL=$1
ACCOUNT_NAME="$2"
PRINCIPAL_NAME="facets-$2"
WEBHOOK_ID=$3

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

# Check if the service account already exists
SA_EMAIL="$PRINCIPAL_NAME@$PROJECT_ID.iam.gserviceaccount.com"
EXISTING_SA=$(gcloud iam service-accounts list --filter="email:$SA_EMAIL" --format="value(email)")

if [ -z "$EXISTING_SA" ]; then
    echo "Creating Service Account '$PRINCIPAL_NAME'..."
    gcloud iam service-accounts create "$PRINCIPAL_NAME" --display-name="Facets Reader ($PRINCIPAL_NAME)"

    if [ $? -ne 0 ]; then
        echo "Failed to create Service Account."
        exit 1
    fi

    # Wait for a short period to ensure the service account is fully created
    sleep 5
else
    echo "Service Account $SA_EMAIL already exists."
fi

# Assign Read-Only IAM roles to the service account
echo "Assigning read-only IAM policy bindings..."

READ_ONLY_ROLES=(
    "roles/viewer"
    "roles/container.viewer"
    "roles/container.clusterViewer"
    "roles/iam.securityReviewer"
    "roles/secretmanager.viewer"
    "roles/cloudasset.viewer"
    "roles/compute.networkViewer"
    "roles/cloudkms.viewer"
)

for role in "${READ_ONLY_ROLES[@]}"; do
    echo "  - Attaching $role..."
    gcloud projects add-iam-policy-binding "$PROJECT_ID" \
        --member="serviceAccount:$SA_EMAIL" \
        --role="$role" \
        --condition=None --quiet

    if [ $? -ne 0 ]; then
        echo "    Warning: Failed to attach $role. Proceeding with remaining roles..."
    fi
done

echo "Read-only policy bindings attached successfully."

# Generate key for the Service Account.
KEY_FILE="$(mktemp).json"
trap 'rm -f "$KEY_FILE"' EXIT

gcloud iam service-accounts keys create "$KEY_FILE" --iam-account="$SA_EMAIL"

if [ $? -ne 0 ]; then
    echo "Failed to generate key for Service Account."
    exit 1
fi

echo "Service Account key generated successfully."

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
