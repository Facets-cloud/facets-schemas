#!/bin/bash

#######Basic tools
apt-get update 
apt-get install -y --no-install-recommends less jq python2.7 curl python-dateutil gnupg2 telnet openjdk-11-jre-headless

#######Additional installs
apt-get install -y vim libcurl4 mysql-client-5.7 redis-tools acl

###### mongo clients and tools #######
wget  https://repo.mongodb.org/apt/ubuntu/dists/bionic/mongodb-org/4.2/multiverse/binary-amd64/mongodb-org-tools_4.2.3_amd64.deb
wget https://repo.mongodb.org/apt/ubuntu/dists/bionic/mongodb-org/4.2/multiverse/binary-amd64/mongodb-org-shell_4.2.3_amd64.deb
dpkg -i mongodb-org-tools_4.2.3_amd64.deb
dpkg -i mongodb-org-shell_4.2.3_amd64.deb

##### cypher shell #####
wget https://dist.neo4j.org/cypher-shell/cypher-shell_4.0.6_all.deb
apt-get install -y openjdk-11-jre-headless
dpkg -i cypher-shell_4.0.6_all.deb

#######s3cmd
mkdir /usr/share/s3cmd
curl -O -L https://github.com/s3tools/s3cmd/releases/download/v2.0.2/s3cmd-2.0.2.tar.gz
tar xzf s3cmd-2.0.2.tar.gz
cd s3cmd-2.0.2
cp -R s3cmd S3 /usr/share/s3cmd
ln -s /usr/share/s3cmd/s3cmd /usr/bin/s3cmd
#######s3cmd

#######kubectl
curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl
kubectl version --client
######kubectl

#######capk8s user
useradd -ms /bin/bash -p $(openssl passwd -1 capk8s) capk8s
chown capk8s -R /home/capk8s
ls -lrth /home
echo "cd /var/log/efs" >> /home/capk8s/.bashrc
source /home/capk8s/.bashrc

#######capk8s user

######SSH configs
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
######SSH configs

