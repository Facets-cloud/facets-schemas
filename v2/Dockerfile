FROM openjdk:8-jre
EXPOSE 8080
EXPOSE 9999
COPY target/*jar .
COPY charts /charts
CMD java -agentlib:jdwp=transport=dt_socket,server=y,address=9999,suspend=n -jar *.jar
