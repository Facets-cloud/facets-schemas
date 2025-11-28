#!/bin/bash

# Check if CP_URL, ROLE_NAME, WEBHOOK_ID, and ACCOUNT_ID were provided as arguments
if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <CP_URL> <ROLE_NAME> <WEBHOOK_ID> <ACCOUNT_ID>"
    exit 1
fi

CP_URL=$1
ROLE_NAME=$2
WEBHOOK_ID=$3
ACCOUNT_ID=$4

# Generate External ID
EXTERNAL_ID=$(uuidgen)
echo "Generated External ID: $EXTERNAL_ID"

# Create trust policy
TRUST_POLICY="{\"Version\": \"2012-10-17\", \"Statement\": [{\"Effect\": \"Allow\", \"Principal\": {\"AWS\": \"arn:aws:iam::$ACCOUNT_ID:root\"}, \"Action\": \"sts:AssumeRole\", \"Condition\": {\"StringEquals\": {\"sts:ExternalId\": \"$EXTERNAL_ID\"}}}]}"

# Create IAM role
CREATE_ROLE_OUTPUT=$(aws iam create-role --role-name "$ROLE_NAME" --assume-role-policy-document "$TRUST_POLICY" --description "Read-only role for Facets discovery" 2>&1)
CREATE_ROLE_EXIT_CODE=$?

if [ $CREATE_ROLE_EXIT_CODE -ne 0 ]; then
    if echo "$CREATE_ROLE_OUTPUT" | grep -q "EntityAlreadyExists"; then
        echo "Role '$ROLE_NAME' already exists."
        read -p "Do you want to use the existing role? (y/n): " USE_EXISTING

        if [[ "$USE_EXISTING" =~ ^[Yy]$ ]]; then
            # Get existing role ARN
            ROLE_ARN=$(aws iam get-role --role-name "$ROLE_NAME" --query 'Role.Arn' --output text)
            if [ $? -ne 0 ]; then
                echo "Failed to get existing role details."
                exit 1
            fi
            echo "Using existing role: $ROLE_ARN"

            # Update trust policy
            echo "Updating trust policy..."
            aws iam update-assume-role-policy --role-name "$ROLE_NAME" --policy-document "$TRUST_POLICY"
            if [ $? -ne 0 ]; then
                echo "Failed to update trust policy."
                exit 1
            fi
        else
            echo "Exiting. Please choose a different role name or delete the existing role."
            exit 1
        fi
    else
        echo "Failed to create IAM role: $CREATE_ROLE_OUTPUT"
        exit 1
    fi
else
    ROLE_ARN=$(echo "$CREATE_ROLE_OUTPUT" | jq -r .Role.Arn)
    echo "Role created successfully: $ROLE_ARN"
fi

# Attach AWS managed ReadOnlyAccess policy
aws iam attach-role-policy --role-name "$ROLE_NAME" --policy-arn "arn:aws:iam::aws:policy/ReadOnlyAccess"
if [ $? -ne 0 ]; then
    echo "Failed to attach ReadOnlyAccess policy to IAM role."
    exit 1
fi
echo "ReadOnlyAccess policy attached successfully."

# Attach SSM policy for jumpbox access
aws iam attach-role-policy --role-name "$ROLE_NAME" --policy-arn "arn:aws:iam::aws:policy/AmazonSSMReadOnlyAccess"
if [ $? -ne 0 ]; then
    echo "Warning: Failed to attach SSMReadOnlyAccess policy."
fi

# Create inline policy for SSM StartSession
SSM_SESSION_POLICY='{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ssm:StartSession",
                "ssm:TerminateSession",
                "ssm:ResumeSession",
                "ssm:DescribeSessions",
                "ssm:GetConnectionStatus"
            ],
            "Resource": "*"
        }
    ]
}'

aws iam put-role-policy --role-name "$ROLE_NAME" --policy-name "SSMSessionAccess" --policy-document "$SSM_SESSION_POLICY" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "SSM session access policy attached successfully."
else
    echo "Warning: Failed to attach SSM session policy."
fi

# Initialize jumpbox info array for private clusters
declare -a JUMPBOX_INFO

# Ask if user wants to add EKS cluster read access
echo ""
read -p "Do you want to add read access to EKS clusters? (y/n): " ADD_EKS_ACCESS

