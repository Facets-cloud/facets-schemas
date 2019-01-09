FROM ubuntu
RUN apt-get update
RUN apt-get install openjdk-8-jdk maven -y
COPY src /deisdeployer/src
COPY pom.xml /deisdeployer
WORKDIR /deisdeployer
RUN ls -l
RUN mvn clean package
CMD java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n -jar /deisdeployer/target/*.jar
EXPOSE 8080 5005
