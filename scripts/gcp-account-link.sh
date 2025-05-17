#!/bin/bash

# Check if CP_URL, PRINCIPAL_NAME, and WEBHOOK_ID were provided as arguments
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <CP_URL> <PRINCIPAL_NAME> <WEBHOOK_ID>"
    exit 1
fi

CP_URL=$1
ACCOUNT_NAME="$2"
# Concatenate "facets-" with the provided principal name
PRINCIPAL_NAME="facets-$2"
ROLE_NAME=$(echo $PRINCIPAL_NAME | tr '-' '_')

WEBHOOK_ID=$3

# Fetch all projects in JSON format
echo "Fetching available projects..."
PROJECTS_JSON=$(gcloud projects list --format=json)

if [ $? -ne 0 ]; then
    echo "Failed to fetch projects. Ensure you're logged in and try again."
    exit 1
fi

# Convert JSON to "CSV"
PROJECTS=$(echo "$PROJECTS_JSON" | jq -r '.[] | "\(.projectId):\(.projectId)"')

if [ -z "$PROJECTS" ]; then
    echo "No projects are available."
    exit 1
fi

# Display projects and prompt for choice
echo "Please select a project by number:"
IFS=$'\n' # Ensure we split lines on newlines
select PROJECT_LINE in $PROJECTS; do
    PROJECT_ID=$(echo $PROJECT_LINE | cut -d":" -f2)
    if [ -n "$PROJECT_ID" ]; then
        echo "You have selected project '$PROJECT_ID'"
        break
    else
        echo "Invalid selection. Please select a number from the list."
    fi
done

# Set the project context
gcloud config set project "$PROJECT_ID" &>/dev/null

if [ $? -ne 0 ]; then
    echo "Failed to set the project context."
    exit 1
fi

##### Enabling APIs before creating service account #######

# List of APIs to enable
apis=(
  "alloydb.googleapis.com" "analyticshub.googleapis.com" "artifactregistry.googleapis.com" "autoscaling.googleapis.com" 
  "bigquery.googleapis.com" "bigqueryconnection.googleapis.com" "bigquerydatapolicy.googleapis.com" "bigquerymigration.googleapis.com" 
  "bigqueryreservation.googleapis.com" "bigquerystorage.googleapis.com" "certificatemanager.googleapis.com" "cloudapis.googleapis.com" 
  "cloudkms.googleapis.com" "cloudresourcemanager.googleapis.com" "cloudtrace.googleapis.com" "compute.googleapis.com" "container.googleapis.com" 
  "containerfilesystem.googleapis.com" "containerregistry.googleapis.com" "dataform.googleapis.com" "dataplex.googleapis.com" 
  "datastore.googleapis.com" "deploymentmanager.googleapis.com" "dns.googleapis.com" "gkebackup.googleapis.com" "iam.googleapis.com" 
  "iamcredentials.googleapis.com" "logging.googleapis.com" "monitoring.googleapis.com" "networkconnectivity.googleapis.com" "oslogin.googleapis.com" 
  "pubsub.googleapis.com" "redis.googleapis.com" "servicemanagement.googleapis.com" "servicenetworking.googleapis.com" "serviceusage.googleapis.com" 
  "sql-component.googleapis.com" "sqladmin.googleapis.com" "storage-api.googleapis.com" "storage-component.googleapis.com" "storage.googleapis.com"
)

# Get the list of enabled APIs
echo "Fetching the list of enabled APIs..."
enabled_apis=$(gcloud services list --enabled --format="value(config.name)")

# Function to enable an API
enable_api() {
  local api_name=$1
  echo "Enabling API: ${api_name}"
  gcloud services enable "${api_name}"
}

# Iterate over the APIs and enable them if not already enabled
for api in "${apis[@]}"; do
  if echo "${enabled_apis}" | grep -q "${api}"; then
    echo "API : ${api} is already enabled"
  else
    enable_api "${api}" &
  fi
done

# Wait for all background jobs to complete
wait

echo "All specified APIs have been enabled."

# Check if the service account already exists
SA_EMAIL="$PRINCIPAL_NAME@$PROJECT_ID.iam.gserviceaccount.com"
EXISTING_SA=$(gcloud iam service-accounts list --filter="email:$SA_EMAIL" --format="value(email)")

