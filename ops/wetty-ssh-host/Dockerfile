FROM rastasheep/ubuntu-sshd:latest
COPY setup.sh .
RUN ls -l
RUN /bin/bash setup.sh
CMD [ "mkdir -p /mnt","&&","chown -R capk8s:capk8s /mnt/", "&&" ,"/usr/sbin/sshd", "-D", "-e"]