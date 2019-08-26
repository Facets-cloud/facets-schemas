FROM rastasheep/ubuntu-sshd:latest
RUN useradd -p $(openssl passwd -1 capk8s) capk8s -m
RUN apt-get update && apt-get install -y less s3cmd jq

