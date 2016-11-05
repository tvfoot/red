#!/usr/bin/env bash
# use curl to download the service-account.json from $SERVICE_ACCOUNT_URI, if set,
# to the path/filename set in $SERVICE_ACCOUNT.
if [[ $SERVICE_ACCOUNT && ${SERVICE_ACCOUNT} && $SERVICE_ACCOUNT_URI && ${SERVICE_ACCOUNT_URI} ]]
then
    echo "Service Account detected - downloading..."
    curl -L -o ${SERVICE_ACCOUNT} "${SERVICE_ACCOUNT_URI}"
else
    echo "ServiceAccount uri not set."
fi
