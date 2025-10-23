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

# Sleep 10 seconds
echo "Sleeping 10 seconds"
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
