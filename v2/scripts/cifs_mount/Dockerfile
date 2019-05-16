FROM mcr.microsoft.com/dotnet/core/sdk:2.1

WORKDIR /app

COPY src/* ./

RUN chmod -R 755 .
RUN ./create_bulkupload_folder.sh
RUN ./create_group_and_user.sh
RUN ./install_packages.sh

ENV ADPASS $ADPASS
ENV MOUNT_CIFS=false

ENTRYPOINT ADPASS=$ADPASS MOUNT_CIFS=$MOUNT_CIFS ./cifs_mount.sh