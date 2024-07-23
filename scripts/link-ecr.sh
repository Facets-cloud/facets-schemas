#!/bin/bash

if [ "$#" -ne 3 ]; then
	echo "Usage: $0 <accountName> <callbackHost> <webhookId>"
	exit 1
fi

ACCOUNT_NAME=$1
CALLBACKHOST=$2
WEBHOOKID=$3

read -p "Please enter the AWS region: " REGION

REGION_VALID=false
AVAILABLE_REGIONS=$(aws ec2 describe-regions --query "Regions[].RegionName" --output text)
for AVAILABLE_REGION in $AVAILABLE_REGIONS; do
	if [ "$AVAILABLE_REGION" == "$REGION" ]; then
		REGION_VALID=true
		break
	fi
done

if ! $REGION_VALID; then
	echo "Invalid region: $REGION"
	exit 1
fi

aws iam create-user --user-name "$ACCOUNT_NAME" >/dev/null
ACCESS_KEY_JSON=$(aws iam create-access-key --user-name "$ACCOUNT_NAME")

ACCESS_KEY_ID=$(echo $ACCESS_KEY_JSON | jq -r '.AccessKey.AccessKeyId')
SECRET_ACCESS_KEY=$(echo $ACCESS_KEY_JSON | jq -r '.AccessKey.SecretAccessKey')

echo "Access Key ID: $ACCESS_KEY_ID"
echo "Secret Access Key: $SECRET_ACCESS_KEY"

aws iam attach-user-policy --user-name "$ACCOUNT_NAME" --policy-arn "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryPowerUser"

AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)
ECR_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com"

echo "ECR URI: $ECR_URI"


PAYLOAD='{
    "payload": {
        "artifactoryType": "ECR",
        "awsAccountId": "'"$AWS_ACCOUNT_ID"'",
        "awsKey": "'"$ACCESS_KEY_ID"'",
        "awsRegion": "'"$REGION"'",
        "awsSecret": "'"$SECRET_ACCESS_KEY"'",
        "name": "'"$ACCOUNT_NAME"'",
        "uri": "'"$ECR_URI"'"
    },
    "webhookId": "'"$WEBHOOKID"'"
}'


curl -k -X POST "https://${CALLBACKHOST}/public/v1/link-ecr" \
    -H "Content-Type: application/json" \
    -d "$PAYLOAD"

echo "CURL request to the callback URI has been made with the ECR details."
