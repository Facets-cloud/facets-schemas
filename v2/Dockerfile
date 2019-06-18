FROM 486456986266.dkr.ecr.us-west-1.amazonaws.com/ecommerce/openjdk8-with-newrelic:5ad24aa
EXPOSE 8080
COPY target/ /application/
COPY charts/ /charts/
WORKDIR /application
CMD java -jar *.jar
