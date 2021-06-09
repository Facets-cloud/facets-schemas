#!/bin/bash

export DEBIAN_FRONTEND=noninteractive
echo -e "\nInstalling smblclient..."
apt-get update && apt-get install -y -o Apt::Get::AllowUnauthenticated=true smbclient

echo -e "\nInstalling cifs-utils..."
apt-get update && apt-get install -y cifs-utils dnsutils tzdata
echo "installing newrelic"

wget -q https://download.newrelic.com/dot_net_agent/previous_releases/8.17.438.0/newrelic-netcore20-agent_8.17.438.0_amd64.deb && dpkg -i newrelic-netcore20-agent_8.17.438.0_amd64.deb
