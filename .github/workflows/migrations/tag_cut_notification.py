import boto3
import json
import os
import re
import requests
from get_migrations import get_migrations

def list_json_files_from_bucket(bucket_name, iac_stream):
    s3 = boto3.client('s3')
    response = s3.list_objects_v2(Bucket=bucket_name, Prefix=iac_stream)
    json_files = [obj['Key'] for obj in response.get('Contents', []) if obj['Key'].endswith('-migrations.json')]
    
    # Function to extract version number as a tuple
    def extract_version(file_name):
        match = re.search(r'(\d+)\.(\d+)\.(\d+)', file_name)
        return tuple(map(int, match.groups())) if match else (0, 0, 0)

    # Sorting by version number in reverse order
    json_files.sort(key=extract_version, reverse=True)
    
    return json_files

def read_json_file_from_s3(bucket_name, file_name):
    s3 = boto3.client('s3')
    response = s3.get_object(Bucket=bucket_name, Key=file_name)
    return json.loads(response['Body'].read())

def get_sequence_number_from_previous_tag(json_content):
    try:
        return json_content['migrations'][0]['sequenceNumber']
    except (IndexError, KeyError) as e:
        print(f"Error extracting sequence number: {e}")
        return None

def prepare_migration_message(migrations, previous_tag_sequence_number):
    messages = []
    for migration in migrations:
        if migration.seq_no > previous_tag_sequence_number:  # Removed cloud check
            message = f"*{migration.description}*\n"  # Title as description
            message += f"  - Allowed Cloud(s): {', '.join([str(cloud).upper() for cloud in migration.cloud])}\n" # Bullet point for cloud
            if migration.downtime:
                message += f"  - *This migration has a downtime associated with it.*\n"  # Bullet point for downtime
            messages.append(message)
    return messages

def send_slack_notification(migration_messages, iac_stream):
    tag = os.environ.get('CURRENT_TAG')
    slack_token = os.environ.get('SLACK_BOT_TOKEN')  # Get your Slack bot token from environment variable
    channel_ids_json = os.environ.get('SLACK_CHANNEL_IDS')  # Get JSON of Slack channel IDs from environment variable

    if not slack_token or not channel_ids_json:
        print("Slack bot token or channel IDs are not set.")
        return

    # Parse the channel IDs from JSON
    channel_ids = json.loads(channel_ids_json).values()  # Expecting a JSON array of channel IDs

    # Formatting the entire message for Slack
    message_text = f"*{iac_stream.capitalize()} IaC Stream – Tag Build Notification*\n"
    message_text += f"Tag *{tag}* is now available in the *{iac_stream}* IaC stream. "
    message_text += f"With this build, the following *Migrations* will be applied during the upcoming maintenance window, provided that the IaC is mapped to the latest version:\n" + "\n".join(migration_messages)
    message_text += f"\nTo *disable* automated updates, please *pin* your IaC version."

    # Loop through each channel and send a message
    for channel_id in channel_ids:
        payload = {
            "channel": channel_id,
            "text": message_text
        }

        # Sending the request to Slack API
        response = requests.post("https://slack.com/api/chat.postMessage", headers={
            "Authorization": f"Bearer {slack_token}",
            "Content-Type": "application/json"
        }, json=payload)

        if response.status_code == 200:
            print(f"Slack notification sent successfully to channel: {channel_id}")
        else:
            print(f"Failed to send notification to channel {channel_id}: {response.status_code} - {response.text}")

def main():
    bucket_name = os.environ.get('BUCKET_NAME')
    iac_stream = os.environ.get('IAC_STREAM')
    json_files = list_json_files_from_bucket(bucket_name, iac_stream)
    if not json_files:
        print("No JSON files found in the S3 bucket.")
        return

    # Get the last file
    last_file = json_files[0]  # First file after sorting in reverse order

    # Read and extract the first sequence number from the last file
    json_content = read_json_file_from_s3(bucket_name, last_file)
    previous_tag_highest_sequence_number = get_sequence_number_from_previous_tag(json_content)

    if previous_tag_highest_sequence_number is not None:
        print(f"The Previous Tag highest sequence number is: {previous_tag_highest_sequence_number}")

        # List all migrations
        migration_directory = os.path.join(os.environ.get('MIGRATION_SCRIPTS_PATH'))
        all_migrations = get_migrations(migration_directory)  # Retrieve all migrations

        # Prepare messages for migrations with a higher sequence number
        migration_messages = prepare_migration_message(all_migrations, previous_tag_highest_sequence_number)

        if migration_messages:
            print("Migrations with higher sequence numbers:")
            for message in migration_messages:
                print(message)

            # Send the notification to Slack
            send_slack_notification(migration_messages, iac_stream)  # Send the Slack notification for production
        else:
            print("No migrations with higher sequence numbers.")
    else:
        print("Could not retrieve the Previous Tag highest sequence number.")

if __name__ == "__main__":
    main()