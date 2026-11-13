#!/bin/bash

# Setup links (control plane /v2/setup/<id>) pass the policy to apply as a file:
#   aws-account-link.sh <CP_HOST> <NAME> <WEBHOOK_ID> <FACETS_ACCOUNT_ID> --policy policy.json
# That path is strict (HTTPS verified, JSON built with jq, safe to re-run) and
# leaves the original 4-argument flow below unchanged.
if [ "$#" -eq 6 ] && [ "$5" = "--policy" ]; then
    set -euo pipefail
    CP_HOST=$1 ROLE_NAME=$2 WEBHOOK_ID=$3 ACCOUNT_ID=$4 POLICY_FILE=$6
    die() { echo "ERROR: $*" >&2; exit 1; }
    command -v aws >/dev/null || die "aws CLI not found"
    command -v jq >/dev/null || die "jq not found"
    [[ "$CP_HOST" =~ ^[A-Za-z0-9.-]+(:[0-9]+)?$ ]] || die "invalid control plane host: $CP_HOST"
    [[ "$ROLE_NAME" =~ ^[A-Za-z0-9+=,.@_-]{1,64}$ ]] || die "invalid role name: $ROLE_NAME"
    [ -n "$WEBHOOK_ID" ] || die "webhook id is required"
    [[ "$ACCOUNT_ID" =~ ^[0-9]{12}$ ]] || die "Facets AWS account id must be 12 digits"
    [ -r "$POLICY_FILE" ] || die "policy file not readable: $POLICY_FILE"
    jq -e '.Statement | length > 0' "$POLICY_FILE" >/dev/null || die "policy file is not an IAM policy: $POLICY_FILE"

    EXTERNAL_ID=$(uuidgen | tr '[:upper:]' '[:lower:]')
    TRUST_POLICY=$(jq -cn --arg acct "$ACCOUNT_ID" --arg ext "$EXTERNAL_ID" \
        '{Version:"2012-10-17",Statement:[{Effect:"Allow",Principal:{AWS:("arn:aws:iam::"+$acct+":root")},Action:"sts:AssumeRole",Condition:{StringEquals:{"sts:ExternalId":$ext}}}]}')

    # A re-run of the same link (for example after a failed callback) updates the
    # role it created; a role this link did not create is never touched.
    if EXISTING=$(aws iam get-role --role-name "$ROLE_NAME" --output json 2>/dev/null); then
        OWNER=$(aws iam list-role-tags --role-name "$ROLE_NAME" --output json | jq -r '.Tags[]? | select(.Key=="facets-setup-link") | .Value')
        [ "$OWNER" = "$WEBHOOK_ID" ] || die "IAM role $ROLE_NAME already exists and was not created by this setup link; ask for a link with another account name"
        aws iam update-assume-role-policy --role-name "$ROLE_NAME" --policy-document "$TRUST_POLICY"
        ROLE_ARN=$(echo "$EXISTING" | jq -r .Role.Arn)
        echo "Updated role: $ROLE_ARN"
    else
        ROLE_ARN=$(aws iam create-role --role-name "$ROLE_NAME" --assume-role-policy-document "$TRUST_POLICY" \
            --description "Facets control plane access, created from a setup link" \
            --tags "Key=facets-setup-link,Value=$WEBHOOK_ID" --output json | jq -r .Role.Arn)
        echo "Created role: $ROLE_ARN"
    fi
    aws iam put-role-policy --role-name "$ROLE_NAME" --policy-name "${ROLE_NAME}-Policy" --policy-document "file://$POLICY_FILE"
    echo "Attached policy ${ROLE_NAME}-Policy."

    echo "Waiting 10 seconds for IAM to propagate..."
    sleep 10
    BODY=$(jq -cn --arg w "$WEBHOOK_ID" --arg e "$EXTERNAL_ID" --arg r "$ROLE_ARN" --arg n "$ROLE_NAME" \
        '{webhookId:$w,payload:{externalId:$e,iamRole:$r,name:$n}}')
    RESP=$(mktemp)
    trap 'rm -f "$RESP"' EXIT
    # No -f: curl drops the response body on an HTTP error with -f, and that body
    # carries the control plane's reason.
    CODE=$(curl -sS --retry 3 -X POST -H 'Content-Type: application/json' -d "$BODY" -o "$RESP" \
        -w '%{http_code}' "${CP_SCHEME:-https}://${CP_HOST}/public/v1/link-aws") || die "could not reach the control plane at $CP_HOST"
    if [[ "$CODE" != 2* ]]; then
        echo "ERROR: control plane rejected the link (HTTP $CODE):" >&2; cat "$RESP" >&2; echo >&2
        echo "The role is in place; fix the issue above and run the same command again." >&2
        exit 1
    fi
    echo "Linked '$ROLE_NAME': role $ROLE_ARN trusted by AWS account $ACCOUNT_ID with an external ID."
    exit 0
fi

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <CP_URL> <ROLE_NAME> <WEBHOOK_ID> <ACCOUNT_ID> [--policy <policy.json>]"
    exit 1
fi

CP_URL=$1
ROLE_NAME=$2
WEBHOOK_ID=$3
ACCOUNT_ID=$4

# Prompt for access mode
echo "Select access mode:"
echo "  1) Write access (default) - Use this to provision environments in AWS using Facets"
echo "  2) Read-only access - Use this to discover Facets blueprint from existing setup"
read -p "Enter choice [1]: " ACCESS_MODE

# Default to write mode if empty
ACCESS_MODE=${ACCESS_MODE:-1}

# If read-only mode is selected, download and execute the reader script
if [ "$ACCESS_MODE" = "2" ]; then
    echo ""
    echo "Read-only mode selected. Downloading reader script..."
    READER_SCRIPT_URL="https://facets-cloud.github.io/facets-schemas/scripts/aws-account-link-reader.sh"
    TEMP_SCRIPT=$(mktemp /tmp/aws-account-link-reader.XXXXXX.sh)

    curl -fsSL "$READER_SCRIPT_URL" -o "$TEMP_SCRIPT"

    if [ $? -ne 0 ]; then
        echo "Failed to download reader script from $READER_SCRIPT_URL"
        rm -f "$TEMP_SCRIPT"
        exit 1
    fi

    chmod +x "$TEMP_SCRIPT"

    echo "Executing reader script..."
    echo ""
    "$TEMP_SCRIPT" "$CP_URL" "$ROLE_NAME" "$WEBHOOK_ID" "$ACCOUNT_ID"
    EXIT_CODE=$?

    rm -f "$TEMP_SCRIPT"
    exit $EXIT_CODE
elif [ "$ACCESS_MODE" != "1" ]; then
    echo "Invalid choice. Defaulting to write access mode."
fi

echo ""
echo "Write access mode selected. Continuing with full permissions..."
echo ""

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

echo "externalId: $EXTERNAL_ID"
echo "iamRole: $ROLE_ARN"
echo "name: $ROLE_NAME"

# Sleep 3 seconds
echo "Sleeping 10 seconds"
sleep 10

# Post data using curl
curl -k -X POST "https://${CP_URL}/public/v1/link-aws" -H "accept: */*" -H "Content-Type: application/json; charset=utf-8" -d "{ \"payload\":{ \"externalId\": \"$EXTERNAL_ID\", \"iamRole\": \"$ROLE_ARN\", \"name\": \"$ROLE_NAME\" }, \"webhookId\": \"$WEBHOOK_ID\"}"

echo "Data posted successfully to ${CP_URL}."
