#!/bin/bash
#params :
#1 - database
#2 - Insurer Id - integer
#3 - Start Date - string
#4 - Dump location - string
#5 - xsd - optional
if [ $# -ne 4  -a $# -ne 5 ]
then
    echo "Usage: $0 <database> <insurerId> <startDate> <directory> [xsd]"
    echo
    exit 1
fi

if [ $# -eq 5 ]
then
    if [ $5 != 'xsd' ]
    then
        echo "Usage: $0 <database> <insurerId> <startDate> <directory> [xsd]"
        echo
        exit 1
    fi
    GENERATE_XSD=true
fi

DB=$1
INS_ID=$2
START_DATE=$3
DATA_DIR=$4

: ${INS_ID:=3}

OS=`uname`
#echo "System is '${SYSTEM}'"


# Removes first 3 lines, last 3 lines, and strips trailing '+' and white space from intermediatelines
#    $1 - input file
#    $2 - output file
function cleanOutput {
    INFILE=$1
    OUTFILE=$2

    if [ -f ${INFILE} ]
    then
        if [ "${OS}" = "Darwin" ]
        then
            cat ${INFILE} | tail -n +3 | sed '$d' |  sed '$d'  | sed '$d' | sed s'/+$//' | sed -e's/[ \t]*$//' > ${OUTFILE}
        else
            cat ${INFILE} | tail -n +3 | head -n -2 | sed s'/+$//' | sed -e's/[ \t]*$//' > ${OUTFILE}
        fi
        rm ${INFILE}
    fi
}

# Generate an XML file from a query
#    $1 - query
#    $2 - output file
function generateXML {
    QUERY1=$1
    OUTPUT_FILE1=$2

    $PSQL_COMMAND -U chox -d ${DB} -o ${OUTPUT_FILE1} << --EOF--
select query_to_xml(E'${QUERY1}', true, false, '');
--EOF--
}

# Generate an XSD file from a query
#    $1 - query
#    $2 - output file
function generateXSD {
    QUERY2=$1
    OUTPUT_FILE2=$2

    if [ ! -z ${GENERATE_XSD} ]
    then
        $PSQL_COMMAND -U chox -d ${DB} -o ${OUTPUT_FILE2}  << --EOF--
select query_to_xmlschema(E'${QUERY2}', true, false, '');
--EOF--
    fi
}


# Generate XML & XSD files from a query
#    $1 - query
#    $2 - output file base: '.xml' and '.xsd' will be appended to this to
#         generate the output file names
function generateOutput {
    QUERY3=$1
    OUTPUT_FILE3=$2

    # Generate XML
    generateXML "${QUERY3}" ${OUTPUT_FILE3}.tmp
    cleanOutput ${OUTPUT_FILE3}.tmp ${OUTPUT_FILE3}.xml

    # Generate XSD
    generateXSD "${QUERY3}" ${OUTPUT_FILE3}.tmp
    cleanOutput ${OUTPUT_FILE3}.tmp ${OUTPUT_FILE3}.xsd


}

if [ "${OS}" = "Darwin" ]
then
    echo "We are running on OS X...."
    PSQL_COMMAND='/Library/PostgreSQL/9.2/bin/psql'
    : ${START_DATE:=`date -v7d +%F`}
else
    echo "We are running on Linux...."
    PSQL_COMMAND='/usr/local/pgsql/bin/psql'
    : ${START_DATE:=`date --date="7 days ago" +%F`}
fi

echo 'Generating a full XML data dump from '${START_DATE}' for insurer with id='${INS_ID}

XML_DUMPFILE_PREFIX=${DATA_DIR}/fullDataDump-${START_DATE}


CLAIM_DATE_RESTRICTION=' and (c.created_date > '"\'"${START_DATE}"\'::Date or c.last_modified_date > \'"${START_DATE}"\'::Date)"
ADDITIONAL_RESTRICTION=' and c.id between 192000 and 192700'
DATE_RESTRICTION=' and (t.created_date > '"\'"${START_DATE}"\'::Date or t.last_modified_date > \'"${START_DATE}"\'::Date)"
QUERY_RESTRICTION=''

#
# Claim Table
#
echo "Generating dumpfile for claim...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-claim
QUERY='select id, managing_repair, policy_holder_contact_date, cho_reference, status, insurer_id, chorganisation_id, \
    customer_id, incident_id, invoice_id, third_party_id, vehicle_hire_id, engineer_report_id, created_by, created_date, \
    last_modified_by, last_modified_date, hire_monitoring_detail_id, claim_number, indeminty_amount, percentage_liability_accepted, \
    is_quantum_dispute, is_invoice_review_required, credit_agreement_date, gta_notice_date, is_fnol_reviewed, \
    reason_of_rejection_id, status_modified_date, previous_status, workgroup_id, claim_owner_id, version, percentage_liability_cho, \
    liability_agreed_date, liability_status, cho_claim_owner_id, tpi_claim_status, workgroup_id_original, claim_owner_id_original, \
    claim_type, auto_penalty_charges, manual_invoice_approved, final_review_ins, final_review_date_ins, final_review_by_ins, \
    sla_ext_days, no_attachments, managing_repair_last_modified_date, managing_repair_original, liability_modified_date, \
    liability_status_modified_date, is_total_loss_chase
from claim c where c.insurer_id='${INS_ID}''${CLAIM_DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Invoice Table
#
echo "Generating dumpfile for invoice...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-invoice
QUERY='select t.* from invoice t left join claim c on c.invoice_id=t.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Invoice Original Table
#
echo "Generating dumpfile for invoice_original...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-invoiceOriginal
QUERY='select t.* from invoice_original t left join invoice i on i.invoice_original_id=t.id left join claim c on c.invoice_id=i.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Customer Table
#
echo "Generating dumpfile for customer...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-customer
QUERY='select t.* from customer t left join claim c on c.customer_id=t.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Incident Table
#
echo "Generating dumpfile for incident...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-incident
QUERY='select t.* from incident t left join claim c on c.incident_id=t.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Witness Table
#
echo "Generating dumpfile for witness...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-witness
QUERY='select t.* from witness t left join incident i on i.id=t.incident_id left join claim c on c.incident_id=i.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Injury Table
#
echo "Generating dumpfile for injury...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-injury
QUERY='select t.* from injury t left join incident i on i.id=t.incident_id left join claim c on c.incident_id=i.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Third Party Table
#
echo "Generating dumpfile for third_party...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-third_party
QUERY='select t.* from third_party t left join claim c on c.third_party_id=t.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Vehicle Hire Table
#
echo "Generating dumpfile for vehicle_hire...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-vehicle_hire
QUERY='select t.* from vehicle_hire t left join claim c on c.vehicle_hire_id=t.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Engineer Report Table
#
echo "Generating dumpfile for engineer_report...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-engineer_report
QUERY='select t.* from engineer_report t left join claim c on c.engineer_report_id=t.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Hire Monitoring Detail Table
#
echo "Generating dumpfile for hire_monitoring_detail...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-hire_monitoring_detail
QUERY='select t.* from hire_monitoring_detail t left join claim c on c.hire_monitoring_detail_id=t.id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# All above tables are 1-1: lets generate a single composite file as well
#
#echo "Generating dumpfile for all claim data...."
#OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-allClaim
#QUERY='select * from claim c left outer join invoice i on c.invoice_id=i.id \
#                             left outer join customer cu on c.customer_id=cu.id \
#                             left outer join incident inc on c.incident_id=inc.id \
#                             left outer join injury inj on inc.id=inj.incident_id \
#                             left outer join third_party tp on c.third_party_id=tp.id \
#                             left outer join vehicle_hire vh on c.vehicle_hire_id=vh.id \
#                             left outer join engineer_report er on c.engineer_report_id=er.id \
#                             left outer join hire_monitoring_detail hmd on c.hire_monitoring_detail_id=hmd.id \
#where c.insurer_id='${INS_ID}''${CLAIM_DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}
#
#generateOutput "${QUERY}" ${OUTPUT_FILE}
#



#
# Hire Monitoring ECD Table
#
echo "Generating dumpfile for hire_monitoring_ecd...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-hire_monitoring_ecd
QUERY='select t.* from hire_monitoring_ecd t left join claim c on c.id=t.claim_id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Audit Trail Table
#
echo "Generating dumpfile for audit_trail...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-audit_trail
QUERY='select t.* from audit_trail t left join claim c on c.id=t.claim_id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Comment Table
#
echo "Generating dumpfile for comment...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-comment
QUERY_RESTRICTION=' and t.visibility_type in (0,1)'
QUERY='select t.* from comment t left join claim c on c.id=t.claim_id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}

#
# BRE History Table
#
echo "Generating dumpfile for history...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-history
QUERY_RESTRICTION=' and t.is_public=true'
QUERY='select t.* from history t left join claim c on c.id=t.claim_id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Task Table
#
echo "Generating dumpfile for task...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-task
QUERY_RESTRICTION=' and (t.insurer=true or (t.insurer=false and visibility=3))'
QUERY='select t.* from task t left join claim c on c.id=t.claim_id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Attachment Table
#
echo "Generating dumpfile for attachment...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-attachment
QUERY_RESTRICTION=''
QUERY='select t.id, t.claim_id, t.file_name, t.remarks, t.category, t.created_by, t.created_date, t.file_type, t.version, t.deleted from attachment t left join claim c on c.id=t.claim_id where c.insurer_id='${INS_ID}''${DATE_RESTRICTION}''${ADDITIONAL_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Web User Table
#
echo "Generating dumpfile for web_user...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-web_user
#QUERY_RESTRICTION=''
QUERY_RESTRICTION=' and (t.chorganisation_id is null or t.chorganisation_id=3)'  # restrict to RSA users? - for now...
QUERY='select t.id, t.user_name, t.first_name, t.last_name, t.email, t.status, t.is_expired, t.insurer_id, t.chorganisation_id, t.last_login_date, t.blocked, t.blocked_date, t.version from web_user t where 1=1'${DATE_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Insurer Table
#
echo "Generating dumpfile for insurer...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-insurer
QUERY_RESTRICTION=''
QUERY='select t.id, t.name, t.status, t.version from insurer t where 1=1'${DATE_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Workgroup Table
#
echo "Generating dumpfile for workgroup...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-workgroup
QUERY_RESTRICTION=''
QUERY='select t.id, t.name, t.site, t.team, t.status, t.version from workgroup t, insurer i where t.insurer_id=i.id'${DATE_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Chorganisation Table
#
echo "Generating dumpfile for chorganisation...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-chorganisation
QUERY_RESTRICTION=''
QUERY='select t.id, t.name, t.status, t.version from chorganisation t where 1=1'${DATE_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Reason Of Rejection Table
#
echo "Generating dumpfile for reason_of_rejection...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-reason_of_rejection
QUERY_RESTRICTION=' and t.insurer_id='${INS_ID}
QUERY='select t.id, t.name, t.description, t.type, t.version from reason_of_rejection t where 1=1'${DATE_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Reason Of Delay Table
#
echo "Generating dumpfile for reason_of_delay...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-reason_of_delay
QUERY_RESTRICTION=''
QUERY='select t.id, t.name, t.description, t.is_active, t.version from reason_of_delay t where 1=1'${DATE_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


#
# Vehicle Class Table
#
echo "Generating dumpfile for vehicle_class...."
OUTPUT_FILE=${XML_DUMPFILE_PREFIX}-vehicle_class
QUERY_RESTRICTION=''
QUERY='select t.id, t.name, t.version from vehicle_class t where 1=1'${DATE_RESTRICTION}''${QUERY_RESTRICTION}

generateOutput "${QUERY}" ${OUTPUT_FILE}


exit 0
