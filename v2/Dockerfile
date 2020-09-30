FROM 486456986266.dkr.ecr.us-west-1.amazonaws.com/ecommerce/openjdk8-with-newrelic-and-commands:1.0
EXPOSE 8080
COPY target/ /application/
COPY src/main/resources/charts/ /charts/
WORKDIR /application
CMD java -javaagent:/newrelic/newrelic.jar -Dnewrelic.config.license_key=${NEWRELIC_LICENSE_KEY} -Dnewrelic.config.app_name=${NEWRELIC_APP_NAME} -Xmx2048m -jar *.jar
#CMD java -Xmx2048m -jar *.jar