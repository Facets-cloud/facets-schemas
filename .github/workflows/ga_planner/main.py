import aiohttp
import asyncio
import base64
import json
import boto3
import os
from botocore.exceptions import ClientError, NoCredentialsError
from rich.console import Console
from rich.progress import Progress, SpinnerColumn, TextColumn
from datetime import datetime, timedelta
import pytz
import subprocess
import re
# Import our new modules
from filter import load_and_filter_plan
from ai_integration import OpenAIAnalyzer


TIME_ZONE = pytz.timezone("Asia/Kolkata")
RC_BRANCH = os.getenv("RC_BRANCH")
PLAN_BUCKET = os.getenv("PLAN_BUCKET", "rc-branch-plan-upload-bucket")
AWS_ACCESS_KEY = os.getenv("GA_AWS_ACCESS_KEY", "")
AWS_SECRET_KEY = os.getenv("GA_AWS_SECRET_KEY", "")
LEVEL = os.getenv("LEVEL", "debug")
TF_PLAN_FILE = "tfplan-output.json"
TF_PLAN_FILTERED_FILE = "tfplan-filtered.json"
STREAM_MAJOR_VERSION = os.getenv("STREAM_MAJOR_VERSION", "2")
TIMEOUT = os.getenv("TIMEOUT", 3600)
RUN_MIGRATION_SCRIPTS = os.getenv("RUN_MIGRATION_SCRIPTS", "false")

# New configuration variables
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY", "")
GENERATE_AI_SUMMARY = os.getenv("GENERATE_AI_SUMMARY", "true").lower() == "true"
FILTER_SENSITIVE_VALUES = os.getenv("FILTER_SENSITIVE_VALUES", "true").lower() == "true"
EXTRACT_CHANGES_ONLY = os.getenv("EXTRACT_CHANGES_ONLY", "true").lower() == "true"
CLICKUP_API_TOKEN = os.getenv("CLICKUP_API_TOKEN", "")
CLICKUP_SPACE_ID = os.getenv("CLICKUP_SPACE_ID", "")
CLICKUP_ROOT_PAGE_ID = os.getenv("CLICKUP_ROOT_PAGE_ID", "")
CLICKUP_DOC_ID = os.getenv("CLICKUP_DOC_ID", "")

MAJOR_VERSION = os.getenv("MAJOR_VERSION", "50")
MINOR_VERSION = os.getenv("MINOR_VERSION", "latest")

FORCE_RUN_ALL_ENVIRONMENTS = os.getenv("FORCE_RUN_ALL_ENVIRONMENTS", "false")

with open("./control_planes.json", "r") as ff:
    endpoints = json.load(ff)

###################################################################
"""
GA plan Uploader
"""


###################################################################
async def check_successful_releases(cluster_id: str, url: str, username: str, password: str, stack_name: str = "", cluster_name: str = "") -> bool:
    """
    Checks if there are successful releases in the last 14 days for a given cluster.

    Args:
        cluster_id (str): The ID of the cluster.
        url (str): The URL of the API endpoint.
        username (str): The username for the API endpoint.
        password (str): The password for the API endpoint.

    Returns:
        bool: True if there are successful releases in the last 14 days, False otherwise.
    """
    end_date = datetime.now(TIME_ZONE)
    start_date = end_date - timedelta(days=14)
    auth_value = base64.b64encode(f"{username}:{password}".encode("utf-8")).decode("utf-8")
    headers = {"Authorization": f"Basic {auth_value}"}
    parameters = {
        "clusterId": cluster_id,
        "end": end_date.isoformat(),
        "releaseType": "RELEASE",
        "start": start_date.isoformat(),
        "excludeStatus": ["FAULT", "TIMED_OUT", "IN_PROGRESS", "STOPPED", "STARTED" ,"QUEUED" , "APPROVED", "REJECTED"]
    }
    search_url = f"{url}/cc-ui/v1/clusters/{cluster_id}/deployments/search"
    async with aiohttp.ClientSession(
        timeout=aiohttp.ClientTimeout(total=900)
    ) as session:
        async with session.get(
            search_url, headers=headers, params=parameters
        ) as response:
            if response.status != 200:
                cluster_identifier = f"{stack_name}/{cluster_name}" if stack_name and cluster_name else cluster_id
                raise Exception(
                    f"Unable to fetch last 14 days of successful releases for cluster {cluster_identifier}: {response.status}: {await response.text()}"
                )
            data = await response.json()
            content = data.get("content", [])
            if len(content) > 0:
                status = content[0].get('status')
                if status not in ["SUCCEEDED"]:
                    cluster_identifier = f"{stack_name}/{cluster_name}" if stack_name and cluster_name else cluster_id
                    print(f"Skipping {cluster_identifier} since there is a {status} FULL release in the environment in control plane {url}.")
                    return False
            return True


