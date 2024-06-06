#!/bin/bash

# Command-line arguments
USERNAME=$1
CP_URL=$2
PROJECT_NAME=$3
SERVICE_NAME=$4
ARTIFACTORY_NAME=$5
IS_PUSH=$6

# Check required environment variables and set GIT_REF if not provided
if [ -z "$DOCKER_IMAGE_URL" ] || [ -z "$TOKEN" ]; then
    echo "Environment variables DOCKER_IMAGE_URL or TOKEN are not set."
    echo "Please set these before running the script."
    exit 1
fi

# Determine GIT_REF from CI environment or fallback
if [ -z "$GIT_REF" ]; then
    if [ ! -z "$GITHUB_REF" ]; then
        GIT_REF=$GITHUB_REF  # GitHub Actions
    elif [ ! -z "$GIT_COMMIT" ]; then
        GIT_REF=$GIT_COMMIT  # Jenkins and other environments using GIT_COMMIT
    elif [ ! -z "$CI_COMMIT_REF_NAME" ]; then
        GIT_REF=$CI_COMMIT_REF_NAME  # GitLab CI
    else
        echo "GIT_REF is not set and could not be determined from the environment."
        exit 1
    fi
fi

# Path to facetsctl binary
BIN_PATH="$HOME/facetsctl/bin/facetsctl"

# Ensure facetsctl is executable
if [ ! -x "$BIN_PATH" ]; then
    echo "facetsctl is not installed or not executable. Please install facetsctl and try again."
    exit 1
fi

# Login using facetsctl
$BIN_PATH login -u "$USERNAME" -t "$TOKEN" -c "$CP_URL"

# Initialize artifact
$BIN_PATH artifact init -p "$PROJECT_NAME" -s "$SERVICE_NAME" -a "$ARTIFACTORY_NAME"

# Push artifact if required
if [ "$IS_PUSH" == "true" ]; then
    $BIN_PATH artifact push -d "$DOCKER_IMAGE_URL"
fi

# Register artifact, checking if RUN_ID is set
if [ -n "$RUN_ID" ]; then
    $BIN_PATH artifact register -t GIT_REF -v "$VERSION" -i "$DOCKER_IMAGE_URL" -r "$RUN_ID"
else
    $BIN_PATH artifact register -t GIT_REF -v "$VERSION" -i "$DOCKER_IMAGE_URL"
fi

echo "facetsctl operations completed."