if [[ "$ADD_EKS_ACCESS" =~ ^[Yy]$ ]]; then
    # Prompt for AWS region
    read -p "Enter AWS region (e.g., us-east-1): " AWS_REGION

    if [ -z "$AWS_REGION" ]; then
        echo "No region provided. Skipping EKS configuration..."
    else
        echo "Fetching EKS clusters in region $AWS_REGION..."
        EKS_CLUSTERS_JSON=$(aws eks list-clusters --region "$AWS_REGION" --query "clusters" --output json 2>&1)
        EKS_LIST_EXIT_CODE=$?

        if [ $EKS_LIST_EXIT_CODE -ne 0 ]; then
            echo "Failed to fetch EKS clusters: $EKS_CLUSTERS_JSON"
            echo "Skipping EKS configuration..."
        elif [ "$(echo "$EKS_CLUSTERS_JSON" | jq 'length')" -eq 0 ]; then
            echo "No EKS clusters found in region $AWS_REGION."
        else
            # Display clusters
            echo ""
            echo "Available EKS clusters:"
            echo "$EKS_CLUSTERS_JSON" | jq -r '.[]' | while read -r cluster; do
                echo "  - $cluster"
            done
            echo ""
            echo "Enter cluster names separated by spaces (or 'all' for all clusters):"
            read -p "Clusters: " CLUSTER_INPUT

            SELECTED_CLUSTERS=()
            if [[ "$CLUSTER_INPUT" == "all" ]]; then
                readarray -t SELECTED_CLUSTERS < <(echo "$EKS_CLUSTERS_JSON" | jq -r '.[]')
            else
                read -ra SELECTED_CLUSTERS <<< "$CLUSTER_INPUT"
            fi

            if [ ${#SELECTED_CLUSTERS[@]} -eq 0 ]; then
                echo "No clusters selected. Skipping EKS configuration."
            else
                echo ""
                echo "Configuring read access for ${#SELECTED_CLUSTERS[@]} cluster(s)..."

                for CLUSTER_NAME in "${SELECTED_CLUSTERS[@]}"; do
                    # Verify cluster exists
                    CLUSTER_INFO=$(aws eks describe-cluster --name "$CLUSTER_NAME" --region "$AWS_REGION" --output json 2>&1)
                    if [ $? -ne 0 ]; then
                        echo "  Warning: Cluster '$CLUSTER_NAME' not found. Skipping..."
                        continue
                    fi

                    # Get authentication mode
                    AUTH_MODE=$(echo "$CLUSTER_INFO" | jq -r '.cluster.accessConfig.authenticationMode // "CONFIG_MAP"')
                    echo ""
                    echo "  Processing cluster '$CLUSTER_NAME' (auth mode: $AUTH_MODE)..."

                    # Handle CONFIG_MAP mode
                    if [ "$AUTH_MODE" == "CONFIG_MAP" ]; then
                        echo ""
                        echo "  Cluster '$CLUSTER_NAME' currently only supports ConfigMap-based authentication."
                        echo "  Enabling IAM access entries is safe - all existing aws-auth ConfigMap entries continue to work."
                        read -p "  Enable IAM access for this cluster? (y/n): " UPGRADE_CLUSTER

                        if [[ "$UPGRADE_CLUSTER" =~ ^[Yy]$ ]]; then
                            echo "  Enabling IAM access entries..."
                            UPDATE_OUTPUT=$(aws eks update-cluster-config \
                                --name "$CLUSTER_NAME" \
                                --region "$AWS_REGION" \
                                --access-config authenticationMode=API_AND_CONFIG_MAP 2>&1)

                            if [ $? -ne 0 ]; then
                                echo "  Failed to upgrade cluster: $UPDATE_OUTPUT"
                                echo "  Skipping cluster '$CLUSTER_NAME'..."
                                continue
                            fi

                            echo "  Waiting for IAM access to be enabled (this may take a few minutes)..."
                            aws eks wait cluster-active --name "$CLUSTER_NAME" --region "$AWS_REGION"

                            if [ $? -ne 0 ]; then
                                echo "  Timed out waiting for cluster. Skipping..."
                                continue
                            fi

                            # Poll until auth mode actually changes (eventual consistency)
                            echo "  Verifying authentication mode change..."
                            POLL_COUNT=0
                            MAX_POLLS=60  # 5 minutes max (60 * 5 seconds)
                            while [ $POLL_COUNT -lt $MAX_POLLS ]; do
                                CURRENT_MODE=$(aws eks describe-cluster --name "$CLUSTER_NAME" --region "$AWS_REGION" \
                                    --query 'cluster.accessConfig.authenticationMode' --output text 2>/dev/null)
                                if [ "$CURRENT_MODE" == "API_AND_CONFIG_MAP" ]; then
                                    break
                                fi
                                sleep 5
                                POLL_COUNT=$((POLL_COUNT + 1))
                            done

                            if [ "$CURRENT_MODE" != "API_AND_CONFIG_MAP" ]; then
                                echo "  Timed out waiting for auth mode change. Skipping..."
                                continue
                            fi

                            echo "  IAM access entries enabled successfully."
                        else
                            echo "  Skipping cluster '$CLUSTER_NAME'..."
                            continue
                        fi
                    fi

                    # Create access entry
                    echo "  Creating access entry..."
                    ACCESS_ENTRY_OUTPUT=$(aws eks create-access-entry \
                        --cluster-name "$CLUSTER_NAME" \
                        --region "$AWS_REGION" \
                        --principal-arn "$ROLE_ARN" \
                        --type STANDARD 2>&1)

                    if [ $? -ne 0 ]; then
                        if echo "$ACCESS_ENTRY_OUTPUT" | grep -q "ResourceInUseException"; then
                            echo "  Access entry already exists for this role."
                        else
                            echo "  Failed to create access entry: $ACCESS_ENTRY_OUTPUT"
                            continue
                        fi
                    fi

                    # Associate read-only access policy
                    echo "  Associating read-only access policy..."
                    POLICY_OUTPUT=$(aws eks associate-access-policy \
                        --cluster-name "$CLUSTER_NAME" \
                        --region "$AWS_REGION" \
                        --principal-arn "$ROLE_ARN" \
                        --policy-arn "arn:aws:eks::aws:cluster-access-policy/AmazonEKSAdminViewPolicy" \
                        --access-scope type=cluster 2>&1)

                    if [ $? -ne 0 ]; then
                        if echo "$POLICY_OUTPUT" | grep -q "ResourceInUseException"; then
                            echo "  Access policy already associated."
                        else
                            echo "  Failed to associate access policy: $POLICY_OUTPUT"
                            continue
                        fi
                    fi

                    echo "  ✓ Successfully configured read access for cluster '$CLUSTER_NAME'"

                    # Check if cluster is private
                    PUBLIC_ACCESS=$(echo "$CLUSTER_INFO" | jq -r '.cluster.resourcesVpcConfig.endpointPublicAccess')
                    PRIVATE_ACCESS=$(echo "$CLUSTER_INFO" | jq -r '.cluster.resourcesVpcConfig.endpointPrivateAccess')
                    CLUSTER_VPC=$(echo "$CLUSTER_INFO" | jq -r '.cluster.resourcesVpcConfig.vpcId')

                    if [ "$PUBLIC_ACCESS" == "false" ] && [ "$PRIVATE_ACCESS" == "true" ]; then
                        echo ""
                        echo "  Note: Cluster '$CLUSTER_NAME' has a private endpoint only."
                        echo "  A jumpbox (EC2 instance with SSM) in the cluster VPC is required for kubectl access."
                        echo ""
                        read -p "  Do you want to select a jumpbox instance now? (y/n): " SELECT_JUMPBOX

                        if [[ "$SELECT_JUMPBOX" =~ ^[Yy]$ ]]; then
                            # Find SSM-enabled instances in the cluster VPC
                            echo "  Fetching SSM-enabled instances in VPC $CLUSTER_VPC..."
                            SSM_INSTANCES=$(aws ssm describe-instance-information \
                                --region "$AWS_REGION" \
                                --query "InstanceInformationList[?PingStatus=='Online'].[InstanceId]" \
                                --output text 2>/dev/null)

                            if [ -z "$SSM_INSTANCES" ]; then
                                echo "  No SSM-enabled instances found online."
                                echo "  You'll need to set up a jumpbox with SSM agent in VPC $CLUSTER_VPC"
                            else
                                # Filter instances by VPC
                                VPC_INSTANCES=()
                                for INSTANCE_ID in $SSM_INSTANCES; do
                                    INSTANCE_VPC=$(aws ec2 describe-instances \
                                        --instance-ids "$INSTANCE_ID" \
                                        --region "$AWS_REGION" \
                                        --query "Reservations[0].Instances[0].VpcId" \
                                        --output text 2>/dev/null)
                                    if [ "$INSTANCE_VPC" == "$CLUSTER_VPC" ]; then
                                        INSTANCE_NAME=$(aws ec2 describe-instances \
                                            --instance-ids "$INSTANCE_ID" \
                                            --region "$AWS_REGION" \
                                            --query "Reservations[0].Instances[0].Tags[?Key=='Name'].Value" \
                                            --output text 2>/dev/null)
                                        VPC_INSTANCES+=("$INSTANCE_ID:${INSTANCE_NAME:-unnamed}")
                                    fi
                                done

                                if [ ${#VPC_INSTANCES[@]} -eq 0 ]; then
                                    echo "  No SSM-enabled instances found in cluster VPC."
                                    echo "  You'll need to set up a jumpbox with SSM agent in VPC $CLUSTER_VPC"
                                else
                                    echo ""
                                    echo "  Available SSM-enabled instances in cluster VPC:"
                                    for i in "${!VPC_INSTANCES[@]}"; do
                                        IFS=':' read -r inst_id inst_name <<< "${VPC_INSTANCES[$i]}"
                                        echo "    $((i+1))) $inst_id ($inst_name)"
                                    done
                                    echo ""
                                    read -p "  Select instance number (or press Enter to skip): " INSTANCE_CHOICE

                                    if [ -n "$INSTANCE_CHOICE" ] && [ "$INSTANCE_CHOICE" -ge 1 ] && [ "$INSTANCE_CHOICE" -le ${#VPC_INSTANCES[@]} ]; then
                                        SELECTED_INSTANCE="${VPC_INSTANCES[$((INSTANCE_CHOICE-1))]}"
                                        IFS=':' read -r JUMPBOX_ID JUMPBOX_NAME <<< "$SELECTED_INSTANCE"

                                        # Verify SSM connection status
                                        echo "  Verifying SSM connectivity for $JUMPBOX_ID..."
                                        SSM_STATUS=$(aws ssm get-connection-status \
                                            --target "$JUMPBOX_ID" \
                                            --region "$AWS_REGION" \
                                            --query 'Status' \
                                            --output text 2>&1)

                                        if [ "$SSM_STATUS" == "connected" ]; then
                                            # Store jumpbox info for this cluster (including VPC for reference)
                                            JUMPBOX_INFO+=("$CLUSTER_NAME:$AWS_REGION:$JUMPBOX_ID:$JUMPBOX_NAME:$CLUSTER_VPC")
                                            echo "  ✓ SSM connectivity verified for $JUMPBOX_ID ($JUMPBOX_NAME) in VPC $CLUSTER_VPC"
                                        else
                                            echo "  ✗ SSM not connected for $JUMPBOX_ID (status: $SSM_STATUS)"
                                            echo "    Ensure the instance has SSM agent running and proper IAM role."
                                        fi
                                    fi
                                fi
                            fi
                        fi
                    fi
                done

                echo ""
                echo "EKS cluster access configuration complete."
            fi
        fi
    fi
fi

# Sleep 10 seconds
echo ""
echo "Sleeping 10 seconds..."
sleep 10

# Post data using curl
echo "Sending data to Facets..."
CURL_RESPONSE=$(curl -k -s -w "\n%{http_code}" -X POST "https://${CP_URL}/public/v1/link-aws" \
    -H "accept: */*" \
    -H "Content-Type: application/json; charset=utf-8" \
    -d "{ \"payload\":{ \"externalId\": \"$EXTERNAL_ID\", \"iamRole\": \"$ROLE_ARN\", \"name\": \"$ROLE_NAME\" }, \"webhookId\": \"$WEBHOOK_ID\"}")

HTTP_CODE=$(echo "$CURL_RESPONSE" | tail -n1)
RESPONSE_BODY=$(echo "$CURL_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -ne 200 ]; then
    echo "Failed to send data to the specified URL. HTTP response code: $HTTP_CODE"
    echo "Response body: $RESPONSE_BODY"
    exit 1
fi

echo "Data successfully sent to Facets."
echo ""
echo "============================================"
echo "Summary"
echo "============================================"
echo "  Role ARN: $ROLE_ARN"
echo "  External ID: $EXTERNAL_ID"

# Print jumpbox info if any private clusters were configured
if [ ${#JUMPBOX_INFO[@]} -gt 0 ]; then
    echo ""
    echo "Private Cluster Jumpboxes:"
    echo "--------------------------------------------"
    for info in "${JUMPBOX_INFO[@]}"; do
        IFS=':' read -r cluster region jumpbox_id jumpbox_name vpc_id <<< "$info"
        echo "  Cluster: $cluster"
        echo "    Region: $region"
        echo "    VPC: $vpc_id"
        echo "    Jumpbox Instance: $jumpbox_id ($jumpbox_name)"
        echo "    Connect via: aws ssm start-session --target $jumpbox_id --region $region"
        echo ""
    done
fi
echo "============================================"