async def fetch_stacks(session: aiohttp.ClientSession, url: str, username:str, password: str) -> dict:
    """
    Fetches all the stacks of a given control Plane.

    Args:
        session (aiohttp.ClientSession): The HTTP session in order to make api calls
        url (str): The URL of the API endpoint.
        password (str): The password for the API endpoint.

    Returns:
        dict: Returns the json response as a dictionary (json)
    """
    url = f"{url}/cc-ui/v1/stacks/"
    auth_value = base64.b64encode(f"{username}:{password}".encode("utf-8")).decode("utf-8")
    headers = {"Authorization": f"Basic {auth_value}"}
    async with session.get(url, headers=headers) as response:
        if response.status != 200:
            raise Exception(
                f"Failed to fetch stacks: {response.status}: {await response.text()}"
            )
        return await response.json()


async def get_clusters_by_stackname(
    session: aiohttp.ClientSession, endpoint: dict, stack_name: str
) -> dict:
    """
    Fetches clusters with status for a given stack name.

    Args:
        session (aiohttp.ClientSession): The HTTP session for making API calls.
        endpoint (dict): The endpoint details containing URL and password.
        stack_name (str): The name of the stack to fetch clusters for.

    Returns:
        dict: Returns the JSON response as a dictionary.
    """
    url = f"{endpoint['url']}/cc-ui/v1/stacks/{stack_name}/clustersWithStatus"
    auth_value = base64.b64encode(
        f"{endpoint['username']}:{endpoint['password']}".encode("utf-8")
    ).decode("utf-8")
    headers = {"Authorization": f"Basic {auth_value}"}
    async with session.get(url, headers=headers) as response:
        if response.status != 200:
            raise Exception(
                f"Failed to fetch clusters for stack {stack_name}: {response.status}: {await response.text()}"
            )
        return await response.json()


async def get_cluster_version(
    session: aiohttp.ClientSession, endpoint: dict, cluster_id: str, cluster_name: str
) -> dict:
    """
    Fetches the version of a cluster with the given cluster ID.

    Args:
        session (aiohttp.ClientSession): The HTTP session for making API calls.
        endpoint (dict): The endpoint details containing URL and password.
        cluster_id (str): The ID of the cluster to fetch the version for.

    Returns:
        dict: Returns the version information of the cluster as a dictionary.
    """
    url = f"{endpoint['url']}/cc-ui/v1/terraform/cluster/{cluster_id}"
    auth_value = base64.b64encode(
        f"{endpoint['username']}:{endpoint['password']}".encode("utf-8")
    ).decode("utf-8")
    headers = {"Authorization": f"Basic {auth_value}"}
    async with session.get(url, headers=headers) as response:
        if response.status == 404:
            return (
                {}
            )  # Return empyt dict if the cluster version is not found (404 error)
        elif response.status != 200:
            raise Exception(
                f"Failed to fetch cluster version for cluster {cluster_name} (ID: {cluster_id}): {response.status}: {await response.text()}"
            )
        response_json = await response.json()
        return response_json.get("version", {})


async def create_and_wait_for_deployment(
    console: Console,
    session: aiohttp.ClientSession,
    endpoint: str,
    username: str,
    password: str,
    cluster_id: str,
    presigned_url: dict,
    stack_name: str,
    cluster_name: str = "",
):
    """
    Creates and waits for a deployment.

    Args:
        console: The console object for logging.
        session (aiohttp.ClientSession): The HTTP session for making API calls.
        endpoint (str): The endpoint URL.
        username (str): The username for authentication.
        password (str): The password for authentication.
        cluster_id (str): The ID of the cluster.
        presigned_url (dict): The presigned URL for S3.

    Returns:
        dict: Returns the deployment response as a dictionary.
    """
    url = f"{endpoint}/cc-ui/v1/clusters/{cluster_id}/deployments"
    auth_value = base64.b64encode(f"{username}:{password}".encode("utf-8")).decode("utf-8")
    headers = {
        "Authorization": f"Basic {auth_value}",
        "Content-Type": "application/json",
    }
    presigned_url["fields"]["file"] = "@{key}".format(key=TF_PLAN_FILE)
    form_values = " ".join(
        [
            "-F {key}={value}".format(key=key, value=value)
            for key, value in presigned_url["fields"].items()
        ]
    )
    curl_cmd = "curl -sS {form_values} {url}".format(
        form_values=form_values, url=presigned_url["url"]
    )
    body = {
        "overrideBuildSteps": [
            f"bash  /sources/primary/capillary-cloud-tf/tfmain/scripts/baseinfra_migration_plan.sh {RUN_MIGRATION_SCRIPTS}",
            f"""
            {curl_cmd}
            """,
        ],
        "allowDestroy": True,
        "releaseComment": "This is a custom release where we do a custom terraform plan from the GA planner bot to check the release candidates stability, PS: This is internal for facets",
        "forceRelease": True,
        "releaseType": "CUSTOM",
        "tfVersion": {
            "majorVersion": int(MAJOR_VERSION),
            "minorVersion": MINOR_VERSION,
            "tfStream": RC_BRANCH,
        },
        "withRefresh": False,
    }
    environment_link = f"{endpoint}/capc/stack/{stack_name}/releases/cluster/{cluster_id}"
    while True:
        async with session.post(url, headers=headers, json=body) as response:
            if response.status == 403:
                await asyncio.sleep(10)  # Wait for 10 seconds before retrying
                cluster_identifier = f"{stack_name}/{cluster_name}" if cluster_name else cluster_id
                console.log(
                    f"A deployment is already in progress for {cluster_identifier} - {environment_link}... Waiting for existing deployment to complete"
                )
                continue
            deployment_response = await response.json()
            deployment_id = deployment_response.get("id")
            release_url = f"{environment_link}/dialog/release-details/{deployment_id}"
            error_logs = deployment_response.get("errorLogs", None)
            if error_logs is not None:
                cluster_identifier = f"{stack_name}/{cluster_name}" if cluster_name else cluster_id
                raise Exception(
                    f"Deployment failed for cluster {cluster_identifier} - {release_url} : {error_logs}"
                )
            else:
                status = None
                while True:
                    deployment_url = f"{endpoint}/cc-ui/v1/clusters/{cluster_id}/deployments/{deployment_id}"
                    async with session.get(
                        deployment_url, headers=headers
                    ) as deployment_response:
                        if deployment_response.status != 200:
                            raise Exception(
                                f"Failed to get deployment status for deployment ID {deployment_id} URL {deployment_url}: {deployment_response.status}: {await deployment_response.text()}"
                            )
                        deployment_status = await deployment_response.json()
                        if status != deployment_status.get('status'):
                            cluster_identifier = f"{stack_name}/{cluster_name}" if cluster_name else cluster_id
                            console.log(
                                f"{deployment_status.get('status')} for {cluster_identifier} - {endpoint}/capc/{stack_name}/cluster/{cluster_id}/release-details/{deployment_id}"
                            )
                        status = deployment_status.get('status')
                        if status in ["SUCCEEDED", "FAILED"]:
                            break
                        await asyncio.sleep(
                            10
                        )  # Wait for 10 seconds before checking again
                return release_url