if [ -z "$EXISTING_SA" ]; then
    # Create the Service Account with Owner role
    gcloud iam service-accounts create "$PRINCIPAL_NAME" --display-name="$PRINCIPAL_NAME"

    if [ $? -ne 0 ]; then
        echo "Failed to create Service Account."
        exit 1
    fi

    # Wait for a short period to ensure the service account is fully created
    sleep 10
else
    echo "Service Account $SA_EMAIL already exists."
fi

# Generate key for the Service Account
gcloud iam service-accounts keys create "$PRINCIPAL_NAME-key.json" --iam-account="$SA_EMAIL"

if [ $? -ne 0 ]; then
    echo "Failed to generate key for Service Account."
    exit 1
fi

echo "Service Account key generated successfully."
echo "Key file saved as $PRINCIPAL_NAME-key.json"

PERMISSIONS=(
  "alloydb.clusters.create"
  "alloydb.clusters.delete"
  "alloydb.clusters.get"
  "alloydb.clusters.update"
  "alloydb.instances.create"
  "alloydb.instances.delete"
  "alloydb.instances.get"
  "alloydb.instances.update"
  "alloydb.operations.get"
  "cloudkms.cryptoKeyVersions.destroy"
  "cloudkms.cryptoKeyVersions.list"
  "cloudkms.cryptoKeys.create"
  "cloudkms.cryptoKeys.get"
  "cloudkms.cryptoKeys.getIamPolicy"
  "cloudkms.cryptoKeys.setIamPolicy"
  "cloudkms.cryptoKeys.update"
  "cloudkms.keyRings.create"
  "cloudkms.keyRings.get"
  "cloudsql.databases.create"
  "cloudsql.databases.delete"
  "cloudsql.databases.get"
  "cloudsql.instances.create"
  "cloudsql.instances.delete"
  "cloudsql.instances.get"
  "cloudsql.instances.list"
  "cloudsql.instances.update"
  "cloudsql.users.create"
  "cloudsql.users.delete"
  "cloudsql.users.list"
  "cloudsql.users.update"
  "compute.addresses.create"
  "compute.addresses.delete"
  "compute.addresses.get"
  "compute.disks.delete"
  "compute.disks.list"
  "compute.firewalls.create"
  "compute.firewalls.delete"
  "compute.firewalls.get"
  "compute.forwardingRules.create"
  "compute.forwardingRules.delete"
  "compute.forwardingRules.get"
  "compute.forwardingRules.setLabels"
  "compute.globalAddresses.createInternal"
  "compute.globalAddresses.deleteInternal"
  "compute.globalAddresses.get"
  "compute.globalOperations.get"
  "compute.healthChecks.create"
  "compute.healthChecks.delete"
  "compute.healthChecks.get"
  "compute.healthChecks.useReadOnly"
  "compute.instanceGroupManagers.create"
  "compute.instanceGroupManagers.delete"
  "compute.instanceGroupManagers.get"
  "compute.instanceGroups.delete"
  "compute.instanceGroups.use"
  "compute.instanceTemplates.create"
  "compute.instanceTemplates.delete"
  "compute.instanceTemplates.get"
  "compute.instances.list"
  "compute.networks.create"
  "compute.networks.delete"
  "compute.networks.get"
  "compute.networks.removePeering"
  "compute.networks.updatePolicy"
  "compute.networks.use"
  "compute.regionBackendServices.create"
  "compute.regionBackendServices.delete"
  "compute.regionBackendServices.get"
  "compute.regionBackendServices.use"
  "compute.regionOperations.get"
  "compute.routers.create"
  "compute.routers.delete"
  "compute.routers.get"
  "compute.routers.update"
  "compute.sslPolicies.create"
  "compute.sslPolicies.delete"
  "compute.sslPolicies.get"
  "compute.sslPolicies.update"
  "compute.subnetworks.create"
  "compute.subnetworks.delete"
  "compute.subnetworks.get"
  "compute.subnetworks.use"
  "compute.zoneOperations.get"
  "compute.zones.list"
  "container.clusterRoleBindings.create"
  "container.clusterRoleBindings.delete"
  "container.clusterRoleBindings.get"
  "container.clusterRoles.bind"
  "container.clusterRoles.create"
  "container.clusterRoles.escalate"
  "container.clusterRoles.get"
  "container.clusters.create"
  "container.clusters.delete"
  "container.clusters.get"
  "container.clusters.getCredentials"
  "container.clusters.list"
  "container.clusters.update"
  "container.configMaps.create"
  "container.configMaps.get"
  "container.cronJobs.create"
  "container.cronJobs.delete"
  "container.cronJobs.get"
  "container.deployments.create"
  "container.deployments.get"
  "container.deployments.update"
  "container.namespaces.create"
  "container.namespaces.delete"
  "container.namespaces.get"
  "container.operations.get"
  "container.priorityClasses.create"
  "container.priorityClasses.delete"
  "container.priorityClasses.get"
  "container.replicaSets.list"
  "container.roleBindings.create"
  "container.roleBindings.delete"
  "container.roleBindings.get"
  "container.roles.bind"
  "container.roles.create"
  "container.roles.delete"
  "container.roles.escalate"
  "container.roles.get"
  "container.secrets.create"
  "container.secrets.delete"
  "container.secrets.get"
  "container.secrets.list"
  "container.secrets.update"
  "container.serviceAccounts.create"
  "container.serviceAccounts.delete"
  "container.serviceAccounts.get"
  "container.storageClasses.create"
  "container.storageClasses.delete"
  "container.storageClasses.get"
  "dns.changes.create"
  "dns.managedZones.list"
  "dns.resourceRecordSets.create"
  "dns.resourceRecordSets.delete"
  "dns.resourceRecordSets.list"
  "iam.roles.create"
  "iam.roles.delete"
  "iam.roles.get"
  "iam.roles.list"
  "iam.serviceAccounts.actAs"
  "iam.serviceAccounts.create"
  "iam.serviceAccounts.delete"
  "iam.serviceAccounts.get"
  "iam.serviceAccounts.getIamPolicy"
  "iam.serviceAccounts.list"
  "iam.serviceAccounts.setIamPolicy"
  "monitoring.metricDescriptors.list"
  "monitoring.timeSeries.list"
  "redis.instances.create"
  "redis.instances.delete"
  "redis.instances.get"
  "redis.instances.getAuthString"
  "redis.instances.list"
  "redis.instances.update"
  "redis.instances.updateAuth"
  "redis.operations.get"
  "resourcemanager.projects.get"
  "resourcemanager.projects.getIamPolicy"
  "resourcemanager.projects.setIamPolicy"
  "servicenetworking.operations.get"
  "servicenetworking.services.addPeering"
  "servicenetworking.services.get"
  "storage.buckets.create"
  "storage.buckets.delete"
  "storage.buckets.get"
  "storage.buckets.getIamPolicy"
  "storage.buckets.list"
  "storage.buckets.setIamPolicy"
  "storage.buckets.update"
  "storage.objects.delete"
  "storage.objects.get"
  "storage.objects.list"
)

