#!/bin/bash

SetupDir=$1

if [ $TOMCAT_INSTALL"a" == "a" ]; then
   echo "Please set up $TOMCAT_INSTALL as the location of tomcat install"
   exit 1;
fi

if [ $SetupDir"a" == "a" ]; then
   echo "Usage: $0 <new foldername>"
   exit 1;
fi
 
mkdir -p $SetupDir
mkdir $SetupDir/bin
mkdir $SetupDir/conf
mkdir $SetupDir/logs
mkdir $SetupDir/webapps
mkdir $SetupDir/work
mkdir $SetupDir/temp

cp $TOMCAT_INSTALL/libexec/bin/tomcat-juli.jar $SetupDir/bin/
cp -r $TOMCAT_INSTALL/libexec/conf/* $SetupDir/conf/
ln -s $TOMCAT_INSTALL/libexec/lib  $SetupDir/lib

touch $SetupDir/bin/setenv.sh
echo "#!/bin/bash" >> $SetupDir/bin/setenv.sh
echo CATALINA_BASE=${PWD}/${SetupDir} >> $SetupDir/bin/setenv.sh 
echo CATALINA_HOME=\"${TOMCAT_INSTALL}/libexec\" >> $SetupDir/bin/setenv.sh  

echo "export CATALINA_HOME CATALINA_BASE" >> $SetupDir/bin/setenv.sh  
#echo "export CATALINA_OPTS='-Xms3G -Xmx6G'" >> $SetupDir/bin/setenv.sh
#add parameters for IntelliJ debugging     
echo "export CATALINA_OPTS='-Xms3G -Xmx6G -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9999'" >> $SetupDir/bin/setenv.sh

#echo "exec ${CATALINA_HOME}/bin/catalina start" >> $SetupDir/bin/setenv.sh


