import json
import os
import sys

def get_migrations_info(directory):
    migrations_info = []
    for root, dirs, files in os.walk(directory):
        if 'metadata.json' in files:
            metadata_path = os.path.join(root, 'metadata.json')
            with open(metadata_path) as json_file:
                data = json.load(json_file)
                info = {
                    'sequenceNumber': data.get('seq_no'),
                    'description': data.get('description', ' '.join([word.capitalize() for word in os.path.basename(root).split('_')[1:]])),
                    'clouds': data.get('cloud'),
                    'downtime': data.get('downtime', False),
                }
                migrations_info.append(info)
    return sorted(migrations_info, key=lambda x: x['sequenceNumber'], reverse=True)[:50]

if __name__ == '__main__':
    try:
        directory = sys.argv[1]
    except IndexError:
        print("No directory path provided, exiting")
        sys.exit(1)
    content = json.dumps({"migrations": get_migrations_info(directory)})
    with open('migrations.json', 'w') as f:
        f.write(content)
