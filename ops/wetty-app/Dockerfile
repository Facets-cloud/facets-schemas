FROM krishnasrinivas/wetty:latest
RUN printf "Host * \n    StrictHostKeyChecking no" >> ~/.ssh/config
RUN sed -ri 's/#MaxStartups 10:30:100/MaxStartups 50:30:100/g' /etc/ssh/sshd_config
RUN sed -ri 's/#ClientAliveInterval 0/ClientAliveInterval 60/g' /etc/ssh/sshd_config
RUN sed -ri 's/#ClientAliveCountMax 3/ClientAliveCountMax 30/g' /etc/ssh/sshd_config
RUN sed -ri 's/#MaxSessions 10/MaxSessions 50/g' /etc/ssh/sshd_config
RUN sed -ri 's/#LogLevel INFO/LogLevel INFO/g' /etc/ssh/sshd_config
RUN sed -ri 's/#SyslogFacility AUTH/SyslogFacility AUTH/g' /etc/ssh/sshd_config
RUN sed -ri 's/#   StrictHostKeyChecking ask/StrictHostKeyChecking no/g' /etc/ssh/sshd_config
RUN printf "ServerAliveCountMax 30" >> /etc/ssh/ssh_config
RUN printf "ServerAliveInterval 60" >> /etc/ssh/ssh_config
RUN printf "LogLevel INFO" >> /etc/ssh/ssh_config