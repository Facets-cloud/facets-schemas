FROM ubuntu:18.04
RUN apt-get update
RUN apt-get install wget unzip openssh-client python-pip -y
RUN pip install awscli
COPY jdk-6u45-linux-x64.bin /
RUN chmod +x /jdk-6u45-linux-x64.bin
RUN /jdk-6u45-linux-x64.bin
RUN mkdir /opt/jdk
RUN mv /jdk1.6.0_45 /opt/jdk/jdk1.6.0_45
RUN wget http://archive.apache.org/dist/maven/binaries/apache-maven-2.2.1-bin.zip
RUN unzip apache-maven-2.2.1-bin.zip -d /opt/maven
RUN mkdir /root/.m2
ENV PATH /opt/maven/apache-maven-2.2.1/bin/:/opt/jdk/jdk1.6.0_45/bin:$PATH
ENV JAVA_HOME /opt/jdk/jdk1.6.0_45/
COPY run.sh /
RUN mkdir -p /root/.ssh
RUN touch /root/.ssh/known_hosts
COPY docker_install.sh /
RUN bash /docker_install.sh
COPY settings.xml /root/.m2/
ENTRYPOINT ["/bin/bash", "/run.sh"]
