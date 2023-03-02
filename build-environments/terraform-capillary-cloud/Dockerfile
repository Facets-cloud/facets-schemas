FROM 313496281593.dkr.ecr.us-east-1.amazonaws.com/facets-tf-runtime:9

RUN pip3 install --no-cache-dir pykube-ng pytz google-api-python-client google-auth
RUN wget https://releases.hashicorp.com/terraform/1.0.11/terraform_1.0.11_linux_amd64.zip -P terraform_1.0.11
RUN unzip terraform_1.0.11/terraform_1.0.11_linux_amd64.zip -d terraform_1.0.11
RUN chmod +x terraform_1.0.11/terraform
RUN mv terraform_1.0.11/terraform terraform_1.0.11/terraform_1.0.11
RUN mv terraform_1.0.11/terraform_1.0.11 /usr/bin/terraform_1.0
RUN wget https://storage.googleapis.com/kubernetes-release/release/v1.21.0/bin/linux/amd64/kubectl -O kubectl1.21
RUN cp kubectl1.21 /usr/bin/kubectl1.21
RUN chmod +x /usr/bin/kubectl1.21
