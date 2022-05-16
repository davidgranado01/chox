#!/bin/bash
####################################################
# Install the new CHOX Batch Application           #
# Fix the ownership on deployed components         #
# Start the chox tomcat                            #
####################################################

HOST=`hostname`
SERVER_NUMBER=#{BATCH_SERVER_NUMBER}
SERVER_NAME=#{BATCH_SERVER_NAME}

echo "Deploying CHOX application to "${HOST}": "`date`
echo "Current working directory: "`pwd`

if [ -f profile ]; then
    cp profile /home/chox/.profile
    chown -R chox:chox /home/chox/.profile
    echo '.profile file installed'
fi


## Create Application structure in deployment location
if [ ! -d /home/chox/bin ]; then
    mkdir /home/chox/bin
    echo '/home/chox/bin directory created'
else
    echo '/home/chox/bin already exists'
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
if [ ! -d /home/chox/reports ]; then
    mkdir /home/chox/reports
    echo '/home/chox/reports directory created'
else
    echo '/home/chox/reports already exists'
fi
if [ ! -d /home/chox/reports/processed ]; then
    mkdir /home/chox/reports/processed
    echo '/home/chox/reports/processed directory created'
else
    echo '/home/chox/reports/processed already exists'
fi
if [ ! -d /home/chox/reports/excel ]; then
    mkdir /home/chox/reports/excel
    echo '/home/chox/reports/excel directory created'
else
    echo '/home/chox/reports/excel already exists'
fi


##
## Copy Application components to deployment location
##
if [ -d bin ]; then
    cp -a bin/* /home/chox/bin/
    echo "bin contents installed"
fi
if [ -d reports ]; then
    cp -a reports/* /home/chox/bin/
    echo "reports contents installed"
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
    if [[ ${HOST} =~ "${SERVER_NUMBER}" && ${HOST} =~ "${SERVER_NAME}" ]]; then
        su - chox -c '/usr/bin/crontab crontab.txt'
        echo 'Crontab file installed'
    else
        echo 'Crontab not installed as not on bat01 or not at hgt'
    fi
fi

## Fix Ownership & permissions
chown -R chox:chox /home/chox/*
chmod +x /home/chox/bin/*

echo "Finished deploying CHOX Batch application: "`date`

exit 0
