#!/bin/bash

if [[ "$MOUNT_CIFS" = "true" ]] && [[ -n "$ADPASS" ]]
then
    echo "Trying to mount CIFS volume"
    SHARE_IP=`dig +short cp-nav-picklist-app-0.martjack.internal`

    echo -e "\nMounting CIFS share..."
    echo $ADPASS | mount -t cifs //$SHARE_IP/bulkupload/ /mnt/bulkupload/ -o username=testuser,uid=mjuser

else
    echo "MOUNT_CIFS was false, not mounting"
fi

echo $@
$@