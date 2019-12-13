FROM node:12.13.1-stretch
RUN apt-get update
RUN apt install apt-transport-https ca-certificates curl gnupg-agent software-properties-common -y
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian stretch stable"
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN apt-get update
RUN apt-get install docker-ce docker-ce-cli containerd.io -y
ENV NPM_TOKEN dc09dd6f-88b3-420b-99ff-25d8803c25c4
