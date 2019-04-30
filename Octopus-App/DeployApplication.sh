#!/bin/bash
####################################################
# Stop the chox tomcat server                      #
# Remove the old chox APP Application              #
# Install the new chox Application                 #
# Fix the ownership on deployed components         #
# Start the chox tomcat                            #
####################################################

HOST=`hostname`

echo "Deploying CHOX application to "${HOST}": "`date`
echo "Current working directory: "`pwd`

if [ -f profile ]; then
    cp profile /home/chox/.profile
    chown -R chox:chox /home/chox/.profile
    echo '.profile file installed'
fi

## Stop existing service
if [ `ps auxww|grep tomcat|grep chox|grep -v grep|wc -l` -gt 0 ]
then
    echo 'Stopping tomcat....'
    su - chox -c '/opt/tomcat/bin/shutdown.sh 30'
    /bin/sleep 5
    echo 'Done.'
fi
if [ `ps auxww|grep tomcat|grep chox|grep -v grep|wc -l` -gt 0 ]
then
    echo 'Tomcat still running - force stopping...'
    su - chox -c '/opt/tomcat/bin/shutdown.sh 30 -force'
    /bin/sleep 5
    echo 'Done.'
fi

if [ `ps auxww|grep tomcat|grep chox|grep -v grep|wc -l` -gt 0 ]
then
echo 'Tomcat still running - killing...'
for pid in `ps auxwww|grep tomcat|grep chox|grep -v grep|tr -s ' '|cut -d ' ' -f2`
do
    su - chox -c '/bin/kill -9 ${pid} 2>&1 > /dev/null'
    echo "Killed tomcat with pid ${pid}"
    sleep 2
done
fi

if [ `ps auxww|grep tomcat|grep chox|grep -v grep|wc -l` -gt 0 ]
then
    echo 'Tomcat still running - aborting...'
    exit 1
fi

## Create Application structure in deployment location
if [ ! -d /home/chox/bin ]; then
    mkdir /home/chox/bin
    echo '/home/chox/bin directory created'
else
    echo '/home/chox/bin already exists - emptying'
    /bin/rm -rf /home/chox/bin/*
fi
if [ ! -d /home/chox/logs ]; then
    mkdir /home/chox/logs
    echo '/home/chox/logs directory created'
else
    echo '/home/chox/logs already exists'
fi
if [ ! -d /home/chox/logs/processed ]; then
    mkdir /home/chox/logs/processed
    echo '/home/chox/logs/processed directory created'
else
    echo '/home/chox/logs/processed already exists'
fi
if [ -d /home/chox/reports ]; then
#    /bin/rm -rf /home/chox/reports
    echo 'Disabled: /home/chox/reports directory removed'
fi
if [ ! -d /home/chox/tomcat/chox_9080/temp ]; then
    mkdir /home/chox/tomcat/chox_9080/temp
    echo '/home/chox/tomcat/chox_9080/temp directory created'
else
    echo '/home/chox/tomcat/chox_9080/temp already exists'
fi
if [ ! -d /home/chox/tomcat/chox_9080/webapps ]; then
    mkdir /home/chox/tomcat/chox_9080/webapps
    echo '/home/chox/tomcat/chox_9080/webapps directory created'
else
    echo '/home/chox/tomcat/chox_9080/webapps already exists'
fi
if [ ! -d /home/chox/tomcat/chox_9080/logs ]; then
    mkdir /home/chox/tomcat/chox_9080/logs
    echo '/home/chox/tomcat/chox_9080/logs directory created'
else
    echo '/home/chox/tomcat/chox_9080/logs already exists'
fi
if [ ! -d /home/chox/tomcat/chox_9080/logs/old ]; then
    mkdir /home/chox/tomcat/chox_9080/logs/old
    echo '/home/chox/tomcat/chox_9080/logs/old directory created'
else
    echo '/home/chox/tomcat/chox_9080/logs/old already exists'
fi

##
## Clear tomcat logs for non-prod environments
##
if [ "#{ENV_PREFIX}" != "prd" ]; then
    rm -rf /home/chox/tomcat/chox_9080/logs/*
    echo 'Tomcat log directory cleared'
fi

##
## Copy Application components to deployment location
##
if [ -d bin ]; then
    cp -a bin/* /home/chox/bin/
    echo "bin contents installed"
fi
if [ -d tomcat/ROOT ]; then
    if [ -d /home/chox/tomcat/chox_9080/webapps/ROOT ]; then
        rm -rf /home/chox/tomcat/chox_9080/webapps/ROOT
        echo '/home/chox/tomcat/chox_9080/webapps/ROOT removed'
    else
        echo '/home/chox/tomcat/chox_9080/webapps/ROOT does not yet exist'
    fi
    cp -R tomcat/ROOT /home/chox/tomcat/chox_9080/webapps
    echo "Tomcat ROOT app installed"
fi
if [ -f tomcat/server.xml ]; then
    cp tomcat/server.xml /home/chox/tomcat/chox_9080/conf/server.xml
    echo '/home/chox/tomcat/chox_9080/conf/server.xml installed'
fi
if [ -f tomcat/context.xml ]; then
    cp tomcat/server.xml /home/chox/tomcat/chox_9080/conf/context.xml
    echo '/home/chox/tomcat/chox_9080/conf/context.xml installed'
fi
if [ -f tomcat/tomcat-users.xml ]; then
    cp tomcat/tomcat-users.xml /home/chox/tomcat/chox_9080/conf/tomcat-users.xml
    echo '/home/chox/tomcat/chox_9080/conf/tomcat-users.xml installed'
fi
if [ -f tomcat/tomcat-users.xsd ]; then
    cp tomcat/tomcat-users.xsd /home/chox/tomcat/chox_9080/conf/tomcat-users.xsd
    echo '/home/chox/tomcat/chox_9080/conf/tomcat-users.xsd installed'
fi
if [ -f pgpass ]; then
    cp pgpass /home/chox/.pgpass
    chmod 0600 /home/chox/.pgpass
    chown -R chox:chox /home/chox/.pgpass
    echo '.pgpass file installed'
fi

# Load crontab file if exists
if [ -f crontab.txt ]; then
    cp crontab.txt /home/chox/crontab.txt
    chown -R chox:chox /home/chox/crontab.txt
    if [[ ${HOST} =~ "app01" && ${HOST} =~ "-rdg-" ]]; then
        su - chox -c '/usr/bin/crontab crontab.txt'
        echo 'Crontab file installed'
    else
        echo 'Crontab not installed as not on app01 or not at rdg'
    fi
fi

## Fix Ownership & permissions
chown -R chox:chox /home/chox/*
chmod +x /home/chox/bin/*

sleep 10

## Start newly installed App
if [[ ${HOST} =~ "app01" && ${HOST} =~ "-rdg-" ]]; then
    echo 'Starting tomcat...'
    su - chox -c '/home/chox/bin/restartTomcat'
else
    echo 'Tomcat not started as not on app01 or not at rdg'
fi
echo "Finished deploying CHOX application: "`date`

exit 0
