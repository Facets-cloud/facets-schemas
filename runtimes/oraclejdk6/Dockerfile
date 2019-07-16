FROM ubuntu:18.04
COPY jdk-6u45-linux-x64.bin .
RUN chmod +x jdk-6u45-linux-x64.bin
RUN ./jdk-6u45-linux-x64.bin
ENV PATH="jdk1.6.0_45/jre/bin/:${PATH}"
