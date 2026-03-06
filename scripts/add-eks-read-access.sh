#!/bin/bash
#
# add-eks-read-access.sh
#
# Grants an existing IAM role read-only access to EKS clusters using
# EKS access entries and the AmazonEKSViewPolicy.
#
# For clusters using CONFIG_MAP auth, the script can optionally upgrade
# to API_AND_CONFIG_MAP (safe — existing aws-auth ConfigMap entries
# continue to work).
#
# Usage:
#   ./add-eks-read-access.sh --role-arn <ROLE_ARN> [--region <AWS_REGION>]
#
# Requirements: aws cli v2, jq

set -euo pipefail

# ──────────────────────────────────────────────────────────────────────
# Parse arguments
# ──────────────────────────────────────────────────────────────────────
ROLE_ARN=""
AWS_REGION=""

usage() {
    echo "Usage: $0 --role-arn <ROLE_ARN> [--region <AWS_REGION>]"
    echo ""
    echo "Grants an IAM role read-only access to EKS clusters."
    echo ""
    echo "Options:"
    echo "  --role-arn    ARN of the IAM role to grant access (required)"
    echo "  --region      AWS region to scan for EKS clusters (prompted if not provided)"
    echo "  -h, --help    Show this help message"
    exit 1
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        --role-arn)
            ROLE_ARN="$2"
            shift 2
            ;;
        --region)
            AWS_REGION="$2"
            shift 2
            ;;
        -h|--help)
            usage
            ;;
        *)
            echo "Unknown option: $1"
            usage
            ;;
    esac
done

if [ -z "$ROLE_ARN" ]; then
    echo "Error: --role-arn is required."
    echo ""
    usage
fi

# Prompt for region if not provided
if [ -z "$AWS_REGION" ]; then
    read -p "Enter AWS region (e.g., us-east-1): " AWS_REGION
    if [ -z "$AWS_REGION" ]; then
        echo "Error: Region is required."
        exit 1
    fi
fi

# Verify the role exists
echo "Verifying role: $ROLE_ARN"
if ! aws iam get-role --role-name "$(echo "$ROLE_ARN" | awk -F/ '{print $NF}')" > /dev/null 2>&1; then
    echo "Error: Role '$ROLE_ARN' not found. Please create the role first."
    exit 1
fi
echo "Role verified."

# ──────────────────────────────────────────────────────────────────────
# List EKS clusters
# ──────────────────────────────────────────────────────────────────────
echo ""
echo "Fetching EKS clusters in region $AWS_REGION..."
EKS_CLUSTERS_JSON=$(aws eks list-clusters --region "$AWS_REGION" --query "clusters" --output json 2>&1)

if [ $? -ne 0 ]; then
    echo "Failed to fetch EKS clusters: $EKS_CLUSTERS_JSON"
    exit 1
fi

CLUSTER_COUNT=$(echo "$EKS_CLUSTERS_JSON" | jq 'length')
if [ "$CLUSTER_COUNT" -eq 0 ]; then
    echo "No EKS clusters found in region $AWS_REGION."
    exit 0
fi

echo ""
echo "Available EKS clusters:"
echo "$EKS_CLUSTERS_JSON" | jq -r '.[]' | while read -r cluster; do
    echo "  - $cluster"
done

# ──────────────────────────────────────────────────────────────────────
# Select clusters
# ──────────────────────────────────────────────────────────────────────
echo ""
echo "Enter cluster names separated by spaces (or 'all' for all clusters):"
read -p "Clusters: " CLUSTER_INPUT

SELECTED_CLUSTERS=()
if [[ "$CLUSTER_INPUT" == "all" ]]; then
    while IFS= read -r line; do
        SELECTED_CLUSTERS+=("$line")
    done < <(echo "$EKS_CLUSTERS_JSON" | jq -r '.[]')
else
    read -ra SELECTED_CLUSTERS <<< "$CLUSTER_INPUT"
fi

