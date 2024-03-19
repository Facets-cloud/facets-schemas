from behave import *
import subprocess
import json
import os
import shutil
import tfparse
from jsonpath_ng import jsonpath, parse

@when(u'release script is run')
def run_release_script(context):
    trigger_script_path = "/sources/primary/capillary-cloud-tf/tfmain/scripts/trigger.sh"

    # Define the parameters
    param1 = "false"
    param2 = "CUSTOM"
    if context.release_type:
        param2 = context.release_type
    param3 = "false"

    try:
        context.stdout = ""
        context.stderr = ""
        custom_env = {}
        if context.custom_env:
            custom_env = context.custom_env
        # Run the script with the given parameters using bash
        completed_process = subprocess.run(['bash', trigger_script_path, param1, param2, param3], env=custom_env, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        context.stdout = completed_process.stdout.decode('utf-8')
        context.stderr = completed_process.stderr.decode('utf-8')
        completed_process.check_returncode()
        print("trigger.sh script executed successfully.")
        # print("stdout:")
        # print(context.stdout)
        # print("stderr:")
        # print(context.stderr)
        cloud_tf_provider = context.custom_env['CLOUD_TF_PROVIDER']
        context.tfparsed = tfparse.load_from_path(f"/sources/primary/capillary-cloud-tf/{cloud_tf_provider}/level2")
    except subprocess.CalledProcessError as e:
        print("stdout:")
        print(context.stdout)
        print("stderr:")
        print(context.stderr)
        print(f"Error executing the trigger.sh script: {e}")
    except FileNotFoundError:
        print(f"Could not find the trigger.sh script at {trigger_script_path}. Please check the path.")

@given('release type "{release_type}"')
def set_release_type(context, release_type):
    context.release_type = release_type

@given('allow destroy is "{allow_destroy}"')
def set_allow_destroy(context, allow_destroy):
    context.allow_destroy = allow_destroy.lower() == "true"

@given('script "{script_path}" is mocked')
def set_script_mocked(context, script_path):
    # Backup the script
    backup_path = script_path + ".bk"
    shutil.copy(script_path, backup_path)

    # Mock the script
    if script_path.endswith('.sh'):
        with open(script_path, 'w') as f:
            f.write('echo "invoked {}"\n'.format(script_path))
    elif script_path.endswith('.py'):
        with open(script_path, 'w') as f:
            f.write('print("invoked {}")\n'.format(script_path))

    # Save the information to a JSON file
    tracker_path = "/scripts-mocked-tracker.json"
    if not os.path.exists(tracker_path):
        data = {}
    else:
        with open(tracker_path, 'r') as f:
            data = json.load(f)
    data[script_path] = backup_path
    with open(tracker_path, 'w') as f:
        json.dump(data, f)

@given('binary "{binary_name}" is mocked')
def set_binary_mocked(context, binary_name):
    # Get the absolute path of the binary
    binary_path = shutil.which(binary_name)
    if binary_path is None:
        raise ValueError("Could not find binary: " + binary_name)

    # Backup the binary
    backup_path = binary_path + ".bk"
    shutil.copy(binary_path, backup_path)

    # Replace the binary with /bin/true
    shutil.copy('/bin/true', binary_path)

    # Save the information to a JSON file
    tracker_path = "/binaries-mocked-tracker.json"
    if not os.path.exists(tracker_path):
        data = {}
    else:
        with open(tracker_path, 'r') as f:
            data = json.load(f)
    data[binary_path] = backup_path
    with open(tracker_path, 'w') as f:
        json.dump(data, f)

@given('stack name is "{stack_name}"')
def set_stack(context, stack_name):
    context.stack_name = stack_name

    # Create directory if it doesn't exist
    dir_path = os.path.join('/sources/stack', stack_name)
    os.makedirs(dir_path, exist_ok=True)
    context.custom_env['STACK_NAME'] = stack_name
    context.custom_env['STACK_SUBDIRECTORY'] = stack_name
    with open(os.path.join(dir_path, 'stack.json'), 'w') as f:
        json.dump({}, f)

@given('cloud is "{cloud}"')
def set_cloud(context, cloud):
    context.cloud = cloud
    context.custom_env["CLOUD"] = cloud
    context.custom_env['CLOUD_TF_PROVIDER'] = 'tfmain'

@given('custom command is')
def custom_command(context):
    # Define the file path
    config_file_path = "/configs/custom.txt"

    # Create directory if it doesn't exist
    os.makedirs(os.path.dirname(config_file_path), exist_ok=True)

    # Write the multiline text to the file
    with open(config_file_path, 'w') as f:
        f.write(context.text)

@then('stdout includes lines')
def stdout_includes(context):
    assert context.stdout is not None
    expected_output_lines = context.text.splitlines()
    for line in expected_output_lines:
        assert line in context.stdout, f'Expected "{line}" in stdout, but was not found.'

@then('stderr includes lines')
def stderr_includes(context):
    assert context.stderr is not None
    expected_output_lines = context.text.splitlines()
    for line in expected_output_lines:
        assert line in context.stderr, f'Expected "{line}" in stderr, but was not found.'

@then('level2 modules must include "{module}" with source "{source}"')
def module_invoked(context, module, source):
    level2_modules = [i for i in context.tfparsed["module"]
                      if i["__tfmeta"]["filename"] == "main.tf" and
                      i["__tfmeta"]["path"] == module and
                      i["source"] == source]

    assert len(level2_modules) > 0, f"Expected {module} with source {source} to be in level2_modules, but it was not found."

@then('level2 modules must not include "{module}"')
def module_not_invoked(context, module):
    level2_modules = [i for i in context.tfparsed["module"]
                      if i["__tfmeta"]["filename"] == "main.tf" and
                      i["__tfmeta"]["path"] == module]

    assert len(level2_modules) == 0, f"Did not expect {module} to be in level2_modules, but it was found."

@given('a file exists in blueprint at "{path}" with content')
def create_blueprint_file(context, path):
    # Define the full path
    full_path = os.path.join('/sources/stack', context.stack_name, path)

    # Create the directory if it doesn't exist
    os.makedirs(os.path.dirname(full_path), exist_ok=True)

    # Write the content to the file
    with open(full_path, 'w') as f:
        f.write(context.text)

@given('override exists for "{resourceType}" named "{resourceName}" with content')
def set_override(context, resourceType, resourceName):
    # Define the path to the deployment context
    deployment_context_path = '/sources/deployment_context/deploymentcontext.json'

    # Load the current deployment context
    with open(deployment_context_path, 'r') as f:
        deployment_context = json.load(f)

    # Parse the new override
    new_override = json.loads(context.text)

    # Set the override in the deployment context
    if 'overrides' not in deployment_context:
        deployment_context['overrides'] = []
    deployment_context['overrides'] += [{"resourceName": resourceName,
                                        "resourceType": resourceType,
                                        "overrides": new_override}]

    # Save the updated deployment context
    with open(deployment_context_path, 'w') as f:
        json.dump(deployment_context, f, indent=4)

@given('a file exists in substack "{substack_name}" at "{path}" with content')
def substack_file(context, substack_name, path):
    # Create substack directory
    substack_dir = f'/sources/substack_{substack_name}'
    os.makedirs(substack_dir, exist_ok=True)

    # Handle relative path inside substack directory
    file_path = os.path.join(substack_dir, path)
    file_dir = os.path.dirname(file_path)

    # Ensure any necessary subdirectories exist
    os.makedirs(file_dir, exist_ok=True)

    # Get the content from the multi-line string
    content = context.text

    # Write the content to the file
    with open(file_path, 'w') as f:
        f.write(content)

    # Store the substack directory in the context's custom_env
    if "custom_env" not in context:
        context.custom_env = {}
    context.custom_env[f"CODEBUILD_SRC_DIR_SUBSTACK_{substack_name}"] = substack_dir

@given('an artifact exists in artifactory "{artifactory_name}" with name "{name}" artifactory_uri "{uri}" and build id "{build_id}"')
def add_artifact(context, artifactory_name, name, uri, build_id):
    json_file_path = '/sources/deployment_context/deploymentcontext.json'

    # Load the existing data from the JSON file
    with open(json_file_path, 'r') as json_file:
        data = json.load(json_file)

    # If 'artifactories' key doesn't exist, create it
    if 'artifacts' not in data:
        data['artifacts'] = {}

    # If artifactory_name key doesn't exist, create it
    if artifactory_name not in data['artifacts']:
        data['artifacts'][artifactory_name] = {}

    # Create/overwrite the 'name' key with the specified value
    data['artifacts'][artifactory_name][name] = {
        "artifactUri": uri,
        "buildId": build_id,
        "artifactory": artifactory_name,
    }

    # Write the updated data back to the JSON file
    with open(json_file_path, 'w') as json_file:
        json.dump(data, json_file, indent=4)

@then('the JSON file at "{file_path}" must contain "{expected_value}" at JSON path "{json_path}"')
def step_impl(context, file_path, expected_value, json_path):
    # Load the data from the JSON file
    with open(file_path, 'r') as json_file:
        data = json.load(json_file)

    # Parse the JSON path
    jsonpath_expr = parse(json_path)

    # Search for the value in the data
    matches = [match.value for match in jsonpath_expr.find(data)]

    if expected_value == '<EMPTY>':
        expected_value = ''

    # Assert that the expected value is in the results
    assert expected_value in matches, f'Expected value "{expected_value}" not found at JSON path "{json_path}" in file "{file_path}"'