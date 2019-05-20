FROM openjdk:8-jre
EXPOSE 8080
COPY target/*jar .
COPY charts /
CMD java -jar *.jar
