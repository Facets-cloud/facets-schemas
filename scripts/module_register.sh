#!/bin/bash

# Function to print usage
function print_usage() {
  echo "Usage: $0 -c <control_plane_url> -u <username> -t <token> [-p <path>] [-g <git_url>] [-r <git_ref>] [-f] [-a]"
  echo "-c: Control plane URL (e.g., example.com or https://example.com)"
  echo "-u: Username for Basic Auth"
  echo "-t: Token for Basic Auth"
  echo "-p: Optional path to the folder to zip (default: current directory)"
  echo "-g: Optional Git URL"
  echo "-r: Optional Git reference (branch, tag, or commit)"
  echo "-f: Optional flag to mark as feature branch (cannot be published)"
  echo "-a: Optional flag to automatically create intent and output"
  exit 1
}

# Function to clean up temporary files
function cleanup() {
  echo "Cleaning up temporary files..."
  [[ -f "$path/$zip_file" ]] && rm -f "$path/$zip_file"
  [[ -f "response_body.txt" ]] && rm -f "response_body.txt"
  [[ -n "$git_info_file" && -f "$git_info_file" ]] && rm -f "$git_info_file"
}

# Set up trap to ensure cleanup on script exit, including errors
trap cleanup EXIT

# Parse command-line arguments
path="$(pwd)" # Default to the current directory
is_feature_branch=false # Default to false
auto_create=false # Default to false

while getopts "c:u:t:p:g:r:fa" opt; do
  case $opt in
    c) url="$OPTARG" ;;
    u) username="$OPTARG" ;;
    t) token="$OPTARG" ;;
    p) path="$OPTARG" ;;
    g) git_url="$OPTARG" ;;
    r) git_ref="$OPTARG" ;;
    f) is_feature_branch=true ;;
    a) auto_create=true ;;
    *) print_usage ;;
  esac
done

# Validate inputs
if [[ -z "$url" || -z "$username" || -z "$token" ]]; then
  echo "❌ Error: Missing required arguments."
  print_usage
fi

# Normalize URL
url=$(echo "$url" | sed 's#/$##') # Remove trailing slash if exists
url=$(echo "$url" | sed 's#^http://##; s#^https://##') # Remove http/https prefix if exists
url="https://$url/cc-ui/v1/modules/upload" # Ensure https and proper endpoint

# Validate the path
if [[ ! -d "$path" ]]; then
  echo "❌ Error: Specified path '$path' does not exist or is not a directory."
  exit 1
fi

# Validate presence of facets.yaml in the specified path
if [[ ! -f "$path/facets.yaml" ]]; then
  echo "❌ Error: facets.yaml file not found in the specified path '$path'."
  exit 1
fi

# if **/.terraform and .terraform.lock.hcl exists, remove them
find "$path" -type d -name .terraform -exec rm -rf {} +
find "$path" -type f -name .terraform.lock.hcl -exec rm -f {} +

# Create a zip file of the specified folder
zip_file="$(basename "$path").zip"
(cd "$path" && zip -r "$zip_file" . > /dev/null)
if [[ $? -ne 0 ]]; then
  echo "❌ Error: Failed to create zip file."
  exit 1
fi

auth_string=$(echo -n "${username}:${token}" | base64 | tr -d '\n')

# Prepare git info JSON if any git-related parameters are provided or is_feature_branch is true
git_info_file=""
if [[ -n "$git_url" || -n "$git_ref" || "$is_feature_branch" == true || "$auto_create" == true ]]; then
  git_info="{}"
  [[ -n "$git_url" ]] && git_info=$(echo "$git_info" | jq --arg v "$git_url" '. + {gitUrl: $v}')
  [[ -n "$git_ref" ]] && git_info=$(echo "$git_info" | jq --arg v "$git_ref" '. + {gitRef: $v}')
  # Include both isFeatureBranch and autoCreate in the JSON
  git_info=$(echo "$git_info" | jq --arg v "$is_feature_branch" '. + {featureBranch: ($v == "true")}')
  git_info=$(echo "$git_info" | jq --arg v "$auto_create" '. + {autoCreate: ($v == "true")}')
  
  # Create a temporary file for the git info
  git_info_file=$(mktemp)
  echo "$git_info" > "$git_info_file"
  
  # Send the request with both file and git info
  response=$(curl -w "\n%{http_code}" -o response_body.txt -s -X POST "$url" \
    -H "Authorization: Basic ${auth_string}" \
    -F "file=@$path/$zip_file" \
    -F "metadata=@$git_info_file;type=application/json")
else
  # Send the request with file only
  response=$(curl -w "\n%{http_code}" -o response_body.txt -s -X POST "$url" \
    -H "Authorization: Basic ${auth_string}" \
    -F "file=@$path/$zip_file")
fi

# Extract HTTP status code
http_code=$(tail -n 1 <<< "$response")

# Check response status
if [[ "$http_code" == "200" ]]; then
  echo "Module registered successfully."
elif [[ "$http_code" =~ ^4|5 ]]; then
  message=$(jq -r .message response_body.txt 2>/dev/null)
  if [[ -n "$message" && "$message" != "null" ]]; then
    echo "❌ Error: $message (HTTP $http_code)"
    exit 1
  else
    echo "❌ Error: File upload failed with status code $http_code."
    exit 1
  fi
else
  echo "❌ Error: File upload failed with unexpected status code $http_code."
  exit 1
fi

# Note: cleanup function will be called automatically via the EXIT trap
exit 0
