import requests
import json
from jsonschema import RefResolver
import pandas as pd
from inflection import singularize
import re
# Function to resolve references in a schema
def resolve_refs(schema):
    if isinstance(schema, dict):
        if '$ref' in schema:
            with resolver.resolving(schema['$ref']) as resolved:
                return resolve_refs(resolved)
        else:
            return {k: resolve_refs(v) for k, v in schema.items()}
    elif isinstance(schema, list):
        return [resolve_refs(item) for item in schema]
    else:
        return schema

# Function to flatten the schema and extract properties
def flatten_schema(schema, path='', required_props=None):
    properties = {}
    if required_props is None:
        required_props = set()
    if 'required' in schema:
        required_props = required_props.union(set(schema['required']))
    if 'properties' in schema or 'patternProperties' in schema:
        props = schema.get('properties', {})
        props.update(schema.get('patternProperties', {}))
        for name, prop in props.items():
            new_path = path + '.' + name if path else name
            if isinstance(prop, dict):
                if 'properties' in prop or 'patternProperties' in prop:
                    properties.update(flatten_schema(prop, new_path, required_props))
                elif 'allOf' in prop:
                    for subschema in prop['allOf']:
                        properties.update(flatten_schema(subschema, new_path, required_props))
                else:
                    type_info = prop.get('type')
                    if 'enum' in prop:
                        type_info = 'enum: ' + ', '.join(map(str, prop['enum']))
                    if 'const' in prop:
                        type_info = 'const: ' + str(prop['const'])
                    description = prop.get('description', '') or prop.get('definition', '')
                    is_required = 'Yes' if new_path in required_props else 'No'
                    if new_path in properties:
                        properties[new_path][0] = type_info
                        properties[new_path][1] = description
                        properties[new_path][2] = is_required
                    else:
                        properties[new_path] = [type_info, description, is_required]
    elif 'allOf' in schema:
        for subschema in schema['allOf']:
            properties.update(flatten_schema(subschema, path, required_props))
    return properties

def flatten_schemanew(schema, path='', required_props=None):
    properties = {}
    if required_props is None:
        required_props = set()
    if 'required' in schema:
        required_props = required_props.union(set(schema['required']))
    if 'properties' in schema or 'patternProperties' in schema:
        props = schema.get('properties', {})
        pattern_props = schema.get('patternProperties', {})
        for name, prop in props.items():
            new_path = path + '.' + name if path else name
            if isinstance(prop, dict):
                if 'properties' in prop or 'patternProperties' in prop:
                    properties.update(flatten_schema(prop, new_path, required_props))
                elif 'allOf' in prop:
                    for subschema in prop['allOf']:
                        properties.update(flatten_schema(subschema, new_path, required_props))
                else:
                    type_info = prop.get('type')
                    if 'enum' in prop:
                        type_info = 'enum: ' + ', '.join(map(str, prop['enum']))
                    if 'const' in prop:
                        type_info = 'const: ' + str(prop['const'])
                    description = prop.get('description', '') or prop.get('definition', '')
                    is_required = 'Yes' if new_path in required_props else 'No'
                    if new_path in properties:
                        properties[new_path][0] = type_info
                        properties[new_path][1] = description
                        properties[new_path][2] = is_required
                    else:
                        properties[new_path] = [type_info, description, is_required]
        for pattern, prop in pattern_props.items():
            new_path = path + '.' + '<NAME>' if path else '<NAME>'
            if isinstance(prop, dict):
                if 'properties' in prop or 'patternProperties' in prop:
                    properties.update(flatten_schema(prop, new_path, required_props))
                elif 'allOf' in prop:
                    for subschema in prop['allOf']:
                        properties.update(flatten_schema(subschema, new_path, required_props))
                else:
                    type_info = prop.get('type')
                    if 'enum' in prop:
                        type_info = 'enum: ' + ', '.join(map(str, prop['enum']))
                    if 'const' in prop:
                        type_info = 'const: ' + str(prop['const'])
                    description = prop.get('description', '') or prop.get('definition', '')
                    is_required = 'Yes' if new_path in required_props else 'No'
                    if new_path in properties:
                        properties[new_path][0] = type_info
                        properties[new_path][1] = description
                        properties[new_path][2] = is_required
                    else:
                        properties[new_path] = [type_info, description, is_required]
    elif 'allOf' in schema:
        for subschema in schema['allOf']:
            properties.update(flatten_schema(subschema, path, required_props))
    return properties

def generate_reference(row):
    return f"${{{intent_name}.RESOURCE_NAME.{row.name}}}"

# Define a function to manipulate the string
def manipulate_property_name(property_name):
    segments = property_name.split('.')
    for i, segment in enumerate(segments):
        if re.search(r'[^a-zA-Z0-9_-]', segment):
            singular_form = singularize(segments[i - 1]).upper() + "_NAME"
            segments[i] = singular_form
    return '.'.join(segments)

# Load the schema metadata
metadata_url = 'https://facets-cloud.github.io/facets-schemas/schema-metadata.json'
metadata = requests.get(metadata_url).json()

# Initialize an empty list to store the links
links = []

for schema_info in metadata:
    # Skip if no schemaUrl is provided
    if 'schemaUrl' not in schema_info:
        print(f"Skipping {schema_info['intent']} due to missing schemaUrl")
        continue

    # Load the schema
    schema_url = schema_info['schemaUrl']
    try:
        schema = requests.get(schema_url).json()
    except Exception as e:
        print(f"Error loading schema for {schema_info['intent']}: {e}")
        continue

    # Create a resolver for the schema
    resolver = RefResolver(schema_url, schema)

    # Resolve references in the schema
    resolved_schema = resolve_refs(schema)

    # Flatten the resolved schema
    flat_schema = flatten_schema(resolved_schema)

    # Split the flat schema into schema properties and outputs
    schema_properties = {k: v for k, v in flat_schema.items() if not k.startswith('out')}
    outputs = {k: v for k, v in flat_schema.items() if k.startswith('out')}

    # Convert the schema properties and outputs to DataFrames
    df_schema_properties = pd.DataFrame(schema_properties.values(), index=schema_properties.keys(), columns=['Type', 'Description', 'Required'])
    df_outputs = pd.DataFrame(outputs.values(), index=outputs.keys(), columns=['Type', 'Description', 'Required'])

    df_outputs.index = df_outputs.index.map(manipulate_property_name)
    df_schema_properties.index = df_schema_properties.index.map(manipulate_property_name)
    intent_name = schema_info['intent']
    #df_outputs['Referencing'] = ""
    df_outputs['Referencing'] = df_outputs.apply(generate_reference, axis=1)

    # Write the DataFrames to a Markdown file
    filename = schema_info['intent'] + '.md'
    with open(filename, 'w') as f:
        f.write("# " + schema_info['intent'].replace('_', ' ').title() + "\n\n")
        f.write("## Schema Properties\n\n")
        f.write(df_schema_properties.to_markdown() + "\n\n")
        f.write("## Outputs\n\n")
        f.write(df_outputs.to_markdown() + "\n\n")
    print(f"Generated {filename}")

    # Convert the filename from snake case to title case
    title = ' '.join(word.capitalize() for word in schema_info['intent'].split('_'))

    # Add a link to the file to the list of links
    links.append(f"[{title}](./{filename})")

# Create the index.md file with links to each schema
with open('index.md', 'w') as f:
    f.write("# Index\n\n")
    for link in links:
        f.write(f"{link}\n\n")
