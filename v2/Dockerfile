FROM 486456986266.dkr.ecr.us-west-1.amazonaws.com/ecommerce/openjdk8-with-newrelic-and-commands:1.0
EXPOSE 8080
RUN npm cache clean -f
RUN npm install -g n stable
COPY target/ /application/
COPY src/main/resources/charts/ /charts/
WORKDIR /application
CMD java -jar *.jar
