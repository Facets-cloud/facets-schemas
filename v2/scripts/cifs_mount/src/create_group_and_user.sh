#!/bin/bash

if ! getent group mjuser >/dev/null; then
  # Adding system group: mysql.
  addgroup --system mjuser >/dev/null
fi

# creating sdisovery user if he isn't already there
if ! getent passwd mjuser >/dev/null; then
  # Adding system user: mysql.
  adduser \
    --system \
    --disabled-login \
    --ingroup mjuser \
    --no-create-home \
    --home /home/mjuser \
    --gecos "Static services of mj" \
    --shell /bin/bash \
    mjuser  >/dev/null
fi
