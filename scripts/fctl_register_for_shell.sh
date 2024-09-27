# Function to handle facetsctl operations
facetsctl_login() {
    printf "Logging in using facetsctl...\n"
    "$BIN_PATH" login -u "$USERNAME" -t "$TOKEN" -f "$CP_URL"
    printf "facetsctl login succeeded.\n"
}

facetsctl_artifact_init() {
    printf "Initializing artifact...\n"
    "$BIN_PATH" artifact init -p "$PROJECT_NAME" -s "$SERVICE_NAME" -a "$ARTIFACTORY_NAME"
    printf "facetsctl artifact init succeeded.\n"
}

facetsctl_artifact_push() {
    printf "Pushing artifact...\n"
    "$BIN_PATH" artifact push -d "$DOCKER_IMAGE_URL"
    printf "facetsctl artifact push succeeded.\n"
}

facetsctl_artifact_register() {
    printf "Registering artifact...\n"
    "$BIN_PATH" artifact register -t GIT_REF -v "$GIT_REF" -i "$DOCKER_IMAGE_URL" -r "$RUN_ID"
    printf "facetsctl artifact register succeeded.\n"
}

# Call the functions
facetsctl_login
facetsctl_artifact_init

if [ "$IS_PUSH" = "true" ]; then
    facetsctl_artifact_push
fi

facetsctl_artifact_register
