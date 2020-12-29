FROM ubuntu:18.04
RUN apt-get update 
RUN apt-get install -y wget unzip curl
RUN wget https://get.helm.sh/helm-v2.16.1-linux-amd64.tar.gz
RUN wget https://storage.googleapis.com/kubernetes-release/release/v1.14.1/bin/linux/amd64/kubectl
RUN wget https://releases.hashicorp.com/terraform/0.12.20/terraform_0.12.20_linux_amd64.zip
RUN unzip terraform_0.12.20_linux_amd64.zip
RUN chmod +x terraform
RUN chmod +x ./kubectl
RUN mv ./kubectl /usr/bin/
RUN mv ./terraform /usr/bin/
RUN tar -xf helm-v2.16.1-linux-amd64.tar.gz
RUN chmod +x linux-amd64/helm
RUN mv ./linux-amd64/helm /usr/bin/
RUN apt-get install -y mysql-utilities mysql-client npm python-pip jq
RUN npm install -g serverless@1.75.1
RUN pip install awscli
RUN pip install requests
RUN apt-get install perl -y
RUN apt-get install libmysql-diff-perl -y
RUN apt-get install zip git -y
COPY scripts/mysql-schema-diff/Diff.pm  /usr/share/perl5/MySQL/Diff.pm
RUN apt-get install -y python3 python3-pip
RUN pip3 install requests bcrypt jsonmerge python-hcl2==2.0.0
