FROM rastasheep/ubuntu-sshd:latest
COPY setup.sh .
RUN ls -l
RUN /bin/bash setup.sh
CMD ["/usr/sbin/sshd", "-D", "-e"]