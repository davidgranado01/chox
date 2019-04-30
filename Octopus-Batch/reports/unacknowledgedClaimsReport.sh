#!/bin/bash
PSQL_COMMAND=/usr/bin/psql
HOST=#{DB_HOST} #dev-ukre-pgs28.dc.solera-uk.com
USER=#{DB_USER.CHOX} #chox
DB=#{DB_NAME.CHOX} #dev_uk_chox

DUMPFILE=DLG-CREDIT_HIRE_NEW_NOTIFICATIONS_`date +"%d%m%Y%H%M"`.csv
MI_DIRECTORY=/home/chox/reports/


echo '***********************************************************'
echo `date`': Generating Unacknowledged Claims Report for DLG'
echo '***********************************************************'
INS_ID=6
$PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} << --EOF--
\copy (select * from unacknowledgedClaimsReport(${INS_ID}, null::integer[] , array['ClaimUnacknowledgedRouted'], null::integer[])) TO '${DUMPFILE}' DELIMITER ',' CSV HEADER;
--EOF--

/bin/cp ${DUMPFILE} ${MI_DIRECTORY}
/bin/rm ${DUMPFILE}

echo '***********************************************************'
echo 'Done generating Unacknowledged Claims Report for DLG'
echo '***********************************************************'
echo

exit 0;
