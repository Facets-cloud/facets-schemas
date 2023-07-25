import os
import json
import shutil
import fnmatch
def before_scenario(context, scenario):
    replace_primary_directory()
    create_deployment_context()
    set_default_env_vars(context)

def after_scenario(context, scenario):
    reset_script_mocking()
    reset_binary_mocking()
    remove_stack_dir(context)
    reset_custom_commands()
    remove_substack_dirs()
def create_deployment_context():
    directory = '/sources/deployment_context'
    filename = 'deploymentcontext.json'
    # Create directory if it doesn't exist
    os.makedirs(directory, exist_ok=True)
    # Create an empty JSON file
    with open(os.path.join(directory, filename), 'w') as f:
        json.dump({
            "artifacts": {},
            "overrides": []
        }, f)
def reset_binary_mocking():
    print("Resetting binary mockings")
    tracker_path = "/binaries-mocked-tracker.json"
    if os.path.exists(tracker_path):
        with open(tracker_path, 'r') as f:
            data = json.load(f)
        for binary_path, backup_path in data.items():
            shutil.move(backup_path, binary_path)
        os.remove(tracker_path)

def reset_script_mocking():
    print("Resetting script mockings")
    tracker_path = "/scripts-mocked-tracker.json"
    if os.path.exists(tracker_path):
        with open(tracker_path, 'r') as f:
            data = json.load(f)
        for script_path, backup_path in data.items():
            shutil.move(backup_path, script_path)
        os.remove(tracker_path)

def set_default_env_vars(context):
    custom_env  = {}
    custom_env['CODEBUILD_SRC_DIR_DEPLOYMENT_CONTEXT'] = '/sources/deployment_context'
    custom_env['TF_VAR_cc_region'] = 'us-east-1'
    custom_env['CODEBUILD_SRC_DIR_STACK'] = '/sources/stack'
    custom_env['TF_BINARY_VERSION'] = "/usr/bin/terraform_1.0"
    context.custom_env = custom_env

def remove_stack_dir(context):
    dir_path = os.path.join('/sources/stack', context.stack_name)

    # Check if directory exists and remove it
    if os.path.exists(dir_path) and os.path.isdir(dir_path):
        shutil.rmtree(dir_path)

def replace_primary_directory():
    # Define the directory paths
    primary_dir = "/sources/primary"
    facets_dir = "/facets-iac"

    # Check if primary directory exists and remove it
    if os.path.exists(primary_dir) and os.path.isdir(primary_dir):
        shutil.rmtree(primary_dir)

    # Copy facets-iac directory to primary directory
    shutil.copytree(facets_dir, primary_dir)

def reset_custom_commands():
    # Define the file path
    config_file_path = "/configs/custom.txt"

    # Create directory if it doesn't exist
    os.makedirs(os.path.dirname(config_file_path), exist_ok=True)

    # Overwrite the file with an empty file
    open(config_file_path, 'w').close()

def remove_substack_dirs():
    base_dir = '/sources/'
    pattern = 'substack_*'

    # List all directories in the base_dir
    all_dirs = [os.path.join(base_dir, d) for d in os.listdir(base_dir) if os.path.isdir(os.path.join(base_dir, d))]

    # Match directories with pattern and remove them
    for directory in all_dirs:
        if fnmatch.fnmatch(directory, os.path.join(base_dir, pattern)):
            shutil.rmtree(directory)