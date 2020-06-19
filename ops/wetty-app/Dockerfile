FROM krishnasrinivas/wetty:latest
RUN printf "Host * \n    StrictHostKeyChecking no" >> ~/.ssh/config
RUN printf "ServerAliveCountMax 30" >> ~/.ssh/config
RUN printf "ServerAliveInterval 60" >> ~/.ssh/config
RUN printf "LogLevel INFO" >> ~/.ssh/config