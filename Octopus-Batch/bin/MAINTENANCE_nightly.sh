#!/bin/sh

log_dir="/home/chox/logs"
report_dir="/home/chox/reports"

PSQL_COMMAND=/usr/bin/psql
HOST=#{DB_HOST} #dev-ukre-pgs28.dc.solera-uk.com
USER=#{DB_USER.CHOX} #chox
DB=#{DB_NAME.CHOX} #dev_uk_chox

echo '***********************************************************'
echo `date`': Daily Maintenenace'
echo '***********************************************************'
echo 'Updating 'password last modified date' for admin users....'
${PSQL_COMMAND}  -h ${HOST} -U ${USER} -d ${DB} -c "update web_user set password_last_modified_date=now(), last_login_date=now(), is_expired=false where user_name ilike 'admin@%'"
${PSQL_COMMAND}  -h ${HOST} -U ${USER} -d ${DB} -c "update web_user set password_last_modified_date=now(), last_login_date=now(), is_expired=false where user_name in ('erac_scheduler','easidrive_scheduler','paidInvoices.lv','claimmatcher.lv','acknowledgeClaims.dlg','ecdupdate@aiclaimssolutions.com','ecdupdate@erac.com','webservices@erac.com')"
${PSQL_COMMAND}  -h ${HOST} -U ${USER} -d ${DB} -c "update web_user set password_last_modified_date=now(), last_login_date=now(), is_expired=false where user_name in ('notallocated@uk.rsagroup.com','non_abi','prestige_taxi','ff_auxillis','sub_erac','auxillis_fleet','auxillisreview2016','abi_gta')"
echo 'Removing log files over 7 days old from '${log_dir}
/usr/bin/find ${log_dir}/ -mtime +7 -type f -delete
echo 'Removing report files over 7 days old from '${report_dir}
/usr/bin/find ${report_dir}/ -mtime +7 -type f -delete
echo '***********************************************************'

exit 0
