FROM maven:3.8.1-jdk-11

COPY settings.xml root/.m2/settings.xml

RUN { echo '#!/bin/sh'; echo 'set -e'; echo; echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; } > /usr/local/bin/docker-java-home  \
	&& chmod +x /usr/local/bin/docker-java-home

RUN apt-get update && apt-get install -y openjfx
RUN apt-get update && apt-get install -y kmod

RUN curl "https://s3.amazonaws.com/aws-cli/awscli-bundle-1.16.96.zip" -o "awscli-bundle.zip"
RUN unzip awscli-bundle.zip
RUN ./awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws

COPY docker_install.sh .
RUN sh docker_install.sh
ENV PATH=/usr/local/openjdk-11/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/root/.local/bin
RUN mkdir /root/.ssh
RUN ssh-keyscan mvnrepo.capillary.co.in >> /root/.ssh/known_hosts
CMD ["/bin/bash"]
RUN ssh-keyscan mvnrepo.capillary.co.in > /root/.ssh/known_hosts
