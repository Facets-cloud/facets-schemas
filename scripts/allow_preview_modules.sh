#!/bin/bash
set -e

# Function to print usage
function print_usage() {
  echo "Usage: $0 -c <control_plane_url> -u <username> -t <token> -p <project_name> -a <allow_preview_modules>"
  echo "-c: Control plane URL (e.g., example.com or https://example.com)"
  echo "-u: Username for Basic Auth"
  echo "-t: Token for Basic Auth"
  echo "-p: Project name (stack name)"
  echo "-a: Allow preview modules (true/false)"
  exit 1
}

# Parse command-line arguments
while getopts "c:u:t:p:a:" opt; do
  case $opt in
    c) url="$OPTARG" ;;
    u) username="$OPTARG" ;;
    t) token="$OPTARG" ;;
    p) project_name="$OPTARG" ;;
    a) allow_preview_modules="$OPTARG" ;;
    *) print_usage ;;
  esac
done

# Validate inputs
if [[ -z "$url" || -z "$username" || -z "$token" || -z "$project_name" || -z "$allow_preview_modules" ]]; then
  echo "Error: Missing required arguments."
  print_usage
fi

# Normalize URL
url=$(echo "$url" | sed 's#/$##') # Remove trailing slash if exists
url=$(echo "$url" | sed 's#^http://##; s#^https://##') # Remove http/https prefix if exists

# Get stack details
get_url="https://$url/cc-ui/v1/stacks/$project_name"
response=$(curl -w "\n%{http_code}" -o stack.json -s -X GET "$get_url" \
  -H "Authorization: Basic $(echo -n "$username:$token" | base64)")

# Extract HTTP status code
http_code=$(tail -n 1 <<< "$response")

if [[ "$http_code" == "404" ]]; then
  echo "Error: Project not found with name $project_name."
  rm -f stack.json
  exit 1
elif [[ "$http_code" != "200" ]]; then
  echo "Error: Failed to fetch stack details with status code $http_code."
  cat stack.json 2>/dev/null
  rm -f stack.json
  exit 1
fi

# Update the previewModulesAllowed field
if ! jq --argjson allow "$allow_preview_modules" '.previewModulesAllowed = $allow' stack.json > updated_stack.json; then
  echo "Error: Failed to update stack JSON."
  rm -f stack.json
  exit 1
fi

# Send the updated stack JSON to the update API
put_url="https://$url/cc-ui/v1/stacks/$project_name"
response=$(curl -w "\n%{http_code}" -o response_body.txt -s -X PUT "$put_url" \
  -H "Authorization: Basic $(echo -n "$username:$token" | base64)" \
  -H "Content-Type: application/json" \
  -d @updated_stack.json)

# Extract HTTP status code
http_code=$(tail -n 1 <<< "$response")

if [[ "$http_code" == "200" ]]; then
  echo "Stack updated successfully."
elif [[ "$http_code" =~ ^4|5 ]]; then
  message=$(jq -r .message response_body.txt 2>/dev/null)
  if [[ -n "$message" && "$message" != "null" ]]; then
    echo "Error: $message (HTTP $http_code)"
  else
    echo "Error: Update failed with status code $http_code."
  fi
else
  echo "Error: Update failed with unexpected status code $http_code."
fi

# Clean up temporary files
rm -f stack.json updated_stack.json response_body.txt
