#!/bin/bash

# Default values for environment variables
DOCKER_IMAGE_URL=${DOCKER_IMAGE_URL:-}
TOKEN=${TOKEN:-}
GIT_REF=${GIT_REF:-}
RUN_ID=${RUN_ID:-}

# Parse command-line options
while getopts u:cpurl:p:s:a:push: flag
do
    case "${flag}" in
        u) USERNAME=${OPTARG};;
        c) CP_URL=${OPTARG};;
        p) PROJECT_NAME=${OPTARG};;
        s) SERVICE_NAME=${OPTARG};;
        a) ARTIFACTORY_NAME=${OPTARG};;
        i) IS_PUSH=${OPTARG};;
        *) echo "Invalid option: $flag" 1>&2; exit 1;;
    esac
done

# Ensure all critical environment variables are set
if [[ -z "$DOCKER_IMAGE_URL" || -z "$TOKEN" ]]; then
    echo "Critical environment variables DOCKER_IMAGE_URL or TOKEN are not set."
    echo "Please set these before running the script."
    exit 1
fi

# Determine GIT_REF based on CI/CD environment variables if not set
if [ -z "$GIT_REF" ]; then
    if [ ! -z "$GITHUB_REF" ]; then
        # Extract the last part after '/'
        GIT_REF=${GITHUB_REF##*/}
    elif [ ! -z "$GIT_COMMIT" ]; then
        GIT_REF=$GIT_COMMIT  # Common in Jenkins; usually this is a commit SHA
    elif [ ! -z "$CI_COMMIT_REF_NAME" ]; then
        GIT_REF=$CI_COMMIT_REF_NAME  # GitLab CI; usually just the branch or tag name
    elif [ ! -z "$BITBUCKET_COMMIT" ]; then
        GIT_REF=$BITBUCKET_COMMIT  # Bitbucket Pipelines; usually this is a commit SHA
    elif [ ! -z "$CODEBUILD_RESOLVED_SOURCE_VERSION" ]; then
        GIT_REF=$CODEBUILD_RESOLVED_SOURCE_VERSION  # AWS CodeBuild; usually this is a commit SHA
    else
        echo "GIT_REF is not set and could not be determined from the environment."
        exit 1
    fi
fi

# Determine RUN_ID based on CI/CD environment variables if not set
if [ -z "$RUN_ID" ]; then
    if [ ! -z "$GITHUB_RUN_ID" ]; then
        RUN_ID=$GITHUB_RUN_ID
    elif [ ! -z "$BUILD_ID" ]; then
        RUN_ID=$BUILD_ID  # Common in Jenkins
    elif [ ! -z "$CI_PIPELINE_ID" ]; then
        RUN_ID=$CI_PIPELINE_ID  # GitLab CI
    elif [ ! -z "$BITBUCKET_BUILD_NUMBER" ]; then
        RUN_ID=$BITBUCKET_BUILD_NUMBER  # Bitbucket Pipelines
    elif [ ! -z "$CODEBUILD_BUILD_ID" ]; then
        RUN_ID=$CODEBUILD_BUILD_ID  # AWS CodeBuild
    fi
fi

# Path to facetsctl binary
BIN_PATH="facetsctl"

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

# Register artifact
$BIN_PATH artifact register -t "$GIT_REF" -v "$VERSION" -i "$DOCKER_IMAGE_URL" -r "$RUN_ID"

echo "facetsctl operations completed."
