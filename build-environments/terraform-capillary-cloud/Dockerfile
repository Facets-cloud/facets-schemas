FROM 313496281593.dkr.ecr.us-east-1.amazonaws.com/facets-tf-runtime:9

RUN wget https://releases.hashicorp.com/terraform/1.0.11/terraform_1.0.11_linux_amd64.zip -P terraform_1.0.11
RUN unzip terraform_1.0.11/terraform_1.0.11_linux_amd64.zip -d terraform_1.0.11
RUN chmod +x terraform_1.0.11/terraform
RUN mv terraform_1.0.11/terraform terraform_1.0.11/terraform_1.0.11
RUN mv terraform_1.0.11/terraform_1.0.11 /usr/bin/terraform_1.0

