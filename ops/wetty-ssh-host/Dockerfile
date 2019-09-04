FROM rastasheep/ubuntu-sshd:latest
RUN useradd -p $(openssl passwd -1 capk8s) capk8s -m -s /bin/bash
RUN apt-get update \
 && apt-get install -y --no-install-recommends \
    less jq python2.7 curl python-dateutil
RUN mkdir /usr/share/s3cmd \
 && curl -O -L https://github.com/s3tools/s3cmd/releases/download/v2.0.2/s3cmd-2.0.2.tar.gz
RUN tar xzf s3cmd-2.0.2.tar.gz
RUN cd s3cmd-2.0.2 \
 && cp -R s3cmd S3 /usr/share/s3cmd
RUN ln -s /usr/share/s3cmd/s3cmd /usr/bin/s3cmd
RUN chown capk8s -R /home/capk8s