async def file_exists_in_s3(
        file_name: str, access_key: str, secret_key: str
) -> bool:
    """
    Check if a file exists in S3 bucket.

    Args:
        file_name (str): The name/key of the file to check.
        access_key (str): The AWS access key.
        secret_key (str): The AWS secret key.

    Returns:
        bool: True if file exists, False otherwise.
    """
    try:
        s3 = boto3.client(
            "s3",
            aws_access_key_id=access_key,
            aws_secret_access_key=secret_key,
            region_name="ap-south-1",
        )

        s3.head_object(Bucket=PLAN_BUCKET, Key=file_name)
        return True

    except ClientError as e:
        if e.response['Error']['Code'] == '404':
            return False
        else:
            # For other AWS errors (permissions, etc.), return False
            return False

    except Exception:
        # For any other errors, return False
        return False


async def get_presigned_url_for_s3(
    file_name: str, access_key: str, secret_key: str
) -> dict:
    """
    Generate a presigned URL for uploading a file to S3.

    Args:
        file_name (str): The name of the file to be uploaded.
        access_key (str): The AWS access key.
        secret_key (str): The AWS secret key.

    Returns:
        dict: The presigned URL for uploading the file to S3.
    """
    s3 = boto3.client(
        "s3",
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
        region_name="ap-south-1",
    )
    presigned_url = s3.generate_presigned_post(
        PLAN_BUCKET, file_name, ExpiresIn=TIMEOUT
    )
    return presigned_url


async def get_info(session: aiohttp.ClientSession, name: str, endpoint: dict, control_plane_key: str) -> dict:
    """
    Fetch information about stacks, clusters, and versions.

    Args:
        session (aiohttp.ClientSession): The aiohttp client session.
        name (str): The name of the stack.
        endpoint (dict): The endpoint details containing URL and password.
        control_plane_key (str): The control plane key (e.g., 'root', 'moveinsync').

    Returns:
        dict: A dictionary containing information about stacks, clusters, and versions.
    """
    console = Console()
    console.log(f"Fetching stack {name} from {endpoint['url']}")
    stacks = await fetch_stacks(session, endpoint["url"], endpoint["username"], endpoint["password"])
    stacks_clusters_versions = {}  # Initialize stacks_clusters_versions here
    white_listed_clusters = endpoint.get("whitelisted_clusters", [])
    for stack in stacks:
        stack_name = stack.get("name")
        clusters = await get_clusters_by_stackname(session, endpoint, stack_name)
        if stack_name not in stacks_clusters_versions:
            stacks_clusters_versions[stack_name] = {}
        for cluster in clusters:
            cluster_id = cluster.get("id")
            cluster_name = cluster.get("name", "")
            version = await get_cluster_version(
                session, endpoint, cluster_id, cluster_name
            )
            if len(white_listed_clusters) > 0:
                if cluster_name in white_listed_clusters and len(version) > 0:
                    stacks_clusters_versions[stack_name][cluster_id] = {
                        "name": cluster.get("name"),
                        "state": cluster.get("clusterState"),
                        "id": cluster_id,
                        "version": version,
                        "url": endpoint["url"],
                        "username": endpoint["username"],
                        "password": endpoint["password"],
                        "control_plane_key": control_plane_key,
                    }
            else:
                if len(version) > 0:
                    stacks_clusters_versions[stack_name][cluster_id] = {
                        "name": cluster.get("name"),
                        "state": cluster.get("clusterState"),
                        "id": cluster_id,
                        "version": version,
                        "url": endpoint["url"],
                        "username": endpoint["username"],
                        "password": endpoint["password"],
                        "control_plane_key": control_plane_key,
                    }

    return stacks_clusters_versions


