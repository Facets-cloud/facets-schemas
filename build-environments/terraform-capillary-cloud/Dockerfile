FROM ubuntu:18.04
RUN apt-get update 
RUN apt-get install -y wget unzip curl

RUN wget https://storage.googleapis.com/kubernetes-release/release/v1.14.1/bin/linux/amd64/kubectl
RUN chmod +x ./kubectl
RUN mv ./kubectl /usr/bin/

RUN wget https://releases.hashicorp.com/terraform/0.12.20/terraform_0.12.20_linux_amd64.zip
RUN unzip terraform_0.12.20_linux_amd64.zip
RUN chmod +x terraform
RUN mv ./terraform /usr/bin/

RUN wget https://get.helm.sh/helm-v3.5.4-linux-amd64.tar.gz
RUN tar -zxvf helm-v3.5.4-linux-amd64.tar.gz
RUN chmod +x linux-amd64/helm
RUN mv ./linux-amd64/helm /usr/bin/helm
RUN helm plugin install https://github.com/hickeyma/helm-mapkubeapis

RUN apt-get install -y mysql-utilities mysql-client npm python-pip jq
RUN npm install -g serverless@1.75.1
RUN pip install awscli
RUN pip install requests
RUN apt-get install perl -y
RUN apt-get install libmysql-diff-perl -y
RUN apt-get install zip git -y
COPY scripts/mysql-schema-diff/Diff.pm  /usr/share/perl5/MySQL/Diff.pm
RUN apt-get install -y python3 python3-pip
RUN pip3 install requests bcrypt jsonmerge python-hcl2==2.0.0 boto3 pystache pyyaml

# Install maven 3.3.9
RUN wget --no-verbose -O /tmp/apache-maven-3.3.9-bin.tar.gz http://www-eu.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz && \
    tar xzf /tmp/apache-maven-3.3.9-bin.tar.gz -C /opt/ && \
    ln -s /opt/apache-maven-3.3.9 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/local/bin  && \
    rm -f /tmp/apache-maven-3.3.9-bin.tar.gz
RUN apt-get install -y openjdk-8-jdk 

RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
ENV MAVEN_HOME /opt/maven

