#!/bin/bash

set -euo pipefail

# Function to prompt user for confirmation
confirm() {
    read -r -p "$1 [Y/n] " response
    case "$response" in
        [nN][oO]|[nN])
            false
            ;;
        *)
            true
            ;;
    esac
}

# Check if required arguments are provided
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 CP_URL SERVICE_ACCOUNT_NAME WEBHOOK_ID"
    exit 1
fi

# Assign arguments to variables
CP_URL="${1}"
SERVICE_ACCOUNT_NAME="${2}"
WEBHOOK_ID="${3}"
NAMESPACE="default"  # You can change this if you want to create the service account in a different namespace

# Get the current Kubernetes context
CURRENT_CONTEXT=$(kubectl config current-context)

echo "Current Kubernetes context: $CURRENT_CONTEXT"

# Prompt user for confirmation before proceeding
confirm "Do you want to create resources in the current Kubernetes context? ($CURRENT_CONTEXT)" || exit

# Create the service account in the specified namespace
kubectl create serviceaccount "${SERVICE_ACCOUNT_NAME}" --namespace "${NAMESPACE}"

# Create a cluster role binding for the service account, granting it view (read-only) access
kubectl create clusterrolebinding "${SERVICE_ACCOUNT_NAME}-view-binding" --clusterrole=view --serviceaccount="${NAMESPACE}:${SERVICE_ACCOUNT_NAME}"

# Create a secret of type service account token for the service account
SECRET_NAME="${SERVICE_ACCOUNT_NAME}-token"

cat <<EOF > UPDATED_SECRET.YAML
apiVersion: v1
kind: Secret
metadata:
  name: $SECRET_NAME
  annotations:
    kubernetes.io/service-account.name: "$SERVICE_ACCOUNT_NAME"
type: kubernetes.io/service-account-token
EOF

# Apply the changes using kubectl
kubectl apply -f UPDATED_SECRET.YAML

# Get the API server endpoint
APISERVER=$(kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}')

# Read the token from the Kubernetes secret
TOKEN=$(kubectl get secret "$SECRET_NAME" -o jsonpath='{.data.token}'| base64 --decode)

# Read the CA certificate from the Kubernetes secret
CA_CERT=$(kubectl get secret "$SECRET_NAME" -o jsonpath='{.data.ca\.crt}')

echo "Cluster information:"
echo "Token: ${TOKEN}"
echo "CA certificate : ${CA_CERT}"
echo "HOST: ${APISERVER}"

# Prepare the curl request with proper JSON escaping
CURL_DATA=$(jq -n \
  --arg token "$TOKEN" \
  --arg ca "$CA_CERT" \
  --arg host "$APISERVER" \
  --arg name "$SERVICE_ACCOUNT_NAME" \
  --arg webhookId "$WEBHOOK_ID" \
  '{payload: {token: $token, certificateAuthority: $ca, host: $host, name: $name}, webhookId: $webhookId}')

CURL_RESPONSE=$(curl -k -s -w "\n%{http_code}" -X POST "https://${CP_URL}/public/v1/link-kubernetes" \
    -H "accept: */*" \
    -H "Content-Type: application/json; charset=utf-8" \
    -d "$CURL_DATA")

HTTP_CODE=$(echo "$CURL_RESPONSE" | tail -n1)
RESPONSE_BODY=$(echo "$CURL_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -ne 200 ]; then
    echo "Failed to send data to the specified URL. HTTP response code: $HTTP_CODE"
    echo "Response body: $RESPONSE_BODY"
    exit 1
fi

echo "Data successfully sent to the specified URL."
