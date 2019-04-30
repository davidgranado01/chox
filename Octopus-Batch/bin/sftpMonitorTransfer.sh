#!/bin/bash
#==============================================================================
#
# CHOX SFTP Monitor/Transfer Utility
#
#------------------------------------------------------------------------------
#
# Transfer files between Audatex and Valexa sftp accounts
#
#------------------------------------------------------------------------------
#
# Usage: $0 <client>
#    where <client is one of RSA,
#
#------------------------------------------------------------------------------
#
#  Revision history:
#
#  v1 - April 2019       - JLD - Initial version
#
#==============================================================================
function getAccountDetails {
    CLIENT=$1
    ENV=$2
    case "$CLIENT" in
        "RSA" | "rsa" )
            case "$ENV" in
                "prd" )
                    VALEXA_SFTP_USER=chox-rsa-mi
                    VALEXA_SFTP_PASSWD='golden invented principle law'
                    AUDATEX_SFTP_USER=dx00010146
                    AUDATEX_SFTP_PASSWD='xuXj50j6FOCK1QOsw5fL'
                    AUDATEX_SFTP_URL='sftp://sftp.audatex.co.uk'
                    ;;
                "dev" )
                    VALEXA_SFTP_USER=chox-rsa-mi-dev
                    VALEXA_SFTP_PASSWD='police newspaper walk improve'
                    AUDATEX_SFTP_USER=dx00010147
                    AUDATEX_SFTP_PASSWD='YBAbBoXtL22L6xAucArf'
                    AUDATEX_SFTP_URL='sftp://pat-sftp.audatex.co.uk'
                    ;;
#                "tst" )
#                    VALEXA_SFTP_USER=chox-rsa-mi-test
#                    VALEXA_SFTP_PASSWD='eight divide six noted';;
                 "pat" )
                    AUDATEX_SFTP_USER=dx00010156
                    AUDATEX_SFTP_URL='pat-sftp.audatex.co.uk'
                    AUDATEX_SFTP_PASSWD='2usq0hsPG6tUsHkQJux5'
                    AUDATEX_SFTP_URL='sftp://pat-sftp.audatex.co.uk'
                    ;;
                "uat" )
                    VALEXA_SFTP_USER=chox-rsa-mi-test
                    VALEXA_SFTP_PASSWD='eight divide six noted'
                    AUDATEX_SFTP_USER=dx00010151
                    AUDATEX_SFTP_PASSWD='QcJO7fvoRGnJKf8JOnmX'
                    AUDATEX_SFTP_URL='sftp://pat-sftp.audatex.co.uk'
                   ;;
                "pre" )
                    AUDATEX_SFTP_USER=dx00010161
                    AUDATEX_SFTP_PASSWD='js3gLfQzO171FRDEs9OB'
                    AUDATEX_SFTP_URL='sftp://pat-sftp.audatex.co.uk'
                    ;;
            esac;;
#        "ERAC" | "erac" )
#            case "$ENV" in
#                "PROD" )
#                    VALEXA_SFTP_USER=chox-erac-mi
#                    VALEXA_SFTP_PASSWD='held memory base brother';;
#                "DEV" )
#                    VALEXA_SFTP_USER=chox-erac-mi-dev
#                    VALEXA_SFTP_PASSWD='unknown species shoot receive';;
#                "TEST" )
#                    VALEXA_SFTP_USER=chox-erac-mi-test
#                    VALEXA_SFTP_PASSWD='probably lying split single';;
#            esac;;
        *)
            echo "No account defined for $CLIENT"
            echo "Usage: $0 prd|pre|pat|uat|dev RSA"
            exit 1;;
    esac
}


if [ $# -ne 2 ];
then
    echo "Usage: $0 prd|pre|pat|uat|dev RSA"
    echo
    exit 1
fi

TMP_DIR=`mktemp -d`
pushd ${TMP_DIR}

# Get account passwords, dependant on client and environment
getAccountDetails $2 $1


VALEXA_SFTP_URL='sftp://gateway.idasnetwork.com:22/~/'

CURL_LOCATION="/usr/local/bin/curl"
if [ ! -f ${CURL_LOCATION} ]
then
    CURL_LOCATION="/usr/bin/curl"
    if [ ! -f ${CURL_LOCATION} ]
    then
        CURL_LOCATION="/opt/local/bin/curl"
    fi
fi


# Get incoming files from Valexa SFTP site and transfer to Audatex upload
echo "Checking Valexa incoming account ${VALEXA_SFTP_USER} on ${VALEXA_SFTP_URL}..."
REMOTE_FILES=`${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 -l --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user ${VALEXA_SFTP_USER}:/'${VALEXA_SFTP_PASSWD}/' ${VALEXA_SFTP_URL}data/upload/`
# curl -s -S --retry 64 --retry-max-time 3600 -l --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user chox-rsa-mi-dev:/'police newspaper walk improve/' sftp://gateway.idasnetwork.com/home/chox-rsa-mi-dev/
#echo "REMOTE_FILES=${REMOTE_FILES}"

for REMOTE_FILE in ${REMOTE_FILES}
do
#  echo "Processing '${REMOTE_FILE} (size ${#REMOTE_FILE})"
  # Only transfer files with names > 2 chars - neeeded to exclude '.' and '..'
  if [ "${#REMOTE_FILE}" -gt "2" ]
    then
    # transfer all files
    echo "Getting remote file '${REMOTE_FILE}'..."
    # Download Matching Remote File
    ${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user ${VALEXA_SFTP_USER}:/'${VALEXA_SFTP_PASSWD}/' -O ${VALEXA_SFTP_URL}data/upload/${REMOTE_FILE}
    # Remove remote file
    echo "Removing remote file '${REMOTE_FILE}'..."
    ${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user ${VALEXA_SFTP_USER}:/'${VALEXA_SFTP_PASSWD}/' ${VALEXA_SFTP_URL} -Q "rm data/upload/${REMOTE_FILE}"
  fi
done

FILES_TO_TRANSFER=`ls`
for FILE_TO_TRANSFER in ${FILES_TO_TRANSFER}
do
    echo "Transferring file ${FILE_TO_TRANSFER}..."
    ${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --ftp-create-dirs --user ${AUDATEX_SFTP_USER}:${AUDATEX_SFTP_PASSWD} -T ${FILE_TO_TRANSFER} "${AUDATEX_SFTP_URL}:10022/~/data/upload/${FILE_TO_TRANSFER}"
#    /usr/bin/curl -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --ftp-create-dirs --user dx00010147:YBAbBoXtL22L6xAucArf -T check_tcatsize "sftp://pat-sftp.audatex.co.uk:10022/~/data/upload/check_tcatsize"
    echo "Removing local file ${FILE_TO_TRANSFER}..."
    /bin/rm ${FILE_TO_TRANSFER}
done


# Get outgoing files from Audatex and transfer to Valexa download
echo "Checking Audatex outgoing account ${AUDATEX_SFTP_USER}:/'${AUDATEX_SFTP_PASSWD}/'..."
REMOTE_FILES=`${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 -l --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user ${AUDATEX_SFTP_USER}:${AUDATEX_SFTP_PASSWD} ${AUDATEX_SFTP_URL}:10022/~/data/download/`
# curl -s -S --retry 64 --retry-max-time 3600 -l --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user dx00010146:'xuXj50j6FOCK1QOsw5fL' sftp://sftp.audatex.co.uk:10022/~/data/download/
echo "REMOTE_FILES=${REMOTE_FILES}"
for REMOTE_FILE in ${REMOTE_FILES}
do
#  echo "Processing '${REMOTE_FILE} (size ${#REMOTE_FILE})"
  # Only transfer files with names > 2 chars - neeeded to exclude '.' and '..'
  if [ "${#REMOTE_FILE}" -gt "2" ]
  then
    echo "Getting remote file "$REMOTE_FILE
    ${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user ${AUDATEX_SFTP_USER}:${AUDATEX_SFTP_PASSWD} -O "${AUDATEX_SFTP_URL}:10022/~/data/download/${REMOTE_FILE}"
    # Remove remote file
    echo "Removing remote file ${REMOTE_FILE}..."
    ${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user ${AUDATEX_SFTP_USER}:${AUDATEX_SFTP_PASSWD} ${AUDATEX_SFTP_URL}:10022 -Q "rm data/download/${REMOTE_FILE}"
#curl -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --user dx00010147:YBAbBoXtL22L6xAucArf sftp://pat-sftp.audatex.co.uk:10022 -Q "rm data/download/MAINTENANCE_nightly.sh"
  fi
done

FILES_TO_TRANSFER=`ls`
for FILE_TO_TRANSFER in ${FILES_TO_TRANSFER}
do
    echo "Transferring file ${FILE_TO_TRANSFER}..."
    ${CURL_LOCATION} -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --ftp-create-dirs --user ${VALEXA_SFTP_USER}:/'${VALEXA_SFTP_PASSWD}/' -T ${FILE_TO_TRANSFER} "${VALEXA_SFTP_URL}data/download/${FILE_TO_TRANSFER}"
# curl -s -S --retry 64 --retry-max-time 3600 --insecure --key /home/chox/.ssh/id_rsa --pubkey /home/chox/.ssh/id_rsa.pub --ftp-create-dirs --user chox-rsa-mi-dev:/'police newspaper walk improve/' -T crontab.final "sftp://gateway.idasnetwork.com:22/~/data/download/crontab.final"
    echo "Removing local file ${FILE_TO_TRANSFER}..."
    /bin/rm ${FILE_TO_TRANSFER}
done

popd
echo "Removing temporary dirtectory "$TMP_DIR
/bin/rmdir $TMP_DIR

echo "Done."

exit 0
