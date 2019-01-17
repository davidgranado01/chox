#!/bin/bash
#params :
#1 - true indicates generate stats
if [ $# -ne 0 -a $# -ne 1 ];
then
    echo "Usage: $0 [true|false]"
    echo
    exit 1
fi

ZIPFILE=dashboardStats-`date +"%F"`.zip
DUMP_DIR="/tmp/chox-dashboard-kbbs-$$"

if [ $# -eq 1 ]
then
    if [ $1 != 'true' -a $1 != 'false' ]
    then
        echo "Usage: $0 [true|false]"
        echo
        exit 1
    fi
    GENERATE=$1
fi

: ${GENERATE:=false}


OS=`uname`
if [ "${OS}" = "Darwin" ]
then
    echo "We are running on OS X...."
    PSQL_COMMAND=/Library/PostgreSQL/9.2/bin/psql
else
    echo "We are running on Linux...."
    PSQL_COMMAND=/usr/bin/psql
fi
HOST=#{DB_HOST} #dev-ukre-pgs28.dc.solera-uk.com
USER=#{DB_USER.CHOX} #chox
DB=#{DB_NAME.CHOX} #dev_uk_chox

/bin/mkdir ${DUMP_DIR}
echo "Creating CSV dashboard files in directory ${DUMP_DIR}"

# Create trigger file
TRIGGER_FILE="${DUMP_DIR}/${ZIPFILE/%.zip}.trg"
/bin/touch ${TRIGGER_FILE}

#
# Dashboard stats (totals)
#

if [ "${GENERATE}" = "true" ]
then
echo 'Generating top-level dashboard stats...'
${PSQL_COMMAND}  -h ${HOST} -U ${USER} -d ${DB} << --EOF--
select * from generateDashboardStats();
--EOF--
fi

echo 'Dumping top-level dashboard stats to files...'
${PSQL_COMMAND}  -h ${HOST} -U ${USER} -d ${DB} << --EOF--
\copy dashboard_kbbs TO '${DUMP_DIR}/dashboard_kbbs.csv' DELIMITER ',' CSV HEADER;

\copy insurer(id, name) TO '${DUMP_DIR}/insurer.csv' DELIMITER ',' CSV HEADER;
\copy chorganisation(id, name)  TO '${DUMP_DIR}/chorganisation.csv' DELIMITER ',' CSV HEADER;
\copy workgroup(id, insurer_id, name) TO '${DUMP_DIR}/workgroup.csv' DELIMITER ',' CSV HEADER;
\copy web_user(id, first_name, last_name, insurer_id, chorganisation_id) TO '${DUMP_DIR}/user.csv' DELIMITER ',' CSV HEADER;
\copy web_user_workgroup(id, user_id, workgroup_id) TO '${DUMP_DIR}/user_workgroup.csv' DELIMITER ',' CSV HEADER;
\copy insurer_chorganisation(id, insurer_id, chorganisation_id) TO '${DUMP_DIR}/insurer_cho_mapping.csv' DELIMITER ',' CSV HEADER;

\copy (select * from dashboard_claim_data(null,null,null,null)) TO '${DUMP_DIR}/claim_data.csv' DELIMITER ',' CSV HEADER;
--EOF--
echo 'Zipping files....'
pushd ${DUMP_DIR}
/usr/bin/zip ${ZIPFILE} dashboard_kbbs.csv insurer.csv chorganisation.csv workgroup.csv user.csv user_workgroup.csv insurer_cho_mapping.csv claim_data.csv
/bin/rm -f dashboard_kbbs.csv insurer.csv chorganisation.csv workgroup.csv user.csv user_workgroup.csv insurer_cho_mapping.csv claim_data.csv
popd

echo "Done. Data is in ${DUMP_DIR}/${ZIPFILE}"


# Transfer
LOCATION=/shared/nfs/chox/dataload/outbound/KBBS
echo "Moving generated files to ${LOCATION}..."
/bin/mv ${DUMP_DIR}/${ZIPFILE} ${LOCATION}
/bin/mv ${DUMP_DIR}/*.trg ${LOCATION}

/bin/rmdir ${DUMP_DIR}

echo "Done."

exit 0;
