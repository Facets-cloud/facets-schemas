FROM rastasheep/ubuntu-sshd:latest
RUN useradd -p $(openssl passwd -1 capk8s) capk8s -m
RUN apt-get update && apt-get install less s3cmd

