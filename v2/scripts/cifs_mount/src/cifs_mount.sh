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
if [[ "$ENABLE_NEWRELIC" = "true" ]] ; then
    echo "Enabling newrelic"
    sed -i s/My\ Application/${CLUSTER}-${NEW_RELIC_APP_NAME}/g /usr/local/newrelic-netcore20-agent/newrelic.config
    sed -i s/REPLACE_WITH_LICENSE_KEY/$NEWRELIC_LICENSE_KEY/g /usr/local/newrelic-netcore20-agent/newrelic.config
else
    echo "Newrelic is disabled"
fi
echo $@
$@
