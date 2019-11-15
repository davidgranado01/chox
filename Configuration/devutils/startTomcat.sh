#!/bin/bash

SetupDir=$1

if [ $SetupDir"a" == "a" ]; then
   echo "Usage: $0 <foldername>"
   exit 1;
fi

source $SetupDir/bin/setenv.sh
exec ${CATALINA_HOME}/bin/startup.sh
#exec ${CATALINA_HOME}/bin/catalina.sh start

ps -ef | grep tomcat
