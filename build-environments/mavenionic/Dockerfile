FROM 486456986266.dkr.ecr.us-west-1.amazonaws.com/mavendocker:v1.4
RUN curl -sL https://deb.nodesource.com/setup_11.x | sudo -E bash -
RUN apt-get install -y nodejs
RUN apt-get install docker-ce=17.03.0~ce-0~ubuntu-xenial -y --allow-downgrades
RUN npm install -g ionic
