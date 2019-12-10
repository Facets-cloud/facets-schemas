FROM 486456986266.dkr.ecr.us-west-1.amazonaws.com/mavendocker:v1.4
ARG SBT_VERSION=0.13.8
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion
COPY ./cap-sbt.json /root/.sbt/
COPY com.capillary.cap_sbt_plugins /root/.ivy2/local/com.capillary.cap_sbt_plugins