async def process_cluster(console: Console, stack_name: str, cluster: dict, ai_analyzer=None, previous_rc_branch: str = "", rc_page_id: str = None) -> tuple:
    """
    Process a cluster for deployment and generate AI analysis report.

    Args:
        console (Console): The console object for logging.
        stack_name (str): The name of the stack.
        cluster (dict): The cluster details.
        ai_analyzer: OpenAI analyzer instance for generating reports.
        previous_rc_branch (str): Previous RC branch name for comparison.

    Returns:
        tuple: A tuple containing stack_name, cluster_name, cluster_state, cluster_id, cluster_version, plan_path, and release_link.
    """
    async with aiohttp.ClientSession(
        timeout=aiohttp.ClientTimeout(total=int(TIMEOUT))
    ) as session:
        cluster_id = cluster.get("id", "")
        cluster_version = cluster.get("version", {}).get("tfStream", None)
        cluster_name = cluster.get("name")
        cluster_state = cluster.get("state")
        cc_endpoint = cluster.get("url", "")
        cc_username = cluster.get("username", "")
        cc_password = cluster.get("password", "")
        plan_path = f"{stack_name}/{cluster_id}/{RC_BRANCH}/{TF_PLAN_FILE}"
        release_link = "NOT RUN IN THIS GA PLANNER, REFER TO THE PREVIOUS RUNS TO KNOW THE RELEASE LINK"
        
        if FORCE_RUN_ALL_ENVIRONMENTS == "true" or not await file_exists_in_s3(plan_path, access_key=AWS_ACCESS_KEY, secret_key=AWS_SECRET_KEY):
            presigned_url = await get_presigned_url_for_s3(
                plan_path,
                access_key=AWS_ACCESS_KEY,
                secret_key=AWS_SECRET_KEY,
            )
            release_link = await create_and_wait_for_deployment(
                console,
                session,
                cc_endpoint,
                cc_username,
                cc_password,
                cluster_id,
                presigned_url,
                stack_name,
                cluster_name,
            )  # Create deployments for the cluster
        
        # Download and process plans asynchronously
        plan_metadata = await process_plans_async(
            console, stack_name, cluster_id, cluster_name, plan_path, previous_rc_branch
        )
        
        # Generate AI analysis if analyzer is available and plans were processed successfully
        if ai_analyzer and plan_metadata:
            control_plane_key = cluster.get("control_plane_key", "")
            await analyze_plans_async(console, plan_metadata, ai_analyzer, previous_rc_branch, control_plane_key, rc_page_id)
        
        return (
            stack_name,
            cluster_name,
            cluster_state,
            cluster_id,
            cluster_version,
            plan_path,
            release_link,
        )


async def process_plans_async(console: Console, stack_name: str, cluster_id: str, 
                            cluster_name: str, plan_path: str, previous_rc_branch: str) -> dict:
    """
    Download and process plans asynchronously for a single environment.
    Returns processed plan metadata for further analysis.
    """
    try:
        # Download current RC plan
        console.log(f"[cyan]Downloading current RC plan for {stack_name}/{cluster_name}")
        if not download_plan_from_s3(PLAN_BUCKET, plan_path, plan_path, AWS_ACCESS_KEY, AWS_SECRET_KEY):
            console.log(f"[red]Failed to download current RC plan for {stack_name}/{cluster_name}")
            return {}
        
        # Download previous RC plan if available
        previous_plan_path = None
        if previous_rc_branch:
            previous_plan_path = plan_path.replace(f"/{RC_BRANCH}/", f"/{previous_rc_branch}/")
            previous_plan_key = f"{stack_name}/{cluster_id}/{previous_rc_branch}/tfplan-output.json"
            
            console.log(f"[cyan]Downloading previous RC plan: {previous_rc_branch} for {stack_name}/{cluster_name}")
            if not download_plan_from_s3(PLAN_BUCKET, previous_plan_key, previous_plan_path, AWS_ACCESS_KEY, AWS_SECRET_KEY):
                console.log(f"[yellow]Could not download previous RC plan for {stack_name}/{cluster_name}")
                previous_plan_path = None
        
        # Process both plans
        console.log(f"[cyan]Processing plans for {stack_name}/{cluster_name}")
        _, _, _, plan_metadata = process_plan(plan_path, previous_plan_path)
        
        if not plan_metadata:
            console.log(f"[red]Failed to process plans for {stack_name}/{cluster_name}")
            return {}
        
        # Add environment information to metadata
        plan_metadata["environment"] = f"{stack_name}/{cluster_name}"
        plan_metadata["plan_path"] = plan_path
        
        console.log(f"[green]Successfully processed plans for {stack_name}/{cluster_name}")
        return plan_metadata
            
    except Exception as e:
        console.log(f"[red]Error processing plans for {stack_name}/{cluster_name}: {e}")
        return {}


