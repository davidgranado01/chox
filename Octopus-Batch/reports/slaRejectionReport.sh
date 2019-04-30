#!/bin/bash
MI_DIRECTORY=/home/chox/reports/

if [ $# -ne 4 ];
then
    echo "Usage: $0 FixedFee|Subscriber <insurerId> <startDate> <endDate>"
    echo
    exit 1
fi

reportType=$1
insurerId=$2
startDate=$3
endDate=$4

TMPFILE=slaRejectionReport.$$.tmp
QUERYFILE=slaRejectionReport.$$.sql

PSQL_COMMAND=/usr/bin/psql
HOST=#{DB_HOST} #dev-ukre-pgs28.dc.solera-uk.com
USER=#{DB_USER.CHOX} #chox
DB=#{DB_NAME.CHOX} #dev_uk_chox

# Get Insurer name
$PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -o ${TMPFILE} << --EOF--
SELECT name from insurer where id=${insurerId}
--EOF--
insurerName=`head -3 ${TMPFILE} | tail -1 | tr -d '[[:space:]]'`;
/bin/rm ${TMPFILE}
DUMPFILE=${insurerName}-${reportType}_SLA_Rejection_Report-`date +"%F"`.txt


if [ $reportType = 'Subscriber' ];
then
    # First get max subscriber SLA Days for Insurer from BRE Band
    $PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -o ${TMPFILE} << --EOF--
SELECT max(subscriber_sla_days) from bre_band where insurer_id=${insurerId}
--EOF--
    maxSLA=`head -3 ${TMPFILE} | tail -1 | tr -d '[[:space:]]'`;
    /bin/rm ${TMPFILE}
#    echo "Generating report for Subscriber claims with SLA of "${maxSLA}" days"
    $PSQL_COMMAND --pset footer -h ${HOST} -U ${USER} -d ${DB} -o ${TMPFILE} << --EOF--
select * from slaSubscriberRejectionReportQuery(${insurerId}, '${startDate}', '${endDate}', ${maxSLA});
--EOF--
    /usr/bin/tail -2 ${TMPFILE} > ${QUERYFILE}
#    echo "Query file generated - running"
    $PSQL_COMMAND --pset footer -h ${HOST} -U ${USER} -d ${DB} -f ${QUERYFILE} -o ${DUMPFILE}.tmp
#    echo "Report created in "${DUMPFILE}
    /bin/rm ${QUERYFILE} ${TMPFILE}
    # Now we need to format the report:
    #       Remove column name header lines, leaving only the first one
    #       Remove header/bidy separator lines, except for the first one
    # First add header lines
    /usr/bin/head -2  ${DUMPFILE}.tmp >  ${DUMPFILE}
    # Now add all lines, excluding the headers and empty lines
    /bin/cat ${DUMPFILE}.tmp  | /bin/grep -v -e "-------------------+" | /bin/grep -v -e "Total # rejected" | /bin/sed '/^\s*$/d' >> ${DUMPFILE}
    /bin/rm ${DUMPFILE}.tmp
elif [ $reportType = 'FixedFee' ];
then
    # First get max Fixed Fee SLA Days for Insurer from BRE Band
    $PSQL_COMMAND --pset footer -h ${HOST} -U ${USER} -d ${DB} -o ${TMPFILE} << --EOF--
SELECT max(fixedfee_sla_days) from bre_band where insurer_id=${insurerId}
--EOF--
    maxSLA=`head -3 ${TMPFILE} | tail -1 | tr -d '[[:space:]]'`;
    /bin/rm ${TMPFILE}
#    echo "Generating report for Fixed Fee claims with SLA of "${maxSLA}" days"
    $PSQL_COMMAND --pset footer -h ${HOST} -U ${USER} -d ${DB} -o ${TMPFILE} << --EOF--
select * from slaFixedFeeRejectionReportQuery(${insurerId}, '${startDate}', '${endDate}', ${maxSLA});
--EOF--
    /usr/bin/tail -2 ${TMPFILE} > ${QUERYFILE}
#    echo "Query file generated - running"
    $PSQL_COMMAND --pset footer -h ${HOST} -U ${USER} -d ${DB} -f  ${QUERYFILE} -o ${DUMPFILE}.tmp
#    echo "Report created in "${DUMPFILE}
    /bin/rm ${QUERYFILE} ${TMPFILE}
    # Now we need to format the report:
    #       Remove column name header lines, leaving only the first one
    #       Remove header/bidy separator lines, except for the first one
    # First add header lines
    /usr/bin/head -2  ${DUMPFILE}.tmp >  ${DUMPFILE}
    # Now add all lines, excluding the headers and empty lines
    /bin/cat ${DUMPFILE}.tmp  | /bin/grep -v -e "-------------------+" | /bin/grep -v -e "Total # rejected" | /bin/sed '/^\s*$/d' >> ${DUMPFILE}
    /bin/rm ${DUMPFILE}.tmp
else
    echo "No such report type: " $reportType
    echo "Usage: $0 FixedFee|Subscriber <insurerId> <startDate> <endDate>"
    echo
    exit 1
fi

mv ${DUMPFILE} ${MI_DIRECTORY}

exit 0
