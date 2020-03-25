#!/bin/bash
#==============================================================================
#
# CHOX XML Data Dump Utility
#
#------------------------------------------------------------------------------
#
# This utility generates XML delta files for any of the business tables in
# the Chox database. Each of the business tables has an associated _history
# table that contains all of the historical data for the main table. This
# _history table is used to provide a full dump of all activity to the core
# data items.
#
#
# Once the delta XML files are created they are transfered to an SFTP
# server and deleted.
#
#------------------------------------------------------------------------------
#
# Usage: $0  <insurerId> <outboundLocation> <startDate> <endDate> [xsd]
#
#------------------------------------------------------------------------------
#
#  Revision history:
#
#  v1 - March 2014       - JLD - Initial version (choxDataTransfer.sh)
#  v2 - March 2020       - mainly for Mobility to dump a range of date
#
#==============================================================================

if [ $# -ne 4  -a $# -ne 5 ]
then
    echo "Usage: $0  <insurerId> <outboundLocation> <startDate> <endDate> [xsd]"
    echo
    exit 1
fi

if [ $# -eq 5 ]
then
    if [ $5 != 'xsd' ]
    then
        echo "Usage: $0 <insurerId> <outboundLocation> <startDate> <endDate> [xsd]"
        echo
        exit 1
    fi
    GENERATE_XSD=xsd
fi

INSURER_ID=$1
OUTBOUND_LOCATION=$2
START_DATE=$3
END_DATE=$4

echo '***********************************************************'
echo `date`': Generating XML Dump fir insurer ' ${INSURER_ID} '*************'
echo '***********************************************************'

# Production
DUMP_SCRIPT_LOCATION="/home/chox/bin/rangeDump.sh"

if [ ! -f ${DUMP_SCRIPT_LOCATION} ]
then
    DUMP_SCRIPT_LOCATION="./rangeDump.sh"
    if [ ! -f ${DUMP_SCRIPT_LOCATION} ]
    then
        echo "Cannot find XML Data Dump script at ${DUMP_SCRIPT_LOCATION}"
        echo
        exit 1
    fi
fi

DUMP_DIR="/tmp/chox-data-dump-$$"
/bin/mkdir ${DUMP_DIR}

RANGE_START_DATE=$START_DATE
RANGE_END_DATE=$(date -I -d "$RANGE_START_DATE + 1 month")

while [[ "$RANGE_START_DATE" < "$END_DATE" ]]; do

    if [[ "$RANGE_END_DATE" > "$END_DATE" ]]; then
      # loop will exit after this last one
      RANGE_END_DATE=$END_DATE
    fi

    echo "Dump range between: $RANGE_START_DATE and: $RANGE_END_DATE"

    # Create XML data dump files
    if [ -z ${GENERATE_XSD} ]
    then
        echo "Creating XML dump files in directory ${DUMP_DIR}"
        ${DUMP_SCRIPT_LOCATION} ${INSURER_ID} ${RANGE_START_DATE} ${RANGE_END_DATE} ${DUMP_DIR}
    else
        echo "Creating XML and XSD dump files in directory ${DUMP_DIR}"
        ${DUMP_SCRIPT_LOCATION} ${INSURER_ID} ${RANGE_START_DATE} ${RANGE_END_DATE} ${DUMP_DIR} ${GENERATE_XSD}
    fi

    RANGE_START_DATE=$(date -I -d "$RANGE_START_DATE + 1 month")
    RANGE_END_DATE=$(date -I -d "$RANGE_START_DATE + 1 month")
done

# Create trigger file
TRIGGER_FILE="${DUMP_DIR}/CHOX-`date "+%Y%m%d"`.trg"
/bin/touch ${TRIGGER_FILE}

# Compress
echo "Compressing files...."
/usr/bin/zip -q -j ${DUMP_DIR}/CHOX-`date "+%Y%m%d"`.ZIP ${DUMP_DIR}/*.XML
set echo
if [ "${GENERATE_XSD}" = "xsd" ]
then
    /usr/bin/zip -q -j ${DUMP_DIR}/CHOX-`date "+%Y%m%d"`.ZIP ${DUMP_DIR}/*.XSD
fi
/bin/rm -f ${DUMP_DIR}/*.XML ${DUMP_DIR}/*.XSD

# Transfer
echo "Transfering files using account ${OUTBOUND_LOCATION}"
mv ${DUMP_DIR}/*.ZIP ${OUTBOUND_LOCATION}
mv ${DUMP_DIR}/*.trg ${OUTBOUND_LOCATION}

/bin/rmdir ${DUMP_DIR}

echo "Done."
echo '***********************************************************'
echo

exit 0