async def analyze_plans_async(console: Console, plan_metadata: dict, ai_analyzer, previous_rc_branch: str, control_plane_key: str = "", rc_page_id: str = None):
    """
    Generate AI analysis for processed plan metadata.
    Saves the AI analysis report in the same directory as the current RC plan and publishes to Google Docs.
    """
    if not plan_metadata or not ai_analyzer:
        return
    
    try:
        environment = plan_metadata.get("environment", "Unknown")
        plan_path = plan_metadata.get("plan_path", "")
        release_link = plan_metadata.get("release_link", "")
        
        console.log(f"[cyan]Generating AI analysis for {environment}")
        
        # Create context for single environment analysis
        environment_context = f"Environment: {environment}"
        if previous_rc_branch:
            environment_context += f"\nRC Comparison: {RC_BRANCH} vs {previous_rc_branch}"
        
        # Extract the filtered plan from metadata for AI analysis
        filtered_plan_data = plan_metadata.get("filtered_plan", {})
        if not filtered_plan_data:
            console.log(f"[yellow]No filtered plan data found for {environment}")
            return
        
        # Get previous plan data from metadata
        previous_filtered_plan = plan_metadata.get("previous_rc_plan", {})
        
        # Use the single plan analysis with RC comparison data
        analysis_report = await ai_analyzer.analyze_single_plan(filtered_plan_data, previous_filtered_plan, environment_context)
        
        if analysis_report:
            # Save report in the same directory as the current RC plan
            plan_directory = "/".join(plan_path.split("/")[:-1])  # Remove filename
            report_filename = f"{plan_directory}/ai_analysis_report_{datetime.now().strftime('%Y%m%d_%H%M%S')}.md"
            
            # Save the analysis report locally
            ai_analyzer.save_analysis_report(
                analysis_report,
                report_filename
            )
            console.log(f"[green]AI analysis saved to: {report_filename}")
            
            # Publish to ClickUp if configured
            if CLICKUP_API_TOKEN and CLICKUP_SPACE_ID and control_plane_key:
                # Parse stack_name and cluster_name from environment
                parts = environment.split("/")
                if len(parts) >= 2:
                    stack_name = parts[0]
                    cluster_name = parts[1]
                    await publish_to_clickup(console, RC_BRANCH, stack_name, cluster_name, analysis_report, rc_page_id, release_link)
            
        else:
            console.log(f"[yellow]Could not generate AI analysis for {environment}")
            
    except Exception as e:
        console.log(f"[red]Error analyzing plans for {plan_metadata.get('environment', 'Unknown')}: {e}")


async def publish_to_clickup(console: Console, rc_name: str, stack_name: str, 
                            cluster_name: str, analysis_report: str, rc_page_id: str = None, 
                            release_link: str = ""):
    """
    Publishes analysis report to ClickUp with nested page structure.
    Creates nested pages in the format: root_page > rc_name page > stack_name page > cluster_name page
    
    Args:
        console (Console): Console for logging
        rc_name (str): RC branch name (e.g., 'rc-3.27.0')
        stack_name (str): Name of the stack
        cluster_name (str): Name of the cluster
        analysis_report (str): The AI analysis report content
        rc_page_id (str): Pre-created RC page ID to reuse
        release_link (str): Release link from current RC plan
    """
    if not CLICKUP_API_TOKEN or not CLICKUP_SPACE_ID or not rc_page_id:
        console.log("[yellow]ClickUp integration not configured or missing RC page ID. Skipping publication.")
        return
    
    try:
        headers = {
            "Authorization": f"{CLICKUP_API_TOKEN}",
            "Content-Type": "application/json",
            "accept": "application/json"
        }
        
        # Use the pre-created RC page ID
        stack_page_id = await create_clickup_page(headers, rc_page_id, stack_name,
                                                 f"# {stack_name}\n\nTerraform plan analysis for stack: {stack_name}")
        
        # Create the final analysis page for the cluster using v3 API
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        cluster_page_title = cluster_name
        
        # Build page content with release link if available
        page_content = f"""# Terraform Plan Analysis Report

**Environment:** {stack_name}/{cluster_name}  
**RC Branch:** {rc_name}  
**Generated:** {timestamp}
"""
        
        # Add release link if available
        if release_link:
            page_content += f"\n**Release Link:** {release_link}"
        
        page_content += f"\n\n{analysis_report}\n"
        
        # Create the cluster analysis page
        cluster_page_id = await create_clickup_page(headers, stack_page_id, cluster_page_title, page_content)
        
        if cluster_page_id:
            console.log(f"[green]Created ClickUp page for {rc_name}/{stack_name}/{cluster_name}")
        else:
            console.log(f"[red]Failed to create ClickUp page for {rc_name}/{stack_name}/{cluster_name}")
        
    except Exception as e:
        console.log(f"[red]Error publishing to ClickUp for {rc_name}/{stack_name}/{cluster_name}: {e}")





