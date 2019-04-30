#!/bin/sh

log_dir="/home/chox/logs"
tomcat_temp_dir="/home/chox/tomcat/chox_9080/temp"
tomcat_log_dir="/home/chox/tomcat/chox_9080/logs"


echo '***********************************************************'
echo `date`': Daily Maintenenace'
echo '***********************************************************'
echo 'Removing log files over 7 days old from '${log_dir}
/usr/bin/find ${log_dir}/ -mtime +7 -type f -delete

echo 'Removing files over 1 days old from '${tomcat_temp_dir}
/usr/bin/find ${tomcat_temp_dir}/ -mtime +1 -type f -delete
echo 'Removing default tomcat log files over 7 days old from '${tomcat_log_dir}
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "catalina.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "host-manager.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "localhost.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "localhost_access_log.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "manager.*" -delete
echo '***********************************************************'

exit 0
