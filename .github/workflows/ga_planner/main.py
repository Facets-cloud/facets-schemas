import aiohttp
import asyncio
import base64
import json
import boto3
import os
from rich.console import Console
from rich.table import Table
from rich.progress import Progress, SpinnerColumn, TextColumn
from datetime import datetime, timedelta
import pytz


TIME_ZONE = pytz.timezone("Asia/Kolkata")
RC_BRANCH = os.getenv("RC_BRANCH")
PLAN_BUCKET = os.getenv("PLAN_BUCKET", "rc-branch-plan-upload-bucket")
AWS_ACCESS_KEY = os.getenv("GA_AWS_ACCESS_KEY", "")
AWS_SECRET_KEY = os.getenv("GA_AWS_SECRET_KEY", "")
LEVEL = os.getenv("LEVEL", "debug")
TF_PLAN_FILE = "tfplan-output.json"
STREAM_MAJOR_VERSION = os.getenv("STREAM_MAJOR_VERSION", "2")
TIMEOUT = os.getenv("TIMEOUT", 3600)
RUN_MIGRATION_SCRIPTS = os.getenv("RUN_MIGRATION_SCRIPTS", "false")

MAJOR_VERSION = os.getenv("MAJOR_VERSION", "50")
MINOR_VERSION = os.getenv("MINOR_VERSION", "latest")

with open("./control_planes.json", "r") as ff:
    endpoints = json.load(ff)

###################################################################
"""
GA plan Uploader
"""


###################################################################
async def check_successful_releases(cluster_id: str, url: str, username: str, password: str) -> bool:
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
        "status": "SUCCEEDED",
    }
    search_url = f"{url}/cc-ui/v1/clusters/{cluster_id}/deployments/search"
    async with aiohttp.ClientSession(
        timeout=aiohttp.ClientTimeout(total=900)
    ) as session:
        async with session.get(
            search_url, headers=headers, params=parameters
        ) as response:
            if response.status != 200:
                raise Exception(
                    f"Unable to fetch last 14 days of successful releases for cluster {cluster_id}: {response.status}: {await response.text()}"
                )
            data = await response.json()
            return len(data["content"]) > 0


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
                f"Failed to fetch cluster version for cluster name {cluster_name} cluster ID {cluster_id}: {response.status}: {await response.text()}"
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
            # f"terraform plan -out ./tfplan.json && terraform show -no-color -json ./tfplan.json > {TF_PLAN_FILE}",
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
    while True:
        async with session.post(url, headers=headers, json=body) as response:
            if response.status == 403:
                await asyncio.sleep(10)  # Wait for 10 seconds before retrying
                console.log(
                    f"A deployment is already in progress for {cluster_id} - {endpoint}/capc/{stack_name}/cluster/{cluster_id}/releases... Waiting for existing deployment to complete"
                )
                continue
            deployment_response = await response.json()
            deployment_id = deployment_response.get("id")
            release_url = f"{endpoint}/capc/{stack_name}/cluster/{cluster_id}/release-details/{deployment_id}"
            error_logs = deployment_response.get("errorLogs", None)
            if error_logs is not None:
                raise Exception(
                    f"Deployment failed for cluster {cluster_id} - {endpoint}/capc/{stack_name}/cluster/{cluster_id}/release-details/{deployment_id} : {error_logs}"
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
                                f"Failed to get deployment status for deployment ID {deployment_id}: {deployment_response.status}: {await deployment_response.text()}"
                            )
                        deployment_status = await deployment_response.json()
                        if status != deployment_status.get('status'):
                            console.log(
                                f"{deployment_status.get('status')} for {cluster_id} - {endpoint}/capc/{stack_name}/cluster/{cluster_id}/release-details/{deployment_id}"
                            )
                        status = deployment_status.get('status')
                        if status in ["SUCCEEDED", "FAILED"]:
                            break
                        await asyncio.sleep(
                            10
                        )  # Wait for 10 seconds before checking again
                return release_url


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


async def get_info(session: aiohttp.ClientSession, name: str, endpoint: dict) -> dict:
    """
    Fetch information about stacks, clusters, and versions.

    Args:
        session (aiohttp.ClientSession): The aiohttp client session.
        name (str): The name of the stack.
        endpoint (dict): The endpoint details containing URL and password.

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
                    }

    return stacks_clusters_versions


async def process_cluster(console: Console, stack_name: str, cluster: dict) -> tuple:
    """
    Process a cluster for deployment.

    Args:
        console (Console): The console object for logging.
        stack_name (str): The name of the stack.
        cluster (dict): The cluster details.

    Returns:
        tuple: A tuple containing stack_name, cluster_name, cluster_state, cluster_id, and cluster_version.
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
        )  # Create deployments for the cluster
        return (
            stack_name,
            cluster_name,
            cluster_state,
            cluster_id,
            cluster_version,
            plan_path,
            release_link,
        )


###################################################################
"""
GA plan Classifier
"""
###################################################################


