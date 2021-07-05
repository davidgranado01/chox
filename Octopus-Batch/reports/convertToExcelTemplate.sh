#!/bin/bash
#params :
#1 - input Directory
#2 - done Directory
#3 - output directory

export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
export PATH=${JAVA_HOME}/bin:/bin:${PATH}
MODE=#{MODE} # EXPORT - send emails and transfer files, NOEMAIL - rports generated but not emailed but sftp ok, NOEXPORT - reports generated but not delivered via sftp or email
DELAY_SEND_EMAIL_REPORTS=#{DELAY_SEND_EMAIL_REPORTS}

function getPassword {
    REPORT_NAME=$1
    case "$REPORT_NAME" in
        "ERS-"* )
            PASSWORD="Equitybord";;
        "DLG-"* )
            PASSWORD="2014Sweets!";;
        "Octagon-"* )
            PASSWORD="Octag0n";;
        "SGSM-"* )
            PASSWORD="5laterG0rd0n";;
        "RSA-"* )
            PASSWORD="Royal1";;
        "Motability-"* )
            PASSWORD="Mot1";;
        "Helphire-"* )
            PASSWORD="Help1";;
        "ERAC-"* )
            PASSWORD="ERACD4t4";;
        "AccidentExchange-"* )
            PASSWORD="3xchang3";;
        *)
            PASSWORD="C0mpliance";;
    esac
}

function getEmailReceivers {
    REPORT_NAME=$1
    ACTIVE=0

    case "$REPORT_NAME" in
        "AccidentExchange-Processed_Notifications_Report_"* )
	    ACTIVE=1
            EMAIL_RECEIVERS=igp.unit@accidentexchange.com,kmartin@accidentexchange.com,ymiah@accidentexchange.com,proche@accidentexchange.com;;
        *)
            EMAIL_RECEIVERS=unknown@nowhere.com;;
    esac

}


if [ $# -ne 3 ]
then
    echo "Usage: $0 <input directory> <processed directory> <output directory>"
    echo
    exit 1
fi

BCC_RECIPIENTS=MichaelPaul.Kemp@valexa.com,Robert.Hon@audatex.co.uk
RECIPIENTS=DL-UKBR-Audatex-penguin-reporting@audatex.com

REPORT_DIR=$1
PROCESSED_DIR=$2
OUTPUT_DIR=$3

CWD=`pwd`
OS=`uname`

echo "**************************************************************************"
echo "**       Converting reports to Excel Template: `date`      **"
echo "**************************************************************************"

if [ "${OS}" = "Darwin" ]
then
    echo "We are running on OS X...."
    GENERATE_COMMAND='java -Xms1G -Xmx6G -jar /Users/john/Projects/Netbeans/chox/reportToExcelTemplate/target/reportToExcelTemplate-0.1.one-jar.jar'
else
    echo "We are running on Linux...."
    GENERATE_COMMAND='java -Xms1G -Xmx7G -jar  /home/chox/bin/reportToExcelTemplate.jar'
fi

pushd ${REPORT_DIR}
#maxSize=9000
for reportFile in $(ls *.rpt);
do
    echo "Processing file " ${reportFile}
    ${GENERATE_COMMAND} ${CWD}/${REPORT_DIR}/${reportFile} ${CWD}/${OUTPUT_DIR}/${reportFile/%.rpt}.xls
    /bin/mv ${CWD}/${REPORT_DIR}/${reportFile} ${CWD}/${PROCESSED_DIR}/${reportFile}
    getEmailReceivers ${reportFile}
#    getPassword ${reportFile}

#    actualsize=$(du -k "${CWD}/${OUTPUT_DIR}/${reportFile/%.rpt}.xls" | cut -f 1)
#    if [ $actualsize -ge $maxSize ]; then
#        /bin/gzip ${CWD}/${OUTPUT_DIR}/${reportFile/%.rpt}.xls
#        attachedFile=${reportFile/%.rpt}.xls.gz
#    else
#        attachedFile=${reportFile/%.rpt}.xls
#    fi

    pushd ${CWD}/${OUTPUT_DIR}
    xlsFile=${reportFile/%.rpt}.xls
#    zipFile=${reportFile/%.rpt}.zip
#    /usr/bin/zip -P ${PASSWORD} ${zipFile} ${xlsFile}

    SUBJECT="CHOX: MI Report "${xlsFile}

    if [ "${ACTIVE}" -eq "1" ]; then
        if [ "${MODE}" == "EXPORT" ]; then
       RECIPIENTS=${EMAIL_RECEIVERS}
sleep ${DELAY_SEND_EMAIL_REPORTS}
/usr/bin/mutt -s "${SUBJECT}" -b ${BCC_RECIPIENTS} -a ${CWD}/${OUTPUT_DIR}/${xlsFile} -- ${RECIPIENTS}  <<  --EOF--
    Please find the attached excel report:
            ${attachedFile}

--EOF--
        fi
    elif [ "${MODE}" == "EXPORT" ]; then
sleep ${DELAY_SEND_EMAIL_REPORTS}
/usr/bin/mutt -s "${SUBJECT}" -b ${BCC_RECIPIENTS} -a ${CWD}/${OUTPUT_DIR}/${xlsFile} -- ${RECIPIENTS}  <<  --EOF--
Please find the attached excel report:
    ${attachedFile}

[This email will be sent to: ${EMAIL_RECEIVERS}]
--EOF--
    fi
#    /bin/rm ${zipFile}
    popd
done

popd

echo "**************************************************************************"

exit 0
