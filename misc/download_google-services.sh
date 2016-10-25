#!/usr/bin/env bash
# use curl to download the google-services.json from $GOOGLE_SERVICE_URI, if set,
# to the path/filename set in $GOOGLE_SERVICE.
if [[ $GOOGLE_SERVICE && ${GOOGLE_SERVICE} && $GOOGLE_SERVICE_URI && ${GOOGLE_SERVICE_URI} ]]
then
    echo "GoogleService detected - downloading..."
    curl -L -o ${GOOGLE_SERVICE} ${GOOGLE_SERVICE_URI}
else
    echo "GoogleService uri not set."
fi
