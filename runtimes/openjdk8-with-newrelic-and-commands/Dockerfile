FROM 486456986266.dkr.ecr.us-west-1.amazonaws.com/ecommerce/openjdk8-with-newrelic:5ad24aa
COPY commands/generic/ /commands/generic/
RUN apt-get update && apt-get install openjdk-8-jdk -y
RUN chmod +x -R /commands/generic/*
