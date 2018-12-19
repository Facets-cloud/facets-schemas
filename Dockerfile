FROM ubuntu
RUN apt-get update
RUN apt-get install openjdk-8-jdk maven -y
COPY target /deisdeployer/target
CMD java -jar /deisdeployer/target/*.jar
EXPOSE 8080
