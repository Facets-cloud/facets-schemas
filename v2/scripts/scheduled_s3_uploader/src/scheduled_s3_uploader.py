import boto3
import datetime as dt
import logging
import os
import schedule
import stat
import sys
import time

from botocore.exceptions import ClientError


logging.basicConfig(stream=sys.stdout, level=logging.INFO)

BASE_DUMP_DIR = os.environ.get("DUMP_DIR", "/var/log/dumps")
S3_BUCKET = os.environ.get("S3_BUCKET", "k8s-file-dumps")
DEFAULT_FREQUENCY_MIN = 5

CLUSTER = os.environ.get("CLUSTER")
if CLUSTER is None:
    logging.error("cluster cannot be absent")
    sys.exit(1)

s3_client = boto3.client('s3')


def get_current_hour_folder():
    now = dt.datetime.now()
    return "{}-{}-{}H{}".format(now.year, now.month, now.day, now.hour)


def file_age_in_minutes(pathname):
    '''
    Return the age of file to the nearest minute
    '''
    age_seconds = time.time() - os.stat(pathname)[stat.ST_MTIME]
    return round(age_seconds / 60)


def upload_file_to_s3(module, path):
    object_name = CLUSTER + "/" + module + "/" + get_current_hour_folder() + "/" + path.split("/")[-1]
    logging.info("writing file to path: {}".format(object_name))
    try:
        response = s3_client.upload_file(path, S3_BUCKET, object_name)
        logging.info("uploaded file successfully: {}".format(object_name))
    except ClientError as e:
        logging.error(e)
        return False
    return True
    

def filter_files_in_dir(dirpath):
    '''
    Filter only files from the provided directory path
    '''
    all_files = [dirpath + "/" + i for i in os.listdir(dirpath)]
    filtered_files = list(filter(os.path.isfile, all_files))
    return [i.split('/')[-1] for i in filtered_files]


def upload_module(module, path):
    # Filter the contents for files only
    logging.info("finding files to upload in the path: {}".format(path))
    files = filter_files_in_dir(path)
    logging.info("going to upload following files: {}".format(files))
    for file in files:
        filepath = path + "/" + file
        # Only upload file if the file was modified more than 5 minutes ago
        if not (file_age_in_minutes(filepath) > 5):
            logging.info("file less than 5 minutes old: {}".format(filepath))
            continue
        # Delete the file if the upload was successful
        is_upload_successful = upload_file_to_s3(module, filepath)
        if is_upload_successful:
            logging.info("removing file: {}".format(filepath))
            os.unlink(filepath)


def schedule_module_upload():
    # Clear all tasks tagged as 5-min-task
    schedule.clear('5-min-task')
    logging.info("cleared all 5 min tasks")
    for module in os.listdir(BASE_DUMP_DIR):
        path = BASE_DUMP_DIR + "/" + module
        # Add a schedule recurring every 5 mins for all directoris/modules
        if os.path.isdir(path):
            logging.info("scheduling 5 min task for module: {}".format(module))
            upload_module(module, path)
            schedule.every(DEFAULT_FREQUENCY_MIN).minutes.do(upload_module, module, path).tag("5-min-task")


schedule_module_upload()

# Check for any new modules every 15 minutes and refresh the list of schedules
schedule.every(15).minutes.do(schedule_module_upload).tag("15-min-task")
logging.debug("scheduled task for refreshing module list")

while True:
    schedule.run_pending()
    time.sleep(1)
