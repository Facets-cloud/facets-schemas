import os
import json


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
        if input_type == "config" :
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