# Convert permissions array to comma-separated string
PERMISSIONS_STRING=$(IFS=,; echo "${PERMISSIONS[*]}")

# Create the IAM role
gcloud iam roles create $ROLE_NAME \
  --project $PROJECT_ID \
  --title "$ROLE_NAME" \
  --description "$ROLE_NAME" \
  --permissions $PERMISSIONS_STRING \
  --stage "GA" \
  --quiet

gcloud projects add-iam-policy-binding "$PROJECT_ID" --member="serviceAccount:$SA_EMAIL" --role="projects/$PROJECT_ID/roles/$ROLE_NAME"

# Base64 encode the service account key JSON
SERVICE_ACCOUNT_KEY_BASE64=$(base64 -w 0 "$PRINCIPAL_NAME-key.json")

# Prepare the curl request with base64 encoded key
CURL_DATA="{ \"payload\": { \"name\": \"$ACCOUNT_NAME\", \"serviceAccountKey\": \"$SERVICE_ACCOUNT_KEY_BASE64\", \"project\": \"$PROJECT_ID\" }, \"webhookId\": \"$WEBHOOK_ID\"}"
CURL_RESPONSE=$(curl -k -s -o /dev/null -w "%{http_code}" -X POST "https://$CP_URL/public/v1/link-gcp" -H "accept: */*" -H "Content-Type: application/json; charset=utf-8" -d "$CURL_DATA")

if [ "$CURL_RESPONSE" -ne 200 ]; then
    echo "Failed to send data to the specified URL. HTTP response code: $CURL_RESPONSE"
    exit 1
fi

echo "Data successfully sent to the specified URL."
