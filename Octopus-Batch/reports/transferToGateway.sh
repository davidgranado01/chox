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
        "DLG" | "dlg" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/outbound/DLG
            ;;
        "Kindertons" | "kindertons" )
            DIRECTORY_TO_STORE=/shared/nfs/chox/dataload/outbound/Kindertons
            ;;
        *)
            echo "No account defined for $CLIENT"
            echo "Usage: $0 RSA|EHI|LV|DLG|Kindertons <file>"
            exit 1;;
    esac
}


if [ $# -ne 2 ];
then
    echo "Usage: $0 RSA|EHI|LV|DLG|Kindertons <file>"
    echo
    exit 1
fi

CLIENT=$1
FILE_TO_TRANSFER=$2

# Use PROD, DEV or TEST
getAccount ${CLIENT}

# Transfer
/bin/cp ${FILE_TO_TRANSFER} ${DIRECTORY_TO_STORE}

echo "Done."

exit 0
