FROM openjdk:8-jre
RUN curl -O "http://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip"
RUN apt-get update
RUN apt-get install unzip -y
RUN unzip newrelic-java.zip
