#!/bin/bash
#==============================================================================
#
# CHOX Get from FTP Gateway Utility
#
#------------------------------------------------------------------------------
#
# Get *.xlsx files from an account on the FTP Gateway and remove
#
#------------------------------------------------------------------------------
#
# Usage: $0 <client>
#    where <client is one of RSA, EHI or LV
#
#------------------------------------------------------------------------------
#
#  Revision history:
#
#  v1.1 - Sept 2018      - JLD - Updated for Audatex infrastructure
#  v1   - Feb 2017       - JLD - Initial version
#
#==============================================================================
function getAccount {
    CLIENT=$1
    case "$CLIENT" in
        "LV" | "lv" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/inbound/LV
            ;;
        "DLG" | "dlg" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/inbound/DLG
            ;;
        "RSA" | "rsa" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/inbound/RSA
            ;;
        "EHI" | "ehi" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/inbound/EHI
            ;;
        *)
            echo "No account defined for $CLIENT"
            echo "Usage: $0 LV|RSA|EHI|DLG <file>"
            exit 1
            ;;
    esac
}


if [ $# -ne 1 ];
then
    echo "Usage: $0 LV|DLG|RSA|EHI"
    echo
    exit 1
fi
PROCESSED_DIR=/home/chox/logs/processed
PSQL_COMMAND=/usr/bin/psql
HOST=#{DB_HOST} #dev-ukre-pgs28.dc.solera-uk.com
USER=#{DB_USER.CHOX} #chox
DB=#{DB_NAME.CHOX} #dev_uk_chox
CLIENT=$1

echo '***********************************************************'
echo `date`': Getting claim matching & paid'
echo '                             invoice files for client '${CLIENT}
echo '***********************************************************'

#FILE_PATTERN=$2
#TMP_DIR=`mktemp -d`

# Use PROD, DEV or TEST
getAccount ${CLIENT}


#set IFS variable, which determines separator for file names, to end of line character so that we can handle file names with spaces
SAVEIFS=$IFS
IFS=$(echo -en "\n\b")

pushd ${DIRECTORY_TO_STORE}

#
# Claim Matching files - *.xlsx files in root directory
#
echo "Looking for 'Claim Matching' bordereaux (*.xlsx) files in ${DIRECTORY_TO_STORE}..."


for ENTRY in ${DIRECTORY_TO_STORE}/*.xlsx;
do
    [ -e "$ENTRY" ] || continue
    [ ! -d "$ENTRY" ] || continue
    FILENAME=$(basename $ENTRY)
    # Convert excel file to CSV
    echo "Converting excel file "${FILENAME}" to CSV...."
    /usr/local/bin/xlsx2csv -i ${FILENAME} | /bin/sed '/^,,,,,,/d' > ${FILENAME/%.xslx}.csv
    # Import to databasexa
    echo "Importing CSV file into database..."
    $PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "\copy claim_matching_copy(claim_number,insurer_name,third_party_vehicle_registration,incident_date,indemnity_stance,liability_stance,liability_insurer) FROM '${FILENAME/%.xslx}.csv' delimiter ',' csv header;"
    echo "Removing files..."
    /bin/rm ${FILENAME/%.xslx}.csv
    /bin/mv ${FILENAME} ${PROCESSED_DIR}/${FILENAME}.processed
    # Move valid entries from copy table to import table
    $PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "insert into claim_matching_import(claim_number,insurer_name,third_party_vehicle_registration,incident_date,indemnity_stance,liability_stance,liability_insurer) select claim_number,insurer_name,replace(third_party_vehicle_registration, ' ', ''),incident_date::timestamp without time zone,indemnity_stance,liability_stance,liability_insurer::numeric(5,2) from claim_matching_copy where insurer_name is not null and claim_number is not null and third_party_vehicle_registration is not null and incident_date is not null and indemnity_stance is not null and liability_stance is not null and is_validTimestamp(incident_date) and is_validLiability(liability_insurer);"
    # Truncate copy table
    $PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "truncate table claim_matching_copy;"
done


# Delete Already Matched entries from import table
echo "Deleting already matched entries from claim matching import table..."
$PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "delete from claim_matching_import cmi using claim_matching cm where cmi.claim_number = cm.claim_number and cmi.insurer_name = cm.insurer_name and cm.match_status != 0;"
# remove old entries from claim matching table
echo "Deleting claim matching import entries older than 30 days..."
$PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "delete from claim_matching where match_status=0 and now()::date - last_modified_date::date > 30;"

# remove duplicate entries from import table on (insurer name, claim_number)
echo "Removing duplicate entries from claim matching import table..."
$PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "delete from claim_matching_import cmi using claim_matching_import cmi2 where cmi.insurer_name=cmi2.insurer_name and cmi.claim_number=cmi2.claim_number and cmi.id < cmi2.id"

echo "Finished processing 'ClaimMatching' bordereaux."
#
# Finished processing claim matching files
#


#
# Paid Invoice files - *.csv files in directory 'paidInvoices'
#
echo "Looking for Paid Invoices (*.csv) files..."

# Get files
for ENTRY in ${DIRECTORY_TO_STORE}/LVCHOXInvoicepaid*.csv;
do
    [ -e "$ENTRY" ] || continue
    FILENAME=$(basename $ENTRY)
    # Import to databasexa
    echo "Importing CSV file '${FILENAME}' into import table..."
    $PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "\copy paid_invoices_import(insurer_name, cho_reference, claim_number) FROM '${FILENAME}' delimiter ',' csv header;"
    echo "Removing files..."
    /bin/mv ${FILENAME} ${PROCESSED_DIR}/${FILENAME}.processed
    # Move valid entries from copy table to import table
    echo "Inserting entries into paid_invoices table..."
    $PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "insert into paid_invoices(insurer_name, cho_reference, claim_number, created_by, last_modified_by) select distinct trim(insurer_name), trim(cho_reference), trim(claim_number), 999, 999 from paid_invoices_import pii where pii.insurer_name is not null and pii.claim_number is not null and pii.cho_reference is not null and not exists(select * from paid_invoices pi where pi.insurer_name=trim(pii.insurer_name) and pi.cho_reference=trim(pii.cho_reference) and pi.claim_number=trim(pii.claim_number));"
    # Truncate copy table
    echo "Truncating paid_invoices_import table..."
    $PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "truncate table paid_invoices_import;"
done

# Delete entries with no matching claims
echo "Deleting entries with no matching claim..."
$PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "delete from paid_invoices pi where not exists (select * from claim c, insurer ins where pi.claim_number=c.claim_number and pi.cho_reference=c.cho_reference and c.insurer_id=ins.id and pi.insurer_name = ins.name);"

# Delete entries in wrong status
echo "Deleting entries with wrong claim status..."
$PSQL_COMMAND -h ${HOST} -U ${USER} -d ${DB} -c "delete from paid_invoices pi using claim c, insurer ins where pi.claim_number=c.claim_number and pi.cho_reference=c.cho_reference and c.insurer_id=ins.id and pi.insurer_name = ins.name and c.status != 'AwaitingInvoicePayment';"

echo "Finished processing 'Paid Invoices' files"
#
# Finished processing paid invoice files
#

IFS=$SAVEIFS

popd

echo "Done."
echo '***********************************************************'
echo

exit 0

