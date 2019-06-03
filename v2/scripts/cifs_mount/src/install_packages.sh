#!/bin/bash

export DEBIAN_FRONTEND=noninteractive
echo -e "\nInstalling smblclient..."
apt-get update && apt-get install -y -o Apt::Get::AllowUnauthenticated=true smbclient

echo -e "\nInstalling cifs-utils..."
apt-get update && apt-get install -y cifs-utils

apt-get update && apt-get install tzdata dnsutils -y

wget -q https://download.newrelic.com/dot_net_agent/latest_release/newrelic-netcore20-agent_8.15.455.0_amd64.deb && dpkg -i newrelic-netcore20-agent_8.15.455.0_amd64.deb
