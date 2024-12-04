import os
import json
import sys


def pretty_print_json(data):
    with open('schema-metadata.json', 'w') as file:
        json.dump(data, file, indent=4, sort_keys=True)


def find_module_json_files(directory):
    json_objects = []

    for root, dirs, files in os.walk(directory):
        for file in files:
            if file == 'module.json':
                with open(os.path.join(root, file), 'r') as f:
                    json_object = json.load(f)
                    json_objects.append(json_object)

    return json_objects


def filter_json_objects(json_objects):
    filtered_objects = []
    ignore_modules_list = [
        "kustomize",
        "argo_account",
        "karpenter_node_pool",
        "service_monitoring",
        "kubernetes_monitoring",
        "kubernetes_cluster",
        "network",
        "cloudflare_account",
        "postgres_monitoring",
        "ingress_monitoring",
        "redis_monitoring",
        "mongo_monitoring",
        "mysql_monitoring",
        "elasticsearch_monitoring",
        "mysql_web",
        "database_snapshot"
    ]

    for obj in json_objects:
        if 'input_type' in obj and obj['input_type'] == 'instance':
            if 'provides' in obj and obj['provides'] in ignore_modules_list:
                continue
            if 'disabled' not in obj or not obj['disabled']:
                filtered_objects.append(obj)
    return filtered_objects


def process_input_objects(input_objects):
    output_list = []

    for input_obj in input_objects:
        # Convert 'provides', 'flavors' and 'supported_clouds' to lowercase
        input_obj['provides'] = input_obj['provides'].lower()
        input_obj['flavors'] = [flavor.lower() for flavor in input_obj['flavors']]
        input_obj['supported_clouds'] = [cloud.lower() for cloud in input_obj['supported_clouds']]

        # Find matching output object based on 'provides' property
        matching_output_obj = next((item for item in output_list if item['intent'] == input_obj['provides']), None)

        if matching_output_obj:
            # Consider only the last flavor in the input object's flavors list
            last_input_flavor = input_obj['flavors'][-1]

            # Find matching output flavor
            matching_flavor = next(
                (flavor for flavor in matching_output_obj['flavors'] if flavor['name'][0] == last_input_flavor), None)

            if matching_flavor:
                # Concatenate clouds and versions, convert to set to remove duplicates, then convert back to list
                matching_flavor['clouds'] = list(set(matching_flavor['clouds'] + input_obj['supported_clouds']))
                matching_flavor['versions'] = list(set(matching_flavor['versions'] + [input_obj['version']]))
            else:
                # Create a new flavor and append it to the flavors list
                new_flavor = {
                    'name': [last_input_flavor],
                    'clouds': input_obj['supported_clouds'],
                    'versions': [input_obj['version']]
                }
                matching_output_obj['flavors'].append(new_flavor)

        else:
            # Create a new output object and append it to the output list
            new_output_obj = {
                'intent': input_obj['provides'],
                'flavors': [{
                    'name': [input_obj['flavors'][-1]],  # considering only the last flavor
                    'clouds': input_obj['supported_clouds'],
                    'versions': [input_obj['version']]
                }]
            }
            output_list.append(new_output_obj)
    return output_list


def add_additional_fields(output_objects):
    base_url = "https://facets-cloud.github.io/facets-schemas/schemas/"

    for output_obj in output_objects:
        # Adding additional fields to the output object
        output_obj['displayName'] = output_obj['intent'].capitalize()
        output_obj['description'] = "Some random description"
        output_obj['schemaUrl'] = f"{base_url}{output_obj['intent']}/{output_obj['intent']}.schema.json"
        output_obj['documentation'] = f"{base_url}{output_obj['intent']}/{output_obj['intent']}.schema.html"

        for flavor in output_obj['flavors']:
            # Adding additional fields to the flavors
            flavor['versions'] = [{"number": version, "documentation": "NA"} for version in flavor['versions']]
            flavor[
                'flavorSampleUrl'] = f"{base_url}{output_obj['intent']}/{output_obj['intent']}.{flavor['name'][0]}.sample.json"
    return output_objects


def generate_readme(input_json):
    # Load the JSON data
    # with open('your_json_file.json') as f:
    #     data = json.load(f)

    # Start the table with the headers
    table = "| Kind | Flavor | Version | Schema | Sample | Readme |\n|---|---|---|---|---|---|\n"
    # print(input_json)
    for entry in input_json:
        kind = entry['intent']
        schema_url = f"<{entry['schemaUrl']}>"
        for flavor in entry['flavors']:
            flavor_name = flavor['name'][0]
            flavor_sample_url = f"[Sample](schemas/{kind}/{kind}.{flavor_name}.sample.json)"
            readme_url = f"[Readme](schemas/{kind}/{kind}.schema.md)"
            # get the latest version number
            version_number = max(version['number'] for version in flavor['versions'])
            # Add the row to the table
            table += f"| {kind} | {flavor_name} | {version_number} | {schema_url} | {flavor_sample_url} | {readme_url} |\n"

        # Open the README file and replace the placeholder with the table
    with open('capillary-cloud-tf/docs/README.md', 'r') as file:
        filedata = file.read()

    # Replace the target string
    filedata = filedata.replace('<!-- TABLE_INSERT -->', table)

    # Write the file out again
    with open('capillary-cloud-tf/docs/README.md', 'w') as file:
        file.write(filedata)


def check_files_exist(json_data, base_folder="capillary-cloud-tf/docs/schemas"):
    errors = []
    for item in json_data:
        intent = item["intent"]

        schema_path = os.path.join(base_folder, intent, f"{intent}.schema.json")
        md_path = os.path.join(base_folder, intent, f"{intent}.schema.md")

        if not os.path.exists(schema_path):
            errors.append(f"Error: Schema File not found - {schema_path}")

        if not os.path.exists(md_path):
            errors.append(f"Error: Readme File not found - {md_path}")
        flavors = item.get("flavors", [])
        for flavor in flavors:
            flavor_name = flavor["name"][0]
            flavor_sample_url = flavor.get("flavorSampleUrl", "")

            if not flavor_sample_url:
                print(f"Warning: No flavorSampleUrl found for {intent}.{flavor_name}")
                continue

            # Extracting the file name from the URL
            file_name = os.path.basename(flavor_sample_url)

            doc_sample_path = os.path.join(base_folder, intent, file_name)

            if not os.path.exists(doc_sample_path):
                errors.append(f"Error: Flavor File not found - {doc_sample_path}")
    print('\n'.join(errors))
    sys.exit(1 if errors else 0)


all_modules = find_module_json_files("capillary-cloud-tf/modules/")
per_instance_modules = filter_json_objects(all_modules)
processed_data = process_input_objects(per_instance_modules)
final_data = add_additional_fields(processed_data)
generate_readme(final_data)
pretty_print_json(final_data)
check_files_exist(final_data)