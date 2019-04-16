FROM ubuntu:16.04
RUN apt-get update && apt-get install -y wget
RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
RUN python get-pip.py
RUN pip install awscli --upgrade --user
RUN wget -q https://packages.microsoft.com/config/ubuntu/16.04/packages-microsoft-prod.deb
RUN dpkg -i packages-microsoft-prod.deb
RUN apt-get -y install apt-transport-https
RUN apt-get update && apt-get -y install dotnet-sdk-2.1 
ADD ./docker_install.sh .
RUN sh docker_install.sh
COPY NuGet.Config  /root/.nuget/NuGet/
