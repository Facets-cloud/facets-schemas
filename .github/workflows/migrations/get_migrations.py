import json
import os
import sys

class MigrationScript:
    def __init__(self, metadata, root):
        self.seq_no = metadata.get('seq_no')
        self.cloud = metadata.get('cloud')
        self.required_on = metadata.get('required_on')
        self.description = metadata.get('description', ' '.join([word.capitalize() for word in os.path.basename(root).split('_')[1:]]))
        self.downtime = metadata.get('downtime')

def get_migrations(directory):
    migrations = []
    for root, dirs, files in os.walk(directory):
        if 'metadata.json' in files:
            metadata_path = os.path.join(root, 'metadata.json')
            with open(metadata_path) as json_file:
                data = json.load(json_file)
                migration_script = MigrationScript(data, root)
                migrations.append(migration_script)
    return sorted(migrations, key=lambda x: x.seq_no, reverse=True)[:50]  # Accessing with dot notation

def json_content(migration_scripts):
    migration_content = []
    for migration_script in migration_scripts:
        info = {
            'sequenceNumber': migration_script.seq_no,
            'description': migration_script.description,
            'clouds': migration_script.cloud,
            'downtime': migration_script.downtime,
        }
        migration_content.append(info)
    return migration_content

if __name__ == '__main__':
    try:
        directory = sys.argv[1]
    except IndexError:
        print("No directory path provided, exiting")
        sys.exit(1)
    migrations = get_migrations(directory)
    content = json.dumps({"migrations": json_content(migrations)})
    with open('migrations.json', 'w') as f:
        f.write(content)