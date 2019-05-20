import logging
import sys
import time

from pythonjsonlogger import jsonlogger

logging.basicConfig(stream=sys.stdout, level=logging.DEBUG)
logger = logging.getLogger()
logHandler = logging.StreamHandler()
formatter = jsonlogger.JsonFormatter()
logHandler.setFormatter(formatter)
logger.addHandler(logHandler)

with open('/tmp/healthy', 'r') as fin:
    last_line = fin.readlines()[-1]

last_timestamp = int(last_line.split(' ')[0])
current_timestamp = round(time.time())
if current_timestamp - last_timestamp > 60:
    logger.debug('unhealthy - nothing logged in the last 1 minute. last timestamp: {}, current timestamp: {}'.format(
        last_timestamp,
        current_timestamp
    ))
    sys.exit(1)

logger.debug('healthy - last timestamp: {}, current timestamp: {}'.format(
    last_timestamp,
    current_timestamp
))

sys.exit(0)
