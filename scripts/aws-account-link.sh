#!/bin/bash

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <ROLE_NAME> <ACCOUNT_ID> <CP_URL> <WEBHOOK_ID>"
    exit 1
fi

CP_URL=$1
ROLE_NAME=$2
WEBHOOK_ID=$3
ACCOUNT_ID=$4
POLICY_URL="https://facets-cloud.github.io/facets-schemas/scripts/aws-policy.json"

EXTERNAL_ID=$(uuidgen)
echo "Generated External ID: $EXTERNAL_ID"

POLICY_JSON=$(curl -s "$POLICY_URL")
if [ -z "$POLICY_JSON" ]; then
    echo "Failed to download policy JSON. Please check the URL and try again."
    exit 1
fi

TRUST_POLICY="{\"Version\": \"2012-10-17\", \"Statement\": [{\"Effect\": \"Allow\", \"Principal\": {\"AWS\": \"arn:aws:iam::$ACCOUNT_ID:root\"}, \"Action\": \"sts:AssumeRole\", \"Condition\": {\"StringEquals\": {\"sts:ExternalId\": \"$EXTERNAL_ID\"}}}]}"

CREATE_ROLE_OUTPUT=$(aws iam create-role --role-name "$ROLE_NAME" --assume-role-policy-document "$TRUST_POLICY" --description "Role created from external policy JSON")
if [ $? -ne 0 ]; then
    echo "Failed to create IAM role. Ensure you have the necessary permissions."
    exit 1
fi

ROLE_ARN=$(echo "$CREATE_ROLE_OUTPUT" | jq -r .Role.Arn)
echo "Role created successfully: $ROLE_ARN"

# Save the policy to a temporary file
POLICY_FILE=$(mktemp)
echo "$POLICY_JSON" > "$POLICY_FILE"

# Now use the file to attach the policy
ATTACH_POLICY_OUTPUT=$(aws iam put-role-policy --role-name "$ROLE_NAME" --policy-name "${ROLE_NAME}-Policy" --policy-document file://"$POLICY_FILE")
if [ $? -ne 0 ]; then
    echo "Failed to attach policy to IAM role. Ensure you have the necessary permissions."
    rm "$POLICY_FILE"
    exit 1
fi

echo "Policy attached successfully to '$ROLE_NAME'."

# Cleanup the temporary policy file
rm "$POLICY_FILE"

# Sleep 3 seconds
echo "Sleeping 10 seconds"
sleep 10

# Post data using curl
curl -X POST "https://${CP_URL}/public/v1/link-aws" -H "accept: */*" -H "Content-Type: application/json; charset=utf-8" -d "{ \"payload\":{ \"externalId\": \"$EXTERNAL_ID\", \"iamRole\": \"$ROLE_ARN\", \"name\": \"$ROLE_NAME\" }, \"webhookId\": \"$WEBHOOK_ID\"}"

echo "Data posted successfully to ${CP_URL}."