async def create_clickup_page(headers: dict, parent_page_id: str, page_name: str, 
                             page_content: str = "") -> str:
    """
    Create a ClickUp page using the v3 API only.
    
    Args:
        headers (dict): API headers with authorization  
        parent_page_id (str): Parent page ID
        page_name (str): Name of the page to create
        page_content (str): Content for the page (markdown format)
    
    Returns:
        str: Page ID of newly created page
    """
    try:
        # Create new page using v3 API
        if not CLICKUP_DOC_ID:
            raise Exception("CLICKUP_DOC_ID is required for v3 API page creation")
            
        url = f"https://api.clickup.com/api/v3/workspaces/{CLICKUP_SPACE_ID}/docs/{CLICKUP_DOC_ID}/pages"
        
        payload = {
            "parent_page_id": parent_page_id,
            "name": page_name,
            "content_format": "text/md",
            "content": page_content or f"# {page_name}\n\nThis page was created automatically."
        }
        
        page_headers = {
            "accept": "application/json",
            "content-type": "application/json",  
            "Authorization": headers.get("Authorization", "")
        }
        
        async with aiohttp.ClientSession() as session:
            async with session.post(url, json=payload, headers=page_headers) as response:
                if response.status == 201:  # v3 API returns 201 on successful creation
                    result = await response.json()
                    return result.get("id")
                else:
                    error_text = await response.text()
                    raise Exception(f"Failed to create page via v3 API: {response.status} - {error_text}")
                    
    except Exception as e:
        raise Exception(f"Error creating page {page_name}: {e}")


###################################################################
"""
GA plan Classifier
"""
###################################################################


def download_plan_from_s3(
    bucket_name: str, file_key: str, local_path: str, access_key: str, secret_key: str
) -> bool:
    """
    Download a plan from S3 and store it locally.

    Args:
        bucket_name (str): The name of the S3 bucket.
        file_key (str): The key of the plan in the S3 bucket.
        local_path (str): The local path where the plan will be stored.
        
    Returns:
        bool: True if download was successful, False otherwise.
    """
    s3 = boto3.client(
        "s3",
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
        region_name="ap-south-1",
    )
    os.makedirs(os.path.join(*local_path.split("/")[:-1]), exist_ok=True)
    try:
        s3.download_file(bucket_name, file_key, local_path)
        return True
    except Exception as e:
        print(f"An error occurred while downloading the file s3://{bucket_name}/{file_key}: {e}")
        return False


def get_previous_rc_branch(current_rc: str) -> str:
    """
    Get the previous RC branch by querying git for all RC branches and finding the one before current.
    
    Args:
        current_rc (str): Current RC branch like 'rc-3.27.0'
    
    Returns:
        str: Previous RC branch like 'rc-3.26.0' or empty string if not found
    """
    try:
        
        # Get all remote branches that start with 'rc-'
        result = subprocess.run(
            ['git', 'branch', '-r', '--list', 'origin/rc-*'],
            capture_output=True,
            text=True,
            cwd=os.getcwd()
        )
        
        if result.returncode != 0:
            print(f"Failed to get git branches: {result.stderr}")
            return ""
        
        # Extract RC branch names and parse versions
        rc_branches = []
        for line in result.stdout.strip().split('\n'):
            if line.strip():
                # Remove 'origin/' prefix and whitespace
                branch = line.strip().replace('origin/', '')
                # Match rc-x.y.z pattern
                match = re.match(r'rc-(\d+)\.(\d+)\.(\d+)', branch)
                if match:
                    major, minor, patch = int(match.group(1)), int(match.group(2)), int(match.group(3))
                    rc_branches.append((branch, major, minor, patch))
        
        if not rc_branches:
            print("No RC branches found in git")
            return ""
        
        # Sort RC branches by version (major, minor, patch)
        rc_branches.sort(key=lambda x: (x[1], x[2], x[3]))
        
        # Find current RC in the sorted list
        current_index = -1
        for i, (branch, major, minor, patch) in enumerate(rc_branches):
            if branch == current_rc:
                current_index = i
                break
        
        if current_index == -1:
            print(f"Current RC branch '{current_rc}' not found in git branches")
            return ""
        
        if current_index == 0:
            print(f"'{current_rc}' is the first RC branch, no previous RC available")
            return ""
        
        # Return the previous RC branch
        previous_rc = rc_branches[current_index - 1][0]
        print(f"Found previous RC: {previous_rc} (before {current_rc})")
        return previous_rc
        
    except Exception as e:
        print(f"Error getting previous RC branch: {e}")
        return ""




# Constants for file naming
TERRAFORM_PLAN_OUTPUT_FILE = "tfplan-output.json"
TERRAFORM_PLAN_FILTERED_FILE = "tfplan-filtered.json"

