#!/bin/bash
set -e

# Function to print usage
function print_usage() {
  echo "Usage: $0 -c <control_plane_url> -u <username> -t <token> [-p <path>]"
  echo "-c: Control plane URL (e.g., example.com or https://example.com)"
  echo "-u: Username for Basic Auth"
  echo "-t: Token for Basic Auth"
  echo "-p: Optional path to the folder to zip (default: current directory)"
  exit 1
}

# Parse command-line arguments
path="$(pwd)" # Default to the current directory
while getopts "c:u:t:p:" opt; do
  case $opt in
    c) url="$OPTARG" ;;
    u) username="$OPTARG" ;;
    t) token="$OPTARG" ;;
    p) path="$OPTARG" ;;
    *) print_usage ;;
  esac
done

# Validate inputs
if [[ -z "$url" || -z "$username" || -z "$token" ]]; then
  echo "Error: Missing required arguments."
  print_usage
fi

# Normalize URL
url=$(echo "$url" | sed 's#/$##') # Remove trailing slash if exists
url=$(echo "$url" | sed 's#^http://##; s#^https://##') # Remove http/https prefix if exists
url="https://$url/cc-ui/v1/modules/upload" # Ensure https and proper endpoint

# Validate the path
if [[ ! -d "$path" ]]; then
  echo "Error: Specified path '$path' does not exist or is not a directory."
  exit 1
fi

# Validate presence of facets.yaml in the specified path
if [[ ! -f "$path/facets.yaml" ]]; then
  echo "Error: facets.yaml file not found in the specified path '$path'."
  exit 1
fi

# Create a zip file of the specified folder
zip_file="$(basename "$path").zip"
(cd "$path" && zip -r "$zip_file" . > /dev/null)

if [[ $? -ne 0 ]]; then
  echo "Error: Failed to create zip file."
  exit 1
fi
auth_string=$(echo -n "${username}:${token}" | base64 | tr -d '\n')

# Send the zip file via POST request
response=$(curl -w "\n%{http_code}" -o response_body.txt -s -X POST "$url" \
  -H "Authorization: Basic ${auth_string}" \
  -F "file=@$path/$zip_file")

# Extract HTTP status code
http_code=$(tail -n 1 <<< "$response")

# Check response status
if [[ "$http_code" == "200" ]]; then
  echo "Module registered successfully."
elif [[ "$http_code" =~ ^4|5 ]]; then
  message=$(jq -r .message response_body.txt 2>/dev/null)
  if [[ -n "$message" && "$message" != "null" ]]; then
    echo "Error: $message (HTTP $http_code)"
    exit 1
  else
    echo "Error: File upload failed with status code $http_code."
    exit 1
  fi
else
  echo "Error: File upload failed with unexpected status code $http_code."
  exit 1
fi

# Clean up zip file and response body
rm -f "$path/$zip_file" response_body.txt
