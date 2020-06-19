FROM krishnasrinivas/wetty:latest
RUN echo "Host *" >> ~/.ssh/config
RUN echo "StrictHostKeyChecking no" >> ~/.ssh/config
RUN echo "ServerAliveCountMax 30" >> ~/.ssh/config
RUN echo "ServerAliveInterval 60" >> ~/.ssh/config
RUN echo "LogLevel INFO" >> ~/.ssh/config
RUN cat ~/.ssh/config