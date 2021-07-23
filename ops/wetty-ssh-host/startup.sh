#!/bin/bash

touch /tmp/startup
echo "Init"
#setfacl -m u:capk8s:---  /bin/gunzip
#setfacl -m u:capk8s:---  /bin/gzip
#setfacl -m u:capk8s:---  /bin/uncompress
#setfacl -m u:capk8s:---  /bin/setfacl

/usr/sbin/sshd -D -e