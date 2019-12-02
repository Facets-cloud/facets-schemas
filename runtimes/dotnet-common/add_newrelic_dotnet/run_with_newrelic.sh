#!/bin/bash

if [[ "$ENABLE_NEWRELIC" = "true" ]] ; then
    echo "Enabling newrelic"
    sed -i s/My\ Application/${CLUSTER}-${NEW_RELIC_APP_NAME}/g /usr/local/newrelic-netcore20-agent/newrelic.config
    sed -i s/REPLACE_WITH_LICENSE_KEY/$NEWRELIC_LICENSE_KEY/g /usr/local/newrelic-netcore20-agent/newrelic.config
else
    echo "Newrelic is disabled"
fi
echo $@
$@