def download_plan_from_s3(
    bucket_name: str, file_key: str, local_path: str, access_key: str, secret_key: str
) -> None:
    """
    Download a plan from S3 and store it locally.

    Args:
        bucket_name (str): The name of the S3 bucket.
        file_key (str): The key of the plan in the S3 bucket.
        local_path (str): The local path where the plan will be stored.
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
    except Exception as e:
        print(f"An error occurred while downloading the file s3://{bucket_name}/{file_key}: {e}")


def process_plan(planfile_path: str):
    with open(planfile_path, "r+") as ff:
        plans = json.load(ff)

    del_resources = {}
    del_recreated_resources = {}
    updated_changes = []
    name = "_".join(planfile_path.split("/")[:-2])

    for key, values in plans.items():
        if key == "resource_changes":
            for val in values:
                actions = val["change"]["actions"]
                after = val["change"]["after"]

                if "create" in actions and "delete" in actions:
                    if val["type"] != "null_resource":
                        if val["module_address"] not in del_recreated_resources:
                            del_recreated_resources[val["module_address"]] = []
                        # append to the list
                        del_recreated_resources[val["module_address"]].append(
                            val["type"]
                        )
                elif "delete" in actions:
                    if val["module_address"] not in del_resources:
                        del_resources[val["module_address"]] = []
                    if after is None:
                        del_resources[val["module_address"]].append(val["type"])
                elif "update" in actions:
                    if val["type"] != "scratch_string":
                        changes = get_updates(val)
                        if changes:
                            updated_changes.append(changes)

    updated_grouped_data = group_json(name, updated_changes)

    return (del_recreated_resources, del_resources, updated_grouped_data)


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
            "[cyan]Fetching stacks, clusters, and versions...", total=len(endpoints)
        )
        async with aiohttp.ClientSession(
            timeout=aiohttp.ClientTimeout(total=300)
        ) as session:
            tasks = [
                get_info(session, name, endpoint)
                for name, endpoint in endpoints.items()
            ]
            results = await asyncio.gather(*tasks)
            stacks_clusters_versions = {}
            for result in results:
                for stack_name, clusters in result.items():
                    stacks_clusters_versions[stack_name] = clusters
                progress.update(fetch_task, advance=1)

    uploader_table = Table(show_header=True, header_style="bold magenta")
    uploader_table.title = "Github Action Plan Uploader"
    uploader_table.add_column("Stack Name", style="dim", width=20, overflow="fold")
    uploader_table.add_column("Cluster Name", width=20, overflow="fold")
    uploader_table.add_column("Release Link", max_width=300, overflow="fold")
    uploader_table.add_column("status", width=20, overflow="fold")

    destroyed_table = Table(show_header=True, header_style="bold magenta")
    destroyed_table.title = "Github Action Plan Classifier for destroyed resources"
    destroyed_table.add_column("Stack Name", style="dim", width=20, overflow="fold")
    destroyed_table.add_column("Cluster Name", overflow="fold", width=20)
    destroyed_table.add_column("deleted_resources_addresss", max_width=100)
    destroyed_table.add_column("deleted_resources", max_width=100, overflow="fold")

    destroyed_recreated_table = Table(show_header=True, header_style="bold magenta")
    destroyed_recreated_table.title = (
        "Github Action Plan Classifier for destroyed and recreated resources"
    )
    destroyed_recreated_table.add_column(
        "Stack Name", style="dim", width=10, overflow="fold"
    )
    destroyed_recreated_table.add_column("Cluster Name", overflow="fold", width=10)
    destroyed_recreated_table.add_column(
        "deleted_and_recreated_resources_addresss", max_width=100, overflow="fold"
    )

    updated_table = Table(show_header=True, header_style="bold magenta")
    updated_table.title = "Github Action Plan Classifier for updated resources"
    updated_table.add_column("Stack Name", style="dim", width=10, overflow="fold")
    updated_table.add_column("Cluster Name", overflow="fold", width=10)
    updated_table.add_column("updated resource", max_width=100, overflow="fold")
    updated_table.add_column("Module Address", max_width=200, overflow="fold")

    cluster_tasks = [
        process_cluster(console, stack_name, cluster)
        for stack_name, clusters in stacks_clusters_versions.items()
        for cluster in clusters.values()
        if cluster.get("state") == "RUNNING"
        and cluster.get("version", {}).get("majorVersion", None)
        == int(STREAM_MAJOR_VERSION)
        and cluster.get("version", {}).get("tfStream", None) == "stage"
        or cluster.get("version", {}).get("tfStream", None) == "production"
        and cluster.get("version", {}).get("minorVersion", None) == "latest"
        and await check_successful_releases(
            cluster.get("id"), cluster.get("url"), cluster.get("username"), cluster.get("password")
        )
    ]

    processed_clusters = await asyncio.gather(*cluster_tasks)

    for (
        stack_name,
        cluster_name,
        cluster_state,
        cluster_id,
        cluster_version,
        plan_path,
        release_link,
    ) in processed_clusters:
        download_plan_from_s3(
            PLAN_BUCKET, plan_path, plan_path, AWS_ACCESS_KEY, AWS_SECRET_KEY
        )
        deleted_recreated, deleted, updated_changes = process_plan(plan_path)
        uploader_table.add_row(
            stack_name,
            cluster_name,
            release_link,
            "DONE",
            style="green",
        )
        for k, v in deleted.items():
            destroyed_table.add_row(
                stack_name, cluster_name, k, ",".join(vals for vals in v), style="red"
            )
        for k in deleted_recreated.keys():
            destroyed_recreated_table.add_row(
                stack_name, cluster_name, k, style="yellow"
            )

        for _, values in updated_changes.items():
            for kv, val in values.items():
                for v in val:
                    for x, y in v.items():
                        updated_table.add_row(
                            stack_name, cluster_name, kv, y
                        )

    console.print(uploader_table)
    console.print(destroyed_table)
    console.print(destroyed_recreated_table)
    console.print(updated_table)


if __name__ == "__main__":
    asyncio.run(main())
