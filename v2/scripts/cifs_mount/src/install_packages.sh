#!/bin/bash

export DEBIAN_FRONTEND=noninteractive
echo -e "\nInstalling smblclient..."
apt-get update && apt-get install -y -o Apt::Get::AllowUnauthenticated=true smbclient

echo -e "\nInstalling cifs-utils..."
apt-get update && apt-get install -y cifs-utils

apt-get update && apt-get install dnsutils -y