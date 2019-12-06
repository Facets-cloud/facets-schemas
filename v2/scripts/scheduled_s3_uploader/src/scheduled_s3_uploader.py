import boto3
import datetime as dt
import logging
import os
import schedule
import stat
import sys
import time

from botocore.exceptions import ClientError
from pythonjsonlogger import jsonlogger

logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger()
logHandler = logging.StreamHandler()
formatter = jsonlogger.JsonFormatter()
logHandler.setFormatter(formatter)
logger.addHandler(logHandler)

BASE_DUMP_DIR = os.environ.get("DUMP_DIR", "/var/log/dumps")
S3_BUCKET = os.environ.get("S3_BUCKET", "k8s-file-dumps")
DEFAULT_FREQUENCY_MIN = 5

CLUSTER = os.environ.get("CLUSTER")
if CLUSTER is None:
    logger.error("cluster cannot be absent")
    sys.exit(1)

s3_client = boto3.client('s3')


def get_current_hour_folder():
    today = dt.datetime.today()
    return today.strftime("%Y-%m-%dH%H")


def file_age_in_minutes(pathname):
    """
    Return the age of file to the nearest minute
    """
    age_seconds = time.time() - os.stat(pathname)[stat.ST_MTIME]
    return round(age_seconds / 60)


def upload_file_to_s3(module, path):
    object_name = CLUSTER + "/" + module + "/" + get_current_hour_folder() + "/" + path.split("/")[-1]
    logger.info("writing file to path: {}".format(object_name))
    try:
        response = s3_client.upload_file(path, S3_BUCKET, object_name)
        logger.info("uploaded file successfully: {}".format(object_name))
    except ClientError as e:
        logger.error(e)
        return False
    return True


def filter_files_in_dir(dirpath):
    """
    Filter only files from the provided directory path
    """
    all_files = [dirpath + "/" + i for i in os.listdir(dirpath)]
    filtered_files = list(filter(os.path.isfile, all_files))
    return [i.split('/')[-1] for i in filtered_files]


def upload_module(module, path):
    # Filter the contents for files only
    logger.info("finding files to upload in the path: {}".format(path))
    files = filter_files_in_dir(path)
    logger.info("going to upload following files: {}".format(files))
    for file in files:
        filepath = path + "/" + file
        # Only upload file if the file was modified more than 5 minutes ago
        if not (file_age_in_minutes(filepath) > 5):
            logger.info("file less than 5 minutes old: {}".format(filepath))
            continue
        # Delete the file if the upload was successful
        is_upload_successful = upload_file_to_s3(module, filepath)
        if is_upload_successful:
            logger.info("removing file: {}".format(filepath))
            os.unlink(filepath)


def schedule_module_upload():
    # Clear all tasks tagged as 5-min-task
    schedule.clear('5-min-task')
    logger.info("cleared all 5 min tasks")
    for module in os.listdir(BASE_DUMP_DIR):
        path = BASE_DUMP_DIR + "/" + module
        # Add a schedule recurring every 5 mins for all directoris/modules
        if os.path.isdir(path):
            logger.info("scheduling 5 min task for module: {}".format(module))
            upload_module(module, path)
            schedule.every(DEFAULT_FREQUENCY_MIN).minutes.do(upload_module, module, path).tag("5-min-task")


def health_check():
    if not os.path.exists('/tmp/healthy'):
        logger.info("creating file /tmp/healthy")
        os.system('touch /tmp/healthy')

    current_timestamp = round(time.time())
    logger.info("adding health check entry for timestamp: {}".format(current_timestamp))
    with open('/tmp/healthy', 'a+') as fout:
        fout.write('{} - healthy\n'.format(current_timestamp))


schedule_module_upload()

# Check for any new modules every 15 minutes and refresh the list of schedules
schedule.every(15).minutes.do(schedule_module_upload).tag("15-min-task")
logger.debug("scheduled task for refreshing module list")
schedule.every(5).seconds.do(health_check).tag("health-check")
logger.debug("scheduled health check to run every 5 seconds")

while True:
    schedule.run_pending()
    time.sleep(1)
