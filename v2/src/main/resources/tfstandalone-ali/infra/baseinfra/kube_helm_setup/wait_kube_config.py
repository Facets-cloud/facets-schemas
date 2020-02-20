import os
import time
import sys

TIME_TEN_MINUTES = 10 * 60

start_time = round(time.time())
while True:
    current_time = round(time.time())
    time_difference = current_time - start_time
    if time_difference > TIME_TEN_MINUTES:
        print('waited for 10 minutes for the kube config file, cannot wait more!')
        sys.exit(1)

    kube_config_path = os.path.expanduser("~/.captf/kube/ali/config")
    if os.path.exists(kube_config_path):
        file_last_modified_time = round(os.path.getmtime(kube_config_path))
        file_last_modified_time_difference = current_time - file_last_modified_time
        if file_last_modified_time_difference < 30 and file_last_modified_time_difference > 15:
            print('found updated kube config')
            sys.exit(0)

    time.sleep(5)