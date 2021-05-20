FROM rastasheep/ubuntu-sshd:18.04
COPY setup.sh .
RUN ls -l
RUN /bin/bash setup.sh
CMD ["/usr/sbin/sshd", "-D", "-e"]