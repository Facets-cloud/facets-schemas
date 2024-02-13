FROM 313496281593.dkr.ecr.us-east-1.amazonaws.com/facets-tf-runtime:9
RUN wget https://releases.hashicorp.com/terraform/1.5.7/terraform_1.5.7_linux_amd64.zip -P terraform_1.5.7
RUN unzip terraform_1.5.7/terraform_1.5.7_linux_amd64.zip -d terraform_1.5.7
RUN chmod +x terraform_1.5.7/terraform
RUN mv terraform_1.5.7/terraform terraform_1.5.7/terraform_1.5.7
RUN mv terraform_1.5.7/terraform_1.5.7 /usr/bin/terraform_1.5.7
RUN wget https://releases.hashicorp.com/terraform/1.0.11/terraform_1.0.11_linux_amd64.zip -P terraform_1.0.11
RUN unzip terraform_1.0.11/terraform_1.0.11_linux_amd64.zip -d terraform_1.0.11
RUN chmod +x terraform_1.0.11/terraform
RUN mv terraform_1.0.11/terraform terraform_1.0.11/terraform_1.0.11
RUN mv terraform_1.0.11/terraform_1.0.11 /usr/bin/terraform_1.0
RUN wget https://storage.googleapis.com/kubernetes-release/release/v1.21.0/bin/linux/amd64/kubectl -O kubectl1.21
RUN cp kubectl1.21 /usr/bin/kubectl1.21
RUN chmod +x /usr/bin/kubectl1.21
ENV ENABLE_CACHING="true"
COPY build-environments/terraform-capillary-cloud/scripts/run.py /run.py
COPY build-environments/terraform-capillary-cloud/tf-aws-cache-plugins /sources/primary/tf-aws-cache-plugins
COPY build-environments/terraform-capillary-cloud/tf-gcp-cache-plugins /sources/primary/tf-gcp-cache-plugins
COPY build-environments/terraform-capillary-cloud/tf-azure-cache-plugins /sources/primary/tf-azure-cache-plugins
RUN wget https://app.getambassador.io/download/tel2/linux/amd64/2.4.2/telepresence
RUN mv telepresence /usr/local/bin/telepresence
RUN chmod +x /usr/local/bin/telepresence
RUN pip3 install --no-cache-dir pykube-ng pytz google-api-python-client google-auth

RUN rm -rf /usr/local/bin/telepresence
RUN rm -rf terraform_1.0.11 terraform_1.0 kubectl1.21 terraform_1.5.7

RUN npm uninstall -g serverless
# RUN apt remove -y npm
# RUN apt autoremove -y

RUN apt-get remove -y openjdk-8-jdk
RUN apt autoremove -y
RUN rm -rf /opt/apache-maven-3.3.9/
RUN rm -rf /opt/maven
RUN rm -rf helm-v3.5.4-linux-amd64.tar.gz

RUN rm -rf /usr/bin/python3.6m /usr/bin/terraform_0.13
RUN rm -rf /usr/lib/jvm /usr/lib/python3.8 /usr/lib/python3.7

RUN rm -rf terraform_0.13 terraform_0.12.20_linux_amd64.zip terraform_1.0 terraform_1.0.11
