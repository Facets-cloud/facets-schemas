#!/bin/bash
set -e

# Function to print usage
function print_usage() {
  echo "Usage: $0 -c <control_plane_url> -u <username> -t <token> -i <intent> -f <flavor> -v <version>"
  echo "-c: Control plane URL (e.g., example.com or https://example.com)"
  echo "-u: Username for Basic Auth"
  echo "-t: Token for Basic Auth"
  echo "-i: Intent"
  echo "-f: Flavor"
  echo "-v: Version"
  exit 1
}

# Parse command-line arguments
while getopts "c:u:t:i:f:v:" opt; do
  case $opt in
    c) url="$OPTARG" ;;
    u) username="$OPTARG" ;;
    t) token="$OPTARG" ;;
    i) intent="$OPTARG" ;;
    f) flavor="$OPTARG" ;;
    v) version="$OPTARG" ;;
    *) print_usage ;;
  esac
done

# Validate inputs
if [[ -z "$url" || -z "$username" || -z "$token" || -z "$intent" || -z "$flavor" || -z "$version" ]]; then
  echo "Error: Missing required arguments."
  print_usage
fi

# Normalize URL
url=$(echo "$url" | sed 's#/$##') # Remove trailing slash if exists
url=$(echo "$url" | sed 's#^http://##; s#^https://##') # Remove http/https prefix if exists
url="https://$url/cc-ui/v1/modules/intent/$intent/flavor/$flavor/version/$version/mark-published" # Construct endpoint

# Send the request via POST
response=$(curl -w "\n%{http_code}" -o response_body.txt -s -X POST "$url" \
  -H "Authorization: Basic $(echo -n "$username:$token" | base64)")

# Extract HTTP status code
http_code=$(tail -n 1 <<< "$response")

# Check response status
if [[ "$http_code" == "200" ]]; then
  echo "Module marked as published successfully."
elif [[ "$http_code" =~ ^4|5 ]]; then
  message=$(jq -r .message response_body.txt 2>/dev/null)
  if [[ -n "$message" && "$message" != "null" ]]; then
    echo "Error: $message (HTTP $http_code)"
  else
    echo "Error: Operation failed with status code $http_code."
  fi
else
  echo "Error: Operation failed with unexpected status code $http_code."
fi

# Clean up response body
rm -f response_body.txt