if [ ${#SELECTED_CLUSTERS[@]} -eq 0 ]; then
    echo "No clusters selected. Exiting."
    exit 0
fi

# ──────────────────────────────────────────────────────────────────────
# Configure access for each cluster
# ──────────────────────────────────────────────────────────────────────
echo ""
echo "Configuring read access for ${#SELECTED_CLUSTERS[@]} cluster(s)..."

SUCCESS_COUNT=0
FAIL_COUNT=0

for CLUSTER_NAME in "${SELECTED_CLUSTERS[@]}"; do
    echo ""
    echo "──────────────────────────────────────────"
    echo "Cluster: $CLUSTER_NAME"
    echo "──────────────────────────────────────────"

    # Verify cluster exists
    CLUSTER_INFO=$(aws eks describe-cluster --name "$CLUSTER_NAME" --region "$AWS_REGION" --output json 2>&1)
    if [ $? -ne 0 ]; then
        echo "  Warning: Cluster '$CLUSTER_NAME' not found. Skipping."
        FAIL_COUNT=$((FAIL_COUNT + 1))
        continue
    fi

    # Get authentication mode
    AUTH_MODE=$(echo "$CLUSTER_INFO" | jq -r '.cluster.accessConfig.authenticationMode // "CONFIG_MAP"')
    echo "  Authentication mode: $AUTH_MODE"

    # Handle CONFIG_MAP mode — needs upgrade to support access entries
    if [ "$AUTH_MODE" == "CONFIG_MAP" ]; then
        echo ""
        echo "  This cluster currently only supports ConfigMap-based authentication."
        echo "  Enabling IAM access entries is safe — all existing aws-auth ConfigMap"
        echo "  entries continue to work alongside the new access entries."
        read -p "  Enable IAM access entries for this cluster? (y/n): " UPGRADE_CLUSTER

        if [[ ! "$UPGRADE_CLUSTER" =~ ^[Yy]$ ]]; then
            echo "  Skipping cluster '$CLUSTER_NAME'."
            FAIL_COUNT=$((FAIL_COUNT + 1))
            continue
        fi

        echo "  Enabling IAM access entries (API_AND_CONFIG_MAP)..."
        UPDATE_OUTPUT=$(aws eks update-cluster-config \
            --name "$CLUSTER_NAME" \
            --region "$AWS_REGION" \
            --access-config authenticationMode=API_AND_CONFIG_MAP 2>&1)

        if [ $? -ne 0 ]; then
            echo "  Failed to upgrade cluster: $UPDATE_OUTPUT"
            echo "  Skipping cluster '$CLUSTER_NAME'."
            FAIL_COUNT=$((FAIL_COUNT + 1))
            continue
        fi

        echo "  Waiting for cluster to become active (this may take a few minutes)..."
        aws eks wait cluster-active --name "$CLUSTER_NAME" --region "$AWS_REGION"

        if [ $? -ne 0 ]; then
            echo "  Timed out waiting for cluster. Skipping."
            FAIL_COUNT=$((FAIL_COUNT + 1))
            continue
        fi

        # Poll until auth mode actually changes (eventual consistency)
        echo "  Verifying authentication mode change..."
        POLL_COUNT=0
        MAX_POLLS=60
        CURRENT_MODE=""
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
            echo "  Timed out waiting for auth mode change. Skipping."
            FAIL_COUNT=$((FAIL_COUNT + 1))
            continue
        fi

        echo "  IAM access entries enabled successfully."
    fi

    # Create access entry for the role
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
            FAIL_COUNT=$((FAIL_COUNT + 1))
            continue
        fi
    fi

    # Associate the read-only view policy
    echo "  Associating AmazonEKSViewPolicy..."
    POLICY_OUTPUT=$(aws eks associate-access-policy \
        --cluster-name "$CLUSTER_NAME" \
        --region "$AWS_REGION" \
        --principal-arn "$ROLE_ARN" \
        --policy-arn "arn:aws:eks::aws:cluster-access-policy/AmazonEKSViewPolicy" \
        --access-scope type=cluster 2>&1)

    if [ $? -ne 0 ]; then
        if echo "$POLICY_OUTPUT" | grep -q "ResourceInUseException"; then
            echo "  Access policy already associated."
        else
            echo "  Failed to associate access policy: $POLICY_OUTPUT"
            FAIL_COUNT=$((FAIL_COUNT + 1))
            continue
        fi
    fi

    echo "  Done — read access configured for '$CLUSTER_NAME'."
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
done

# ──────────────────────────────────────────────────────────────────────
# Summary
# ──────────────────────────────────────────────────────────────────────
echo ""
echo "============================================"
echo "Summary"
echo "============================================"
echo "  Role:       $ROLE_ARN"
echo "  Region:     $AWS_REGION"
echo "  Succeeded:  $SUCCESS_COUNT cluster(s)"
echo "  Skipped:    $FAIL_COUNT cluster(s)"
echo "============================================"
