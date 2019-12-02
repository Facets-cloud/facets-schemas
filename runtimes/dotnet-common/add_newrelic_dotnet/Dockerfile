FROM 486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/dotnetcore2p2:buildenv1.0
RUN wget -q https://download.newrelic.com/dot_net_agent/latest_release/newrelic-netcore20-agent_8.17.438.0_amd64.deb && dpkg -i newrelic-netcore20-agent_8.17.438.0_amd64.deb
ENV CORECLR_ENABLE_PROFILING=1 \
CORECLR_PROFILER={36032161-FFC0-4B61-B559-F6C5D41BAE5A} \
CORECLR_NEWRELIC_HOME=/usr/local/newrelic-netcore20-agent \
CORECLR_PROFILER_PATH=/usr/local/newrelic-netcore20-agent/libNewRelicProfiler.so
COPY run_with_newrelic.sh /app/run_with_newrelic.sh
ENTRYPOINT ["/bin/bash", "/app/run_with_newrelic.sh"]
