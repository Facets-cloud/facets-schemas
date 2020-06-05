FROM krishnasrinivas/wetty:latest
RUN printf "Host * \n    StrictHostKeyChecking no" > ~/.ssh/config
CMD ["sh", "-c", "yarn", "start", "--sshhost", "$WETTY_SSH_HOST"]