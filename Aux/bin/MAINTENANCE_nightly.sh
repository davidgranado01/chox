#!/bin/sh

log_dir="/home/chox/logs"
report_dir="/home/chox/reports"
tomcat_temp_dir="/home/chox/tomcat/chox_9080/temp"
tomcat_log_dir="/home/chox/tomcat/chox_9080/logs"

PSQL_COMMAND=/usr/bin/psql
HOST=#{DB_HOST} #dev-ukre-pgs28.dc.solera-uk.com
USER=#{DB_USER.CHOX} #chox
DB=#{DB_NAME.CHOX} #dev_uk_chox

echo '***********************************************************'
echo `date`': Daily Maintenenace'
echo '***********************************************************'
echo 'Updating 'password last modified date' for admin users....'
${PSQL_COMMAND}  -h ${HOST} -U ${USER} -d ${DB} -c "update web_user set password_last_modified_date=now(), is_expired=false where user_name ilike 'admin@%'"
${PSQL_COMMAND}  -h ${HOST} -U ${USER} -d ${DB} -c "update web_user set password_last_modified_date=now(), is_expired=false where user_name in ('erac_scheduler','easidrive_scheduler','paidInvoices.lv','claimmatcher.lv','acknowledgeClaims.dlg',ecdupdate@aiclaimssolutions.com','ecdupdate@erac.com')"

echo 'Removing log files over 7 days old from '${log_dir}
/usr/bin/find ${log_dir}/ -mtime +7 -type f -delete
echo 'Removing report files over 7 days old from '${report_dir}
/usr/bin/find ${report_dir}/ -mtime +7 -type f -delete
echo 'Removing tomcat temp files over 1 days old from '${tomcat_temp_dir}
/usr/bin/find ${tomcat_temp_dir}/ -mtime +1 -type f -delete
echo 'Removing catalina log files over 7 days old from '${tomcat_log_dir}
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "catalina.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "host-manager.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "localhost.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "localhost_access_log.*" -delete
/usr/bin/find ${tomcat_log_dir}/ -mtime +7 -type f -iname "manager.*" -delete
echo '***********************************************************'

exit 0