def load_and_save_filtered_plan(plan_path: str, plan_type: str = "current") -> tuple:
    """
    Load and filter a terraform plan, then save the filtered version.
    
    Args:
        plan_path (str): Path to the terraform plan file
        plan_type (str): Type of plan ("current" or "previous") for logging
    
    Returns:
        tuple: (filtered_plan_data, release_link) - filtered plan data and release link from original plan (only for current plans)
    """
    print(f"Loading and filtering {plan_type} plan: {plan_path}")
    
    # For current plans, extract release_link from raw plan
    release_link = ""
    if plan_type == "current":
        try:
            with open(plan_path, 'r') as f:
                raw_plan = json.load(f)
            release_link = raw_plan.get("release_link", "")
        except Exception as e:
            print(f"Warning: Could not load raw {plan_type} plan for release_link extraction: {e}")
    
    # Load and filter the plan
    filtered_plan = load_and_filter_plan(
        plan_path,
        filter_sensitive=FILTER_SENSITIVE_VALUES,
        resource_changes_only=True,  # Always True - focus on resource changes only
        changes_only=EXTRACT_CHANGES_ONLY
    )
    
    if not filtered_plan:
        print(f"Failed to load or filter {plan_type} plan: {plan_path}")
        return {}, release_link
    
    # Save the filtered plan
    filtered_plan_path = plan_path.replace(TERRAFORM_PLAN_OUTPUT_FILE, TERRAFORM_PLAN_FILTERED_FILE)
    try:
        with open(filtered_plan_path, 'w') as f:
            json.dump(filtered_plan, f, indent=2)
        print(f"Saved filtered {plan_type} plan to: {filtered_plan_path}")
    except Exception as e:
        print(f"Warning: Could not save filtered {plan_type} plan to {filtered_plan_path}: {e}")
    
    return filtered_plan, release_link

def extract_rc_branch_from_path(plan_path: str) -> str:
    """
    Extract RC branch name from a plan path.
    
    Args:
        plan_path (str): Path containing RC branch information
    
    Returns:
        str: RC branch name or empty string if not found
    """
    path_parts = plan_path.split("/")
    for part in path_parts:
        if part.startswith("rc-"):
            return part
    return ""

def categorize_resource_changes(resource_changes: list) -> tuple:
    """
    Categorize resource changes into deleted, recreated, and updated resources.
    
    Args:
        resource_changes (list): List of resource change objects
    
    Returns:
        tuple: (deleted_resources, recreated_resources, updated_changes)
    """
    deleted_resources = {}
    recreated_resources = {}
    updated_changes = []

    for resource in resource_changes:
        if not isinstance(resource, dict):
            continue
            
        actions = resource.get("change", {}).get("actions", [])
        after = resource.get("change", {}).get("after")
        module_address = resource.get("module_address", "root")
        resource_type = resource.get("type", "unknown")

        if "create" in actions and "delete" in actions:
            # Resource is being recreated
            if resource_type != "null_resource":
                if module_address not in recreated_resources:
                    recreated_resources[module_address] = []
                recreated_resources[module_address].append(resource_type)
                
        elif "delete" in actions:
            # Resource is being deleted
            if after is None:
                if module_address not in deleted_resources:
                    deleted_resources[module_address] = []
                deleted_resources[module_address].append(resource_type)
                
        elif "update" in actions:
            # Resource is being updated
            if resource_type != "scratch_string":
                changes = get_updates(resource)
                if changes:
                    updated_changes.append(changes)

    return deleted_resources, recreated_resources, updated_changes

def process_plan(planfile_path: str, previous_plan_path: str = None):
    """
    Enhanced process_plan function that uses auto-detection and filtering capabilities.
    Always filters sensitive attributes for Claude safety.
    Processes both current and previous RC plans if provided.
    """
    # Process current plan
    current_filtered_plan, current_release_link = load_and_save_filtered_plan(planfile_path, "current")
    if not current_filtered_plan:
        return ({}, {}, {}, {})

    # Extract resource changes from current plan
    current_resource_changes = current_filtered_plan.get("resource_changes", [])
    
    # Process previous plan if provided
    previous_filtered_plan = None
    previous_rc_branch = ""
    if previous_plan_path:
        previous_rc_branch = extract_rc_branch_from_path(previous_plan_path)
        print(f"Processing previous RC plan: {previous_rc_branch}")
        previous_filtered_plan, _ = load_and_save_filtered_plan(previous_plan_path, "previous")
        
        if not previous_filtered_plan:
            print(f"Failed to filter previous RC plan from {previous_rc_branch}")
    
    # Categorize resource changes using helper function
    deleted_resources, recreated_resources, updated_changes = categorize_resource_changes(current_resource_changes)
    
    # Generate environment name from plan path
    environment_name = "_".join(planfile_path.split("/")[:-2])
    
    # Group updated changes for display
    updated_grouped_data = group_json(environment_name, updated_changes)
    
    # Build metadata for AI analysis
    plan_metadata = {
        "environment": environment_name,
        "plan_file": planfile_path,
        "filtering_applied": FILTER_SENSITIVE_VALUES,
        "resource_changes_count": len(current_resource_changes),
        "filtered_plan": current_filtered_plan,
        "auto_detection_info": current_filtered_plan.get("metadata", {}),
        "previous_rc_plan": previous_filtered_plan,
        "previous_rc_branch": previous_rc_branch,
        "release_link": current_release_link
    }

    return (recreated_resources, deleted_resources, updated_grouped_data, plan_metadata)


