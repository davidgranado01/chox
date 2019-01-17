#!/bin/bash
#==============================================================================
#
# CHOX Transfer to FTP Gateway Utility
#
#------------------------------------------------------------------------------
#
# Transfer a file to an account onthe FTP Gateway
#
#------------------------------------------------------------------------------
#
# Usage: $0 <client> <file>
#    where <client is one of RSA, EHI or ERAC
#
#------------------------------------------------------------------------------
#
#  Revision history:
#
#  v1 - Feb 2015       - JLD - Initial version
#
#==============================================================================
function getAccount {
    CLIENT=$1
    case "$CLIENT" in
        "LV" | "lv" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/outbound/LV
            ;;
        "RSA" | "rsa" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/outbound/RSA
            ;;
       "EHI" | "ehi" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/outbound/EHI
            ;;
        *)
            echo "No account defined for $CLIENT"
            echo "Usage: $0 RSA|EHI|LV <file>"
            exit 1;;
    esac
}


if [ $# -ne 2 ];
then
    echo "Usage: $0 RSA|EHI|LV <file>"
    echo
    exit 1
fi

CLIENT=$1
FILE_TO_TRANSFER=$2
TMP_DIR=`mktemp -d`

# Use PROD, DEV or TEST
getAccount ${CLIENT}


# Create trigger file
#TRIGGER_FILE=${TMP_DIR}/${FILE_TO_TRANSFER/%.zip}-`date "+%Y%m%d"`.trg
TRIGGER_FILE_FULL=${FILE_TO_TRANSFER%.*}
TRIGGER_FILE=${TMP_DIR}/${TRIGGER_FILE_FULL##*/}.trg
/bin/touch ${TRIGGER_FILE}

# Transfer
/bin/mv ${FILE_TO_TRANSFER} ${DIRECTORY_TO_STORE}
/bin/mv ${TRIGGER_FILE} ${DIRECTORY_TO_STORE}

/bin/rmdir $TMP_DIR

echo "Done."

exit 0
