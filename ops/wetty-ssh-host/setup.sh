#!/bin/bash

apt-get update 
apt-get install -y --no-install-recommends less jq python2.7 curl python-dateutil
mkdir /usr/share/s3cmd
curl -O -L https://github.com/s3tools/s3cmd/releases/download/v2.0.2/s3cmd-2.0.2.tar.gz
tar xzf s3cmd-2.0.2.tar.gz
cd s3cmd-2.0.2
cp -R s3cmd S3 /usr/share/s3cmd
ln -s /usr/share/s3cmd/s3cmd /usr/bin/s3cmd
useradd -ms /bin/bash -p $(openssl passwd -1 capk8s) capk8s
chown capk8s -R /home/capk8s
ls -lrth /home
sed -ri 's/#MaxStartups 10:30:100/MaxStartups 50:30:100/g' /etc/ssh/sshd_config
sed -ri 's/#ClientAliveInterval 0/ClientAliveInterval 60/g' /etc/ssh/sshd_config
sed -ri 's/#ClientAliveCountMax 3/ClientAliveCountMax 30/g' /etc/ssh/sshd_config
sed -ri 's/#MaxSessions 10/MaxSessions 50/g' /etc/ssh/sshd_config
sed -ri 's/#LogLevel INFO/LogLevel VERBOSE/g' /etc/ssh/sshd_config
sed -ri 's/#SyslogFacility AUTH/SyslogFacility AUTH/g' /etc/ssh/sshd_config

echo "ServerAliveCountMax 30" >> /etc/ssh/ssh_config
echo "ServerAliveInterval 60" >> /etc/ssh/ssh_config
echo "LogLevel VERBOSE" >> /etc/ssh/ssh_config
cat /etc/ssh/sshd_config
cat /etc/ssh/ssh_config
