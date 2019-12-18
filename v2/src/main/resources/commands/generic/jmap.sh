#!/bin/bash

JAVA_PROCESS_ID=$(jps | grep -v -i jps | cut -d' ' -f1)
HEAPDUMP_TIMESTAMP=$(date +%s)
FILENAME=/var/log/dumps/heapdump-$HEAPDUMP_TIMESTAMP.hprof
jmap -dump:format=b,file=$FILENAME $JAVA_PROCESS_ID &
echo "dumping heapdump with $FILENAME"