def group_json(name, json_data):
    new_dict = {}
    for data in json_data:
        for key, value in data.items():
            if key not in new_dict:
                new_dict[key] = []
            new_dict[key].append(value)
    return {name: new_dict}


def get_updates(plan: dict):
    """
    Function to get changes from a Terraform plan object.

    Args:
        plan (dict): The Terraform plan object.

    Returns:
        str: A markdown string containing the changes.
    """
    changes = plan.get("change", {})
    before = changes.get("before", {})
    type = plan.get("type", "")
    name = plan.get("name", "")
    index = plan.get("index", None)

    # Find the differences between before and after
    changes_dict = {"module_address": plan.get("module_address", None)}

    if name == "app-chart":
        name = before.get("name", "")

    if index is None:  # to filter out  None values
        changes_req = {f"{type}.{name}": changes_dict}
    else:
        changes_req = {f"{type}.{name}[{index}]": changes_dict}
    return changes_req


###################################################################
"""
Main Function
"""


###################################################################
async def main():
    console = Console()
    with Progress(
        SpinnerColumn(),
        TextColumn("[progress.description]{task.description}"),
        console=console,
    ) as progress:
        fetch_task = progress.add_task(
            "[cyan]Fetching stacks, clusters, and versions...\n", total=len(endpoints)
        )
        async with aiohttp.ClientSession(
            timeout=aiohttp.ClientTimeout(total=300)
        ) as session:
            tasks = [
                get_info(session, control_plane_key, endpoint, control_plane_key)
                for control_plane_key, endpoint in endpoints.items()
            ]
            results = await asyncio.gather(*tasks)
            stacks_clusters_versions = {}
            for result in results:
                for stack_name, clusters in result.items():
                    stacks_clusters_versions[stack_name] = clusters
                progress.update(fetch_task, advance=1)


    # Initialize OpenAI analyzer if API key is provided
    ai_analyzer = None
    if GENERATE_AI_SUMMARY and OPENAI_API_KEY and OPENAI_API_KEY.strip():
        try:
            ai_analyzer = OpenAIAnalyzer(OPENAI_API_KEY)
            console.log("[green]OpenAI integration enabled for summary generation")
        except Exception as e:
            console.log(f"[yellow]Warning: Could not initialize OpenAI analyzer: {e}")
    else:
        if GENERATE_AI_SUMMARY:
            console.log("[yellow]Warning: GENERATE_AI_SUMMARY is enabled but OPENAI_API_KEY is not set or empty")
    
    # Get previous RC branch once at the top level (always enabled)
    previous_rc_branch = ""
    if RC_BRANCH:
        console.log(f"[green]RC comparison enabled for branch: {RC_BRANCH}")
        previous_rc_branch = get_previous_rc_branch(RC_BRANCH)
        if previous_rc_branch:
            console.log(f"[green]Will compare with previous RC: {previous_rc_branch}")
        else:
            console.log(f"[yellow]Could not determine previous RC for {RC_BRANCH}")

    # Create RC page once for ClickUp integration
    rc_page_id = None
    if CLICKUP_API_TOKEN and CLICKUP_SPACE_ID and CLICKUP_ROOT_PAGE_ID and RC_BRANCH:
        try:
            headers = {
                "Authorization": f"{CLICKUP_API_TOKEN}",
                "Content-Type": "application/json",
                "accept": "application/json"
            }
            rc_page_id = await create_clickup_page(headers, CLICKUP_ROOT_PAGE_ID, RC_BRANCH, 
                                                  f"# {RC_BRANCH}\n\nTerraform plan analysis for RC branch: {RC_BRANCH}")
            if rc_page_id:
                console.log(f"[green]Created RC page once: {RC_BRANCH}")
            else:
                console.log(f"[yellow]Failed to create RC page: {RC_BRANCH}")
        except Exception as e:
            console.log(f"[yellow]Could not create RC page: {e}")

    cluster_tasks = [
        process_cluster(console, stack_name, cluster, ai_analyzer, previous_rc_branch, rc_page_id)
        for stack_name, clusters in stacks_clusters_versions.items()
        for cluster in clusters.values()
        if await check_successful_releases(
            cluster.get("id"), cluster.get("url"), cluster.get("username"), cluster.get("password"), stack_name, cluster.get("name")
        )
    ]

    # Process all cluster tasks
    processed_clusters = await asyncio.gather(*cluster_tasks)
    
    # Print summary statistics
    console.log(f"\n[bold green]Summary:[/bold green]")
    console.log(f"- Processed {len(processed_clusters)} environments")
    if RC_BRANCH:
        console.log(f"- RC comparison enabled for branch: {RC_BRANCH}")
    
    # Individual AI analysis reports have already been generated and saved per environment
    if GENERATE_AI_SUMMARY and not OPENAI_API_KEY:
        console.log("[yellow]Warning: AI summary generation enabled but OPENAI_API_KEY not provided")
    elif GENERATE_AI_SUMMARY and OPENAI_API_KEY:
        console.log(f"[green]Individual AI analysis reports generated for each environment")


if __name__ == "__main__":
    asyncio.run(main())
