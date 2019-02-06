FROM openjdk:8-jre
EXPOSE 8080
COPY target/*jar .
CMD java -jar *.jar