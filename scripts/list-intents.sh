#!/bin/bash

# Function to print usage
function print_usage() {
  echo "Usage: $0 -c <control_plane_url> -u <username> -t <token>"
  echo "-c: Control plane URL (e.g., example.com or https://example.com)"
  echo "-u: Username for Basic Auth"
  echo "-t: Token for Basic Auth"
  exit 1
}

# Parse command-line arguments
while getopts "c:u:t:" opt; do
  case $opt in
    c) url="$OPTARG" ;;
    u) username="$OPTARG" ;;
    t) token="$OPTARG" ;;
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
url="https://$url/cc-ui/v1/modules/intents" # Ensure https and proper endpoint

auth_string=$(echo -n "${username}:${token}" | base64 | tr -d '\n')

# Make the API call
response=$(curl -s -w "\n%{http_code}" -X GET "$url" \
  -H "Authorization: Basic ${auth_string}" \
  -H "Content-Type: application/json")

# Extract HTTP status code
http_code=$(tail -n 1 <<< "$response")
response_body=$(sed '$ d' <<< "$response")

# Check response status
if [[ "$http_code" == "200" ]]; then
  echo "API response:"
  echo "$response_body" | jq .
elif [[ "$http_code" =~ ^4|5 ]]; then
  message=$(jq -r .message <<< "$response_body" 2>/dev/null)
  if [[ -n "$message" && "$message" != "null" ]]; then
    echo "Error: $message (HTTP $http_code)"
  else
    echo "Error: API call failed with status code $http_code."
  fi
else
  echo "Error: API call failed with unexpected status code $http_code."
fi
