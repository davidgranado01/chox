#!/bin/bash

SetupDir=$1

if [ $SetupDir"a" == "a" ]; then
   echo "Usage: $0 <foldername>"
   exit 1;
fi

. $SetupDir/bin/setenv.sh
exec ${CATALINA_HOME}/bin/shutdown.sh
#exec ${CATALINA_HOME}/bin/catalina.sh stop
