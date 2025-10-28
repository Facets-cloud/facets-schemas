#!/bin/sh

# Exit immediately if a command exits with a non-zero status.
set -e

# Function to print messages to stdout
log() {
    printf "%s\n" "$1"
}

# Function to print error messages to stderr
error() {
    printf "Error: %s\n" "$1" >&2
}

# Function to login using facetsctl
facetsctl_login() {
    log "Logging in using facetsctl..."
    "$BIN_PATH" login -u "$USERNAME" -t "$TOKEN" -f "$CP_URL"
    log "facetsctl login succeeded."
}

# Function to initialize artifact
facetsctl_artifact_init() {
    log "Initializing artifact..."
    if [ -n "$SERVICE_NAME" ]; then
        "$BIN_PATH" artifact init -p "$PROJECT_NAME" -s "$SERVICE_NAME" -a "$ARTIFACTORY_NAME"
    else
        "$BIN_PATH" artifact init -p "$PROJECT_NAME" -c "$CI_NAME" -a "$ARTIFACTORY_NAME"
    fi
    log "facetsctl artifact init succeeded."
}

# Function to push artifact
facetsctl_artifact_push() {
    log "Pushing artifact..."
    "$BIN_PATH" artifact push -d "$DOCKER_IMAGE_URL"
    log "facetsctl artifact push succeeded."
}

# Function to register artifact
facetsctl_artifact_register() {
    log "Registering artifact..."
    "$BIN_PATH" artifact register -t GIT_REF -v "$GIT_REF" -i "$DOCKER_IMAGE_URL" -r "$RUN_ID"
    log "facetsctl artifact register succeeded."
}

# Print initial script information
log "Starting script execution..."

# Initialize RUN_ID if not set
RUN_ID=${RUN_ID:-}

# Print initial environment variable values
log "Initial Environment Variables:"
log "DOCKER_IMAGE_URL: $DOCKER_IMAGE_URL"
log "TOKEN: $TOKEN"
log "RUN_ID: $RUN_ID"

# Parse command-line options
while getopts "u:c:p:s:a:i:n:" flag; do
    case "$flag" in
        u) USERNAME="$OPTARG" ;;
        c) CP_URL="$OPTARG" ;;
        p) PROJECT_NAME="$OPTARG" ;;
        s) SERVICE_NAME="$OPTARG" ;;
        a) ARTIFACTORY_NAME="$OPTARG" ;;
        i) IS_PUSH="$OPTARG" ;;
        n) CI_NAME="$OPTARG" ;;
        *)
            error "Invalid option: -$OPTARG"
            exit 1
            ;;
    esac
done

# Print parsed command-line options
log "Parsed Command-Line Options:"
log "USERNAME: $USERNAME"
log "CP_URL: $CP_URL"
log "PROJECT_NAME: $PROJECT_NAME"
log "SERVICE_NAME: $SERVICE_NAME"
log "CI_NAME: $CI_NAME"
log "ARTIFACTORY_NAME: $ARTIFACTORY_NAME"
log "IS_PUSH: $IS_PUSH"

# Ensure all critical environment variables are set
if [ -z "$DOCKER_IMAGE_URL" ] || [ -z "$TOKEN" ]; then
    error "Critical environment variables DOCKER_IMAGE_URL or TOKEN are not set."
    error "Please set these before running the script."
    exit 1
fi

# Ensure either SERVICE_NAME or CI_NAME is provided, but not both
if [ -n "$SERVICE_NAME" ] && [ -n "$CI_NAME" ]; then
    error "Both SERVICE_NAME and CI_NAME are provided. Please provide only one."
    exit 1
elif [ -z "$SERVICE_NAME" ] && [ -z "$CI_NAME" ]; then
    error "Neither SERVICE_NAME nor CI_NAME is provided. Please provide one."
    exit 1
fi

# Determine GIT_REF based on CI/CD environment variables if not set
if [ -z "$GIT_REF" ]; then
    if [ -n "$GITHUB_REF" ]; then
        # Extract the last part after '/'
        GIT_REF="${GITHUB_REF##*/}"
    elif [ -n "$GIT_COMMIT" ]; then
        GIT_REF="$GIT_COMMIT"  # Common in Jenkins; usually this is a commit SHA
    elif [ -n "$CI_COMMIT_REF_NAME" ]; then
        GIT_REF="$CI_COMMIT_REF_NAME"  # GitLab CI; usually just the branch or tag name
    elif [ -n "$BITBUCKET_COMMIT" ]; then
        GIT_REF="$BITBUCKET_COMMIT"  # Bitbucket Pipelines; usually this is a commit SHA
    elif [ -n "$CODEBUILD_RESOLVED_SOURCE_VERSION" ]; then
        GIT_REF="$CODEBUILD_RESOLVED_SOURCE_VERSION"  # AWS CodeBuild; usually this is a commit SHA
    else
        error "GIT_REF is not set and could not be determined from the environment."
        exit 1
    fi
fi

# Print determined GIT_REF
log "Determined GIT_REF: $GIT_REF"

# Determine RUN_ID based on CI/CD environment variables if not set
if [ -z "$RUN_ID" ]; then
    if [ -n "$GITHUB_RUN_ID" ]; then
        RUN_ID="$GITHUB_RUN_ID"
    elif [ -n "$BUILD_ID" ]; then
        RUN_ID="$BUILD_ID"  # Common in Jenkins
    elif [ -n "$CI_PIPELINE_ID" ]; then
        RUN_ID="$CI_PIPELINE_ID"  # GitLab CI
    elif [ -n "$BITBUCKET_BUILD_NUMBER" ]; then
        RUN_ID="$BITBUCKET_BUILD_NUMBER"  # Bitbucket Pipelines
    elif [ -n "$CODEBUILD_BUILD_ID" ]; then
        RUN_ID="$CODEBUILD_BUILD_ID"  # AWS CodeBuild
    fi
fi

# Print determined RUN_ID
log "Determined RUN_ID: $RUN_ID"

# Path to facetsctl binary
# Attempt to find facetsctl using command -v, fallback to default path
BIN_PATH=$(command -v facetsctl 2>/dev/null || printf "%s/facetsctl/bin/facetsctl" "$HOME")
log "Bin path: $BIN_PATH"

# Ensure facetsctl is executable
if [ ! -x "$BIN_PATH" ]; then
    error "facetsctl is not installed or not executable. Please install facetsctl and try again."
    exit 1
fi

# Print all variable values
log "Final Variable Values:"
log "Username: $USERNAME"
log "CP_URL: $CP_URL"
log "Project Name: $PROJECT_NAME"
log "Service Name: $SERVICE_NAME"
log "CI Name: $CI_NAME"
log "Artifactory Name: $ARTIFACTORY_NAME"
log "Is Push: $IS_PUSH"
log "Docker Image URL: $DOCKER_IMAGE_URL"
log "Token: $TOKEN"
log "GIT_REF: $GIT_REF"
log "RUN_ID: $RUN_ID"
log "Bin Path: $BIN_PATH"

# Execute facetsctl operations
facetsctl_login
facetsctl_artifact_init

if [ "$IS_PUSH" = "true" ]; then
    facetsctl_artifact_push
fi

facetsctl_artifact_register

log "facetsctl operations completed."
