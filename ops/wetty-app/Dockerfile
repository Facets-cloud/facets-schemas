FROM krishnasrinivas/wetty:latest
RUN printf "Host * \n    StrictHostKeyChecking no" >> ~/.ssh/config
RUN printf "ServerAliveCountMax 30" >> /etc/ssh/ssh_config
RUN printf "ServerAliveInterval 60" >> /etc/ssh/ssh_config
RUN printf "LogLevel INFO" >> /etc/ssh/ssh_config