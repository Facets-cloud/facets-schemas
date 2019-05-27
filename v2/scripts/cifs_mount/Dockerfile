FROM mcr.microsoft.com/dotnet/core/sdk:2.1
WORKDIR /app

COPY src/* ./

RUN chmod -R 755 .
RUN ./create_bulkupload_folder.sh
RUN ./create_group_and_user.sh
RUN ./install_packages.sh

ENV CORECLR_ENABLE_PROFILING=1 \
CORECLR_PROFILER={36032161-FFC0-4B61-B559-F6C5D41BAE5A} \
CORECLR_NEWRELIC_HOME=/usr/local/newrelic-netcore20-agent \
CORECLR_PROFILER_PATH=/usr/local/newrelic-netcore20-agent/libNewRelicProfiler.so

ENTRYPOINT ["/bin/bash", "./cifs_mount.sh"]