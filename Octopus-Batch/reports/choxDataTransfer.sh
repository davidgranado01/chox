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
# Usage: $0  <insurerId> <outboundLocation> [startDate] [xsd]
#
#------------------------------------------------------------------------------
#
#  Revision history:
#
#  v1 - March 2014       - JLD - Initial version
#
#==============================================================================
if [ $# -ne 1 -a $# -ne 2 -a $# -ne 3 -a $# -ne 4 ];
then
    echo "Usage: $0  <insurerId> <outboundLocation> [startDate] [xsd]"
    echo
    exit 1
fi

if [ $# -eq 4 ]
then
    if [ $4 != 'xsd' ]
    then
        echo "Usage: $0 <insurerId> <outboundLocation> [startDate] [xsd]"
        echo
        exit 1
    fi
    GENERATE_XSD=xsd
    START_DATE=$3
fi

if [ $# -eq 3 ]
then
    if [ $3 = 'xsd' ]
    then
        GENERATE_XSD=xsd
    else
	    START_DATE=$3
    fi
fi

: ${START_DATE:=`/bin/date --date="7 days ago" +%F`}
INSURER_ID=$1

echo '***********************************************************'
echo `date`': Generating XML Dump fir insurer ' ${INSURER_ID} '*************'
echo '***********************************************************'

# Production
OUTBOUND_LOCATION=$2
DUMP_SCRIPT_LOCATION="/home/chox/bin/fullXmlDataDump.sh"

if [ ! -f ${DUMP_SCRIPT_LOCATION} ]
then
    DUMP_SCRIPT_LOCATION="./fullXmlDataDump.sh"
    if [ ! -f ${DUMP_SCRIPT_LOCATION} ]
    then
        echo "Cannot find XML Data Dump script at ${DUMP_SCRIPT_LOCATION}"
        echo
        exit 1
    fi
fi

DUMP_DIR="/tmp/chox-data-dump-$$"
/bin/mkdir ${DUMP_DIR}

# Create XML data dump files
if [ -z ${GENERATE_XSD} ]
then
    echo "Creating XML dump files in directory ${DUMP_DIR}"
    ${DUMP_SCRIPT_LOCATION} ${INSURER_ID} ${START_DATE} ${DUMP_DIR}
else
    echo "Creating XML and XSD dump files in directory ${DUMP_DIR}"
    ${DUMP_SCRIPT_LOCATION} ${INSURER_ID} ${START_DATE} ${DUMP_DIR} ${GENERATE_XSD}
fi

# Create trigger file
TRIGGER_FILE="${DUMP_DIR}/CHOX-`date "+%Y%m%d"`.trg"
/bin/touch ${TRIGGER_FILE}

# Compress
echo "Compressing files...."
/usr/bin/zip -q -j ${DUMP_DIR}/CHOX-`date "+%Y%m%d"`.ZIP ${DUMP_DIR}/*.XML
set echo
if [ "${GENERATE_XSD}" = "true" ]
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
