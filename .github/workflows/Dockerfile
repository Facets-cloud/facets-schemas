FROM ubuntu as builder
RUN apt-get update
RUN apt-get install wget unzip xz-utils -y
RUN wget https://go.dev/dl/go1.21.6.linux-amd64.tar.gz
RUN tar -xvf go1.21.6.linux-amd64.tar.gz -C /usr/local
RUN wget https://bootstrap.pypa.io/get-pip.py
RUN wget https://releases.hashicorp.com/terraform/1.5.7/terraform_1.5.7_linux_amd64.zip
RUN unzip terraform_1.5.7_linux_amd64.zip
RUN wget https://dl.k8s.io/release/v1.29.3/bin/linux/amd64/kubectl

FROM ghcr.io/actions/actions-runner
ENV HOME="/home/runner"
# Update the package list
RUN sudo apt-get update
RUN sudo apt-get install zip libxml2-utils awscli git gh jq software-properties-common curl -y
RUN sudo add-apt-repository ppa:deadsnakes/ppa -y
RUN sudo apt-get install python3.12 -y
RUN sudo cp /usr/bin/python3.12 /usr/bin/python3
RUN sudo cp /usr/bin/python3.12 /usr/bin/python
#installing pip
COPY --from=builder get-pip.py ./get-pip.py
RUN sudo python3 get-pip.py
#installing golang
COPY --from=builder /usr/local/go /usr/local/go
ENV PATH="$PATH:/usr/local/go/bin:$HOME/go/bin"
#installing terraform
COPY --from=builder terraform /usr/local/bin/terraform
RUN sudo chown runner /usr/local/bin/terraform
#installing kubectl
COPY --from=builder kubectl /usr/local/bin/kubectl
RUN sudo chown runner /usr/local/bin/kubectl
#installing nodejs
RUN curl https://nodejs.org/dist/v21.7.2/node-v21.7.2-linux-x64.tar.xz | sudo tar -xJf - -C /opt/
ENV PATH="/opt/node-v21.7.2-linux-x64/bin:${PATH}"

RUN sudo chown runner /usr/local/bin/terraform /usr/local/bin/kubectl /opt/node-v21.7.2-linux-x64/bin/*
RUN sudo chmod u+x /usr/local/bin/terraform /usr/local/bin/kubectl /opt/node-v21.7.2-linux-x64/bin/*

#cleanup
RUN sudo apt-get clean && sudo rm -rf /var/lib/apt/lists/*