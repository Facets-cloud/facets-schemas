import os
import json
import sys


def find_module_files(directory):
    module_files = []
    for root, dirs, files in os.walk(directory):
        if 'module.json' in files:
            if 'capillary-cloud-tf/modules/2_deprecated' in root:
                continue
            module_file_path = os.path.join(root, 'module.json')
            module_files.append(module_file_path)
    return module_files


def read_module_file(module_file_path):
    with open(module_file_path) as f:
        module_data = json.load(f)
    return module_data


def versioning(directory_path):
    version_data = {}
    module_files = find_module_files(directory_path)
    for module_file in module_files:
        module_data = read_module_file(module_file)
        provides = module_data.get('provides')
        version = module_data.get('version')
        flavor = module_data.get('flavors')
        clouds = module_data.get("supported_clouds")
        lifecycle = module_data.get("lifecycle")
        input_type = module_data.get("input_type")
        disabled = module_data.get("disabled")
        if input_type == "config" or disabled is True:
            continue
        if provides not in version_data:
            version_data[provides] = {}

        if version in version_data[provides]:
            existing_flavor = next((f for f in version_data[provides][version]["flavors"] if f["clouds"] == clouds), None)
            if existing_flavor is not None:
                existing_flavor["versions"].append({"version": {"number": version , "documentation": "NA"}, "flavor": flavor, })
            else:
                version_data[provides][version]["flavors"].append({"clouds": clouds, "versions": [{"version": {"number": version , "documentation": "NA"}, "flavor": flavor, }]})
        else:
            version_data[provides][version] = {
                'path': module_file,
                'flavors': [{"clouds": clouds, "versions": [{"version": {"number": version , "documentation": "NA"}, "flavor": flavor, }]}]
            }
    return version_data

def print_error_messages(errors):
    # Print all error messages
    for error in errors:
        print(error)


def exit_code(errors):
    # Set exit code based on the presence of errors
    exit_code = 1 if errors else 0
    print(f"Exit Code for above error messages: {exit_code}")
    sys.exit(exit_code)


def check_files_exist(json_data, base_folder="capillary-cloud-tf/docs/schemas"):
    errors = []
    for item in json_data:
        intent = item["intent"]
        # Skip checking for specific intents
        if intent == "kubernetes_node_pool" or intent == "ingress":
            continue

        schema_path = os.path.join(base_folder, intent, f"{intent}.schema.json")
        md_path = os.path.join(base_folder, intent, f"{intent}.schema.md")

        if not os.path.exists(schema_path):
            errors.append(f"Error: Schema File not found - {schema_path}")

        if not os.path.exists(md_path):
            errors.append(f"Error: Readme File not found - {md_path}")

    print_error_messages(errors)
    exit_code(errors)


def flavor_sample_exists(json_data, doc_folder="capillary-cloud-tf/docs/", base_folder="schemas"):
    errors = []
    for item in json_data:
        intent = item["intent"]
        # Skip checking for specific intents
        if intent == "kubernetes_node_pool" or intent == "ingress":
            continue

        flavors = item.get("flavors", [])

        for flavor in flavors:
            flavor_name = flavor["name"][0]
            flavor_sample_url = flavor.get("flavorSampleUrl", "")

            if not flavor_sample_url:
                print(f"Warning: No flavorSampleUrl found for {intent}.{flavor_name}")
                continue

            # Extracting the file name from the URL
            file_name = os.path.basename(flavor_sample_url)

            sample_path = os.path.join(base_folder, intent, file_name)
            doc_sample_path = os.path.join(doc_folder, sample_path)

            if not os.path.exists(doc_sample_path):
                errors.append(f"Error: Flavor File not found - {doc_sample_path}")

    print_error_messages(errors)
    exit_code(errors)



if __name__ == '__main__':
    
    full_data = {}
    # Map to handle current folder naming discrepancies
    provides_mapping = {
        "ingress": "loadbalancer/loadbalancer",
        "kubernetes_node_pool": "nodepool/nodepool"
    }
    directory_path = 'capillary-cloud-tf/modules/'
    version_data = versioning(directory_path)
    for provides, versions in version_data.items():
        for version, module_data in versions.items():
            data = {}
            data["intent"] = provides
            data["displayName"] = provides.capitalize()
            data["description"] = "Some random description"
            # Check if provides value exists in the provides_mapping
            if provides in provides_mapping:
                url_value = provides_mapping[provides]
            else:
                url_value = f"{provides}/{provides}"
            # Build the schemaUrl
            data["schemaUrl"] = f"https://facets-cloud.github.io/facets-schemas/schemas/{url_value}.schema.json"
            data["documentation"] = "Documentation link"
            data["flavors"] = []
            print(json.dumps(module_data["flavors"], indent=4))
            for flavor in module_data["flavors"]:
                for f in flavor["versions"]:
                    transformed_clouds = []
                    transformed_flavors = f['flavor'][:]
                    if len(f['flavor']) > 1 and 'default' in f['flavor']:
                        transformed_flavors.remove('default')
                    for cloud in flavor["clouds"]:
                        # To handle cloud inconsistencies
                        if cloud == "all" or cloud == "any":
                            transformed_clouds = ["gcp", "aws", "azure"]
                            break
                        else:
                            transformed_clouds.append(cloud.lower())
                    flavors = {
                        "name": f["flavor"],
                        "clouds": list(set(transformed_clouds)),
                        "versions": [f["version"]],
                        "flavorSampleUrl" : f"https://facets-cloud.github.io/facets-schemas/schemas/{url_value}.{transformed_flavors[0]}.sample.json"

                    }
                    data["flavors"].append(flavors)
            
            if provides not in full_data:    
                full_data[provides] = data
            else:
                print(data["flavors"])
                existing_data = full_data[provides]
                for flavor in data["flavors"]:
                    existing_flavor = next((f for f in existing_data["flavors"] if f["name"] == flavor["name"]), None)
                    if existing_flavor is None:
                        existing_data["flavors"].append(flavor)
                    else:
                        print(flavor)
                        existing_flavor["versions"].extend(cloud for cloud in flavor["versions"] if cloud not in existing_flavor["versions"])
                        existing_flavor["clouds"].extend(cloud for cloud in flavor["clouds"] if cloud not in existing_flavor["clouds"])


    full_data_list = [v for _,v in full_data.items()]
    # print(json.dumps(full_data_list, indent=4))
    with open('schema-metadata.json', 'w') as f:
        json.dump(full_data_list, f, indent=4)

    input_json_path = 'schema-metadata.json'

    with open(input_json_path, 'r') as json_file:
        json_data = json.load(json_file)

    check_files_exist(json_data)
    flavor_sample_exists(json_data)
