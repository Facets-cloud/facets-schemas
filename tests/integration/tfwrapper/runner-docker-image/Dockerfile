FROM 313496281593.dkr.ecr.us-east-1.amazonaws.com/facets-tf-runtime:45
RUN pip3 install behave
RUN git clone https://github.com/cloud-custodian/tfparse.git
RUN wget https://go.dev/dl/go1.20.6.linux-amd64.tar.gz
RUN rm -rf /usr/local/go && tar -C /usr/local -xzf go1.20.6.linux-amd64.tar.gz
ENV PATH=$PATH:/usr/local/go/bin
RUN pip3 install ./tfparse
RUN pip3 install junit2html