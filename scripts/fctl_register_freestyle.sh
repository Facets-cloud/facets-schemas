#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

echo "Starting script execution..."

# Parse command-line options
while getopts u:c:p:s:a:i:n: flag
do
    case "${flag}" in
        u) USERNAME=${OPTARG};;
        c) CP_URL=${OPTARG};;
        p) PROJECT_NAME=${OPTARG};;
        s) SERVICE_NAME=${OPTARG};;
        a) ARTIFACTORY_NAME=${OPTARG};;
        i) IS_PUSH=${OPTARG};;
        n) CI_NAME=${OPTARG};;
        *) echo "Invalid option: -${OPTARG}" 1>&2; exit 1;;
    esac
done

# Ensure all critical environment variables are set
if [[ -z "$FILE_PATH" || -z "$TOKEN" ]]; then
    echo "Critical environment variables FILE_PATH or TOKEN are not set."
    echo "Please set these before running the script."
    exit 1
fi

# Ensure either SERVICE_NAME or CI_NAME is provided, but not both
if [[ -n "$SERVICE_NAME" && -n "$CI_NAME" ]]; then
    echo "Both SERVICE_NAME and CI_NAME are provided. Please provide only one."
    exit 1
elif [[ -z "$SERVICE_NAME" && -z "$CI_NAME" ]]; then
    echo "Neither SERVICE_NAME nor CI_NAME is provided. Please provide one."
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

# Print determined GIT_REF
echo "Determined GIT_REF: $GIT_REF"

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

# Print determined RUN_ID
echo "Determined RUN_ID: $RUN_ID"

# Path to facetsctl binary
# Attempt to find facetsctl using which
BIN_PATH="$(which facetsctl || echo $HOME/facetsctl/bin/facetsctl)"
echo "Bin path: $BIN_PATH"

# Ensure facetsctl is executable
if [ ! -x "$BIN_PATH" ]; then
    echo "facetsctl is not installed or not executable. Please install facetsctl and try again."
    exit 1
fi

# Print all variable values
echo "Final Variable Values:"
echo "Username: $USERNAME"
echo "CP_URL: $CP_URL"
echo "Project Name: $PROJECT_NAME"
echo "Service Name: $SERVICE_NAME"
echo "CI Name: $CI_NAME"
echo "Artifactory Name: $ARTIFACTORY_NAME"
echo "Is Push: $IS_PUSH"
echo "File Path: $FILE_PATH"
echo "Token: $TOKEN"
echo "GIT_REF: $GIT_REF"
echo "RUN_ID: $RUN_ID"
echo "Bin Path: $BIN_PATH"

# Login using facetsctl
echo "Logging in using facetsctl..."
$BIN_PATH login -u "$USERNAME" -t "$TOKEN" -f "$CP_URL"
if [ $? -ne 0 ]; then
    echo "facetsctl login failed."
    exit 1
fi
echo "facetsctl login succeeded."

# Initialize artifact
echo "Initializing artifact..."
if [ -n "$SERVICE_NAME" ]; then
    $BIN_PATH artifact init -p "$PROJECT_NAME" -s "$SERVICE_NAME" -a "$ARTIFACTORY_NAME"
else
    $BIN_PATH artifact init -p "$PROJECT_NAME" -c "$CI_NAME" -a "$ARTIFACTORY_NAME"
fi
if [ $? -ne 0 ]; then
    echo "facetsctl artifact init failed."
    exit 1
fi
echo "facetsctl artifact init succeeded."

# Register artifact
echo "Registering artifact..."
$BIN_PATH artifact upload -t GIT_REF -v "$GIT_REF" -f "$FILE_PATH" -r "$RUN_ID"
if [ $? -ne 0 ]; then
    echo "facetsctl artifact register failed."
    exit 1
fi
echo "facetsctl artifact register succeeded."

echo "facetsctl operations completed."
