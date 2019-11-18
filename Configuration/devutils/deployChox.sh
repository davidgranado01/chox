#!/bin/bash

SetupDir=$1
CurrentDir=`pwd`
ROOTDir='ROOT'

if [ $SetupDir"a" == "a" ]; then
   echo "Usage: $0  <folder>"
   exit 1;
fi

if [ $CHOX_BUILD"a" == "a" ]; then
   echo "Please set up $CHOX_BUILD as the full path and filename to the chox war file"
   exit 1;
fi


source $SetupDir/bin/setenv.sh
cd $SetupDir/webapps
rm -rf $ROOTDir
mkdir $ROOTDir
cd $ROOTDir
cp $CHOX_BUILD $ROOTDir.war
jar -xvf $ROOTDir.war
rm $ROOTDir.war
cp WEB-INF/classes/application.properties WEB-INF/classes/application.properties.orig
cd  $CurrentDir

cp application.properties.local $SetupDir/webapps/$ROOTDir/WEB-INF/classes
cp application.properties.local $SetupDir/webapps/$ROOTDir/WEB-INF/classes/application.properties
