#!/bin/sh
#==============================================================================
#
# CHOX XML Data Dump Utility
#
#------------------------------------------------------------------------------
#
# This utility generates XML delta files for any of the business tables in
# the PAWS database. Each of the business tables has an associated _history
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
# Usage: $0 <database> <insurerId> <startDate>
#
#------------------------------------------------------------------------------
#
#  Revision history:
#
#  v1 - March 2014       - JLD - Initial version
#
#==============================================================================
if [ $# -ne 3 -a $# -ne 4 -a $# -ne 2 ];
then
    echo "Usage: $0 <database> <insurerId> [<startDate]> [xsd]"
    echo
    exit 1
fi

if [ $# -eq 4 ]
then
    if [ $4 != 'xsd' ]
    then
        echo "Usage: $0 <database> <insurerId> <startDate> [xsd]"
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
    GENERATE_XSD=xsd
fi

: ${START_DATE:=`/bin/date --date="7 days ago" +%F`}

# Production
SFTP_USER=chox-rsa-mi
SFTP_PASSWD='golden invented principle law'

# Dev
#SFTP_USER='chox-rsa-mi-dev'
#SFTP_PASSWD='police newspaper walk improve'

# Test
#SFTP_USER=chox-rsa-mi-test
#SFTP_PASSWD='eight divide six noted'

SFTP_URL='sftp://gateway.idasnetwork.com/home/'${SFTP_USER}'/'

CURL_LOCATION="/usr/local/bin/curl"
if [ ! -f ${CURL_LOCATION} ]
then
    CURL_LOCATION="/usr/bin/curl"
    if [ ! -f ${CURL_LOCATION} ]
    then
        CURL_LOCATION="/opt/local/bin/curl"
    fi
fi

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
    ${DUMP_SCRIPT_LOCATION} $1 $2 ${START_DATE} ${DUMP_DIR}
else
    echo "Creating XML and XSD dump files in directory ${DUMP_DIR}"
    ${DUMP_SCRIPT_LOCATION} $1 $2 ${START_DATE} ${DUMP_DIR} ${GENERATE_XSD}
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
echo "Transfering files using account ${SFTP_USER}"
for transferFile in ${DUMP_DIR}/*.ZIP ${DUMP_DIR}/*.trg
do
    ${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --ftp-create-dirs --user ${SFTP_USER}:/'${SFTP_PASSWD}/' -T ${transferFile} ${SFTP_URL}
    RC=$?
    if [ ${RC} -eq 0 ]
    then
        /bin/rm -f ${transferFile}
    fi
done

/bin/rmdir ${DUMP_DIR}

echo "Done."

exit 0
