FROM ghcr.io/actions/actions-runner
# Update the package list
RUN sudo apt-get update

# Install zip, openjdk and maven
RUN sudo apt-get install -y zip libxml2-utils awscli

RUN sudo apt-get clean && sudo rm -rf /var/lib/apt/lists/*

#ENTRYPOINT ["/bin/bash", "-c"]
#CMD ["entrypoint-dind-rootless.sh"]