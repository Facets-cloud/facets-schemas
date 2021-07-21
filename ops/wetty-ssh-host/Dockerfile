FROM rastasheep/ubuntu-sshd:18.04
COPY setup.sh .
COPY startup.sh .
RUN ls -l
RUN /bin/bash setup.sh
CMD ["/bin/bash", "startup.sh"]