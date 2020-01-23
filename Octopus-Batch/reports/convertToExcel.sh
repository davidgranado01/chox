#!/bin/bash
#params :
#1 - input Directory
#2 - done Directory
#3 - output directory
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
export PATH=${JAVA_HOME}/bin:/bin:${PATH}
MODE=#{MODE} # EXPORT - send emails and transfer files, NOEMAIL - rports generated but not emailed but sftp ok, NOEXPORT - reports generated but not delivered via sftp or email

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
        "Auxillis-"* )
            PASSWORD="Help1";;
        "ERAC-"* )
            PASSWORD="ERACD4t4";;
        "AccidentExchange-"* )
            PASSWORD="3xchang3";;
        "AlbanyAssistance-"* )
            PASSWORD="Help1";;
        "QBE-"* )
            PASSWORD="QB3";;
        "Kindertons-"* )
            PASSWORD="Kindert0n5";;
        "Keoghs-"* )
            PASSWORD="K30ghsPa33";;
        *)
            PASSWORD="C0mpliance";;
    esac
}

function getEmailReceivers {
    REPORT_NAME=$1
# The 'ACTIVE' flag controls how the report is delivered, and can take the following options:
#    0 - the report is password-protected zipped but is only emailed internally (to BCC_RECEIVERS)
#    1 - the report is password-protected zipped and emailed to the client EMAIL_RECEIVERS, as well as being BCC'ed internally to the BCC_RECEIVERS
#    2 - the report is password-protected zipped and sftp'ed to the FTP_CLIENT (using the transferToGatewayWithTrigger.sh script)
#    3 - the report is not password protected or zipped, but is emailed to the client EMAIL_RECEIVERS, as well as being BCC'ed internally to the BCC_RECEIVERS
#    4 - the report is not password protected or zipped, and is sftp'ed to the FTP_CLIENT, with trg files added for txt/xls files (but not csv) (using transferToGateway.sh script for csv, transferToGatewayWithTrigger.sh for xls)
#    5 - the report is not password protected or zipped, and is sftp'ed to the FTP_CLIENT (using transferToGateway.sh script). txt/xls files only.
    ACTIVE=0
    unset -v FTP_CLIENT
    case "$REPORT_NAME" in
        "ERS-Monthly_Insurer_Cost_Report-"* |\
        "ERS-Monthly_Cost_Report_By_Team-"* |\
        "ERS-Monthly_Manual_CHOX_Cost_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Jonathan.Lee@ers.com,terry.joseph@ers.com;;
        "DLG-Monthly_Insurer_Cost_Report_By_AccidentDate-"* |\
        "DLG-Monthly_Cost_Report_By_Team_By_AccidentDate-"* |\
        "DLG-Monthly_Manual_CHOX_Cost_Report_By_AccidentDate-"* |\
        "DLG-Monthly_Manual_CHOX_Cost_Report_By_Team_By_AccidentDate-"* )
	        ACTIVE=0
            EMAIL_RECEIVERS=Steven.Talbot@directlinegroup.co.uk,stephen.hiscock@directlinegroup.co.uk,Andy.A.Cooper@directlinegroup.co.uk,Karen.Dean@nig-uk.com,claire.wills@directline.com,Angela.Kehoe@nig-uk.com,Simon.Holt@directlinegroup.co.uk,Claire.Ratcliffe@nig-uk.com,Annalise.Bartlett@nig-uk.com,tam.bedford@directlinegroup.co.uk;;
        "DLG-Monthly_Insurer_Cost_Report-"* )
            ACTIVE=1
            EMAIL_RECEIVERS=Steven.Talbot@directlinegroup.co.uk,stephen.hiscock@directlinegroup.co.uk,Andy.A.Cooper@directlinegroup.co.uk,Karen.Dean@nig-uk.com,Claire.Ratcliffe@nig-uk.com,Annalise.Bartlett@nig-uk.com,tam.bedford@directlinegroup.co.uk;;
        "DLG-Monthly_Cost_Report_By_Team-"* |\
        "DLG-Monthly_Manual_CHOX_Cost_Report-"* |\
        "DLG-Monthly_Manual_CHOX_Cost_Report_By_Team-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Steven.Talbot@directlinegroup.co.uk,stephen.hiscock@directlinegroup.co.uk,Andy.A.Cooper@directlinegroup.co.uk,Karen.Dean@nig-uk.com,claire.wills@directline.com,Angela.Kehoe@nig-uk.com,Simon.Holt@directlinegroup.co.uk,Claire.Ratcliffe@nig-uk.com,Annalise.Bartlett@nig-uk.com,tam.bedford@directlinegroup.co.uk;;
        "DLG-ClaimDetailsDump-"* |\
        "DLG-InvoiceDetailsDump-"* |\
        "DLG-ClaimCycleDump-"* |\
        "DLG-NotesDump-"* )
            ACTIVE=1
            EMAIL_RECEIVERS=ClaimsAnalytics-motor@directlinegroup.co.uk;;
        "Octagon-Monthly_Insurer_Cost_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=carl.cripps@catalystcsl.co.uk,Terry.Clarke@catalystcsl.co.uk,tony.collins@horizon.gi;;
        "DLG-Monthly_BRE_Audit_Report-"* |\
        "DLG-Pilot_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Tam.Bedford@directlinegroup.co.uk;;
        "DLG-BRE_Monthly_Review_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Richard.e.brown@directlinegroup.co.uk,Tam.Bedford@directlinegroup.co.uk,Andy.A.Cooper@directlinegroup.co.uk;;
        "DLG-Hire_Claimed_vs_Paid_Days-ERAC-"* |\
	    "DLG-Invoice_Notifications-ManualAuxillis-"* )
            ACTIVE=1
            EMAIL_RECEIVERS=tam.bedford@directlinegroup.co.uk;;
	    "DLG-HireInvoiceUploadReport-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=tam.bedford@directlinegroup.co.uk,phil.room@directlinegroup.co.uk;;
        "DLG-PrestigeClaimReport-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=tam.bedford@directlinegroup.co.uk;;
	    "DLG-AuditHistoryReport-ManualHelphire-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=tam.bedford@directlinegroup.co.uk;;
        "DLG-Monthly_Review_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=BICC.Production.Support@directlinegroup.co.uk,tom.stanuil@directlinegroup.co.uk;;
        "SGSM-CHO_PaymentProfile_Report-"* |\
        "SGSM-Review_Report-"* |\
        "SGSM-Contested_Count_Report-"* |\
        "SGSM-Open_Claims_Report-"* |\
        "SGSM-Invoiced_Claims_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=peter.summers@slatergordonsolutions.co.uk,chantelle.carroll@slatergordonsolutions.co.uk,amy.delves@slatergordonsolutions.co.uk;;
        "MPK-SGSM-Insurer_Monthly_Cost_Report-"* )
            ACTIVE=0
            EMAIL_RECEIVERS=toBeProvided@By.MPK;;
        "ERAC-Subscriber_Stats_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Jordan.Hill@ehi.com;;
        "Motability-Commercial_Area_Report-"* )
            EMAIL_RECEIVERS=MichaelPaul.Kemp@valexa.com;;
        "RSA-Ultra_Prestige_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Kelly.cartman@uk.rsagroup.com,bec.jones@uk.rsagroup.com,fiona.odonnell@uk.rsagroup.com,andrew.burton@uk.rsagroup.com,stevie.binns@uk.rsagroup.com;;
	    "QBE-On_Hire_Report-"*  |\
	    "QBE-Defaulted_Claims_Report-"*  |\
	    "QBE-Subscriber_SLA_Rejection_Report-"* )
            ACTIVE=1
            EMAIL_RECEIVERS=Paul.Bone@uk.qbe.com,Jacqueline.Britton@uk.qbe.com;;
	    "RSA-On_Hire_Report-"*  )
            ACTIVE=1
            EMAIL_RECEIVERS=Kelly.cartman@uk.rsagroup.com,bec.jones@uk.rsagroup.com,neil.whittle@uk.rsagroup.com,fiona.odonnell@uk.rsagroup.com,andrew.burton@uk.rsagroup.com,Theresa.bow@uk.rsagroup.com;;
	    "RSA-Insurer_Penalty_Charge_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Kelly.cartman@uk.rsagroup.com,bec.jones@uk.rsagroup.com,neil.whittle@uk.rsagroup.com,fiona.odonnell@uk.rsagroup.com,andrew.burton@uk.rsagroup.com,Theresa.bow@uk.rsagroup.com;;
        "RSA-Commercial_Area_Report-"* )
            EMAIL_RECEIVERS=MichaelPaul.Kemp@valexa.com;;
        "Auxillis-Moved_To_InvoicePaymentLogged_Claim_Detail_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=cpt@albanyassistance.co.uk,susan.harker@albanyassistance.co.uk,protocol@albanyassistance.co.uk;;
	    "Auxillie-FNOL_Notifications_Report_DLG-"* )
	        ACTIVE=1
	        EMAIL_RECEIVERS=protocol@albanyassistance.co.uk;;
	    "Auxillis-Invoice_Notifications_Report_DLG-"* )
	        ACTIVE=1
	        EMAIL_RECEIVERS=protocol@albanyassistance.co.uk;;
        "DLG-Insurer_User_State_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=jamie.willshaw@directlinegroup.co.uk,tam.bedford@directlinegroup.co.uk,Andy.A.Cooper@directlinegroup.co.uk,Jonathan.Clarke@directlinegroup.co.uk,UAC.Governance@directlinegroup.co.uk;;
        "RSA-Average_Time_To_Allocate_Work-"* )
            EMAIL_RECEIVERS=MichaelPaul.Kemp@valexa.com;;
#        "ERAC-Supplementary_Consolidation_Report-"* )
#            EMAIL_RECEIVERS=MichaelPaul.Kemp@valexa.com,ben.richmond@valexa.com;;
        "RSA-FixedFee_Task_Export_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Kelly.cartman@uk.rsagroup.com,Stacie.Warrington@uk.rsagroup.com,Cheryl.pearson@uk.rsagroup.com,greg.wakeling@uk.rsagroup.com,linda.barr@uk.rsagroup.com,sue.jubb@uk.rsagroup.com,andrew.burton@uk.rsagroup.com,steve.anderson@uk.rsagroup.com,christopher.cross@uk.rsagroup.com,bec.jones@uk.rsagroup.com,abdul.shahbaz@uk.rsagroup.com,fiona.odonnell@uk.rsagroup.com,Theresa.bow@uk.rsagroup.com;;
        "RSA-Subscriber_Task_Export_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Kelly.cartman@uk.rsagroup.com,linda.barr@uk.rsagroup.com,andrew.burton@uk.rsagroup.com,Stacie.Warrington@uk.rsagroup.com,steve.anderson@uk.rsagroup.com,christopher.cross@uk.rsagroup.com,greg.wakeling@uk.rsagroup.com,sue.jubb@uk.rsagroup.com,Cheryl.pearson@uk.rsagroup.com,bec.jones@uk.rsagroup.com,abdul.shahbaz@uk.rsagroup.com,fiona.odonnell@uk.rsagroup.com,Theresa.bow@uk.rsagroup.com;;
        "ERAC-TotalLoss_LiabilityUpdate_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=totallossteam@imsolutionslimited.co.uk,courtneyp@imsolutionslimited.co.uk;;
        "DLG-Audit_Facility_Report-"* |\
        "DLG-Audit_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Tam.Bedford@directlinegroup.co.uk;;
        "ERAC-Audit_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Lauren.D.Walker@ehi.com,Nicola.A.Imlach@ehi.com,Sam.Flynn@ehi.com;;
        "RSA-Attachment_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=benjamin.mills@uk.rsagroup.com,bec.jones@uk.rsagroup.com,Andrew.burton@uk.rsagroup.com,linda.barr@uk.rsagroup.com,Stacie.warrington@uk.rsagroup.com,fiona.odonnell@uk.rsagroup.com,andrew.burton@uk.rsagroup.com,Theresa.bow@uk.rsagroup.com,stevie.binns@uk.rsagroup.com;;
        "ERS-Weekly_Action_Management_Report-"* )
            EMAIL_RECEIVERS=MichaelPaul.Kemp@valexa.com,terry.joseph@ers.com;;
        "ERS-Last_ECD_Update_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=jonathan.lee@ers.com,terry.joseph@ers.com;;
        "ERAC-User_State_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=andrew.r.findlay@ehi.com;;
        "ERAC-Repair_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=chris@crashworth.co.uk,Aisling.L.Tuft@ehi.com,Lauren.D.Walker@ehi.com,odonnellj@uk.innovation-group.com;;
        "RSA-Default_Claims_Report-"* )
	        ACTIVE=1
            EMAIL_RECEIVERS=Kelly.cartman@uk.rsagroup.com,linda.barr@uk.rsagroup.com,lee.price@uk.rsagroup.com,stacie.warrington@uk.rsagroup.com,andrew.burton@uk.rsagroup.com,Theresa.bow@uk.rsagroup.com;;
        "ERAC-CHOX_Data_Dump-"* )
            ACTIVE=2
            FTP_CLIENT=EHI;;
#            EMAIL_RECEIVERS=jenny.graham@sherwoodts.co.uk,Nicola.A.Imlach@ehi.com,Christopher.J.Smith@ehi.com,Lauren.D.Walker@ehi.com;;
        "ERAC-Daily_Data_Export-"* )
            ACTIVE=4
            FTP_CLIENT=EHI;;
#            EMAIL_RECEIVERS=Nicola.A.Imlach@ehi.com,Christopher.J.Smith@ehi.com,Lauren.D.Walker@ehi.com;;
# This AE Processed Notification report now has its own template
        "RSA-CHOXinvoice"* )
            ACTIVE=4
            FTP_CLIENT=RSA;;
        "LVCHOXinvoice"* )
            ACTIVE=4
            FTP_CLIENT=LV;;
        "DLG-CREDIT_HIRE_NEW_NOTIFICATIONS_"* )
            ACTIVE=4
            FTP_CLIENT=DLG;;
        "AccidentExchange-Invoice_Notifications_Report_"* )
            ACTIVE=1
            EMAIL_RECEIVERS=kmartin@accidentexchange.com,ymiah@accidentexchange.com,proche@accidentexchange.com,ltrueman@accidentexchange.com;;
        "LV-Monthly_Dashboard_Report-"* |\
        "LV-Weekly_Dashboard_Report-"* )
            ACTIVE=1
            EMAIL_RECEIVERS=Neil.Garrett@audatex.co.uk,Dominic.Czechak@audatex.co.uk;;
	    "LV-Closed_Claim_Report-"* )
	        ACTIVE=1
	        EMAIL_RECEIVERS=andrew.seedhouse@lv.com;;
	    "Kindertons-Invoice_Upload_Report-"* )
	        ACTIVE=5
#            EMAIL_RECEIVERS=thomas.maddock@kindertons.co.uk;;
            FTP_CLIENT=Kindertons;;
	    "Kindertons-Liability_Update_Report-"* )
	        ACTIVE=5
#	        EMAIL_RECEIVERS=richard.bettley@kindertons.co.uk,thomas.beech@kindertons.co.uk,luke.rush@kindertons.co.uk,Stephen.gilligan@kindertons.com;;
            FTP_CLIENT=Kindertons;;
	    "Keoghs-DailyReport-"* )
	        ACTIVE=1
	        EMAIL_RECEIVERS=alane@keoghs.co.uk,lkearsley@keoghs.co.uk;;
        *)
            EMAIL_RECEIVERS=DL-UKBR-Audatex-penguin-reporting@audatex.com;;
    esac

}


if [ $# -ne 3 ]
then
    echo "Usage: $0 <input directory> <processed directory> <output directory>"
    echo
    exit 1
fi

#BCC_RECIPIENTS=Elliot.Roberts@Valexa.com
#BCC_RECIPIENTS=Isabelle.Lecoeuche@audatex.co.uk
#BCC_RECIPIENTS=John.Strawhorne@Valexa.com
BCC_RECIPIENTS=MichaelPaul.Kemp@Valexa.com,Robert.Hon@audatex.co.uk

REPORT_DIR=$1
PROCESSED_DIR=$2
OUTPUT_DIR=$3

CWD=`pwd`
OS=`uname`

echo "**************************************************************************"
echo "**       Converting reports to Excel: `date`      **"
echo "**************************************************************************"

if [ "${OS}" = "Darwin" ]
then
    echo "We are running on OS X...."
    GENERATE_COMMAND='java -Xms1G -Xmx4G -jar /Users/john/Projects/Netbeans/chox/reportToExcel/target/reportToExcel-0.1.one-jar.jar'
else
    echo "We are running on Linux...."
    GENERATE_COMMAND='java -Xms1G -Xmx5G -jar  /home/chox/bin/reportToExcel.jar'
fi

pushd ${REPORT_DIR}
#maxSize=9000
for reportFile in $(ls *.csv);
do

    getEmailReceivers ${reportFile}
    if [ "${ACTIVE}" -eq "4" ]; then
# SFTP unzipped and without password
        if [ "${MODE}" == "EXPORT" -o "${MODE}" == "NOEMAIL" ]; then
            /home/chox/bin/transferToGateway.sh ${FTP_CLIENT} ${reportFile}
        fi
        /bin/mv ${reportFile} ${CWD}/${PROCESSED_DIR}/${reportFile}
    elif [ "${ACTIVE}" -eq "1" ]; then
        SUBJECT="CHOX: MI Report "${reportFile}
        getPassword ${reportFile}
        zipFile=${reportFile/%.csv}.zip
        /usr/bin/zip -P ${PASSWORD} ${zipFile} ${reportFile}
        RECIPIENTS=${EMAIL_RECEIVERS}
        if [ "${MODE}" == "EXPORT" ]; then
/usr/bin/mutt -s "${SUBJECT}" -b ${BCC_RECIPIENTS} -a ${zipFile} -- ${RECIPIENTS}  <<  --EOF--
Please find the attached csv report:
        ${zipFile}
--EOF--
        fi
        /bin/mv ${zipFile} ${CWD}/${PROCESSED_DIR}/${zipFile}
        /bin/rm ${reportFile}
    else
	echo "Report not active: moving ${reportFile} to  ${CWD}/${PROCESSED_DIR}"
	/bin/mv ${reportFile} ${CWD}/${PROCESSED_DIR}
    fi
done

for reportFile in $(ls *.txt);
do
    xlsFile=${reportFile/%.txt}.xls
    ${GENERATE_COMMAND} ${CWD}/${REPORT_DIR}/${reportFile} ${CWD}/${OUTPUT_DIR}/${xlsFile}
    /bin/mv ${CWD}/${REPORT_DIR}/${reportFile} ${CWD}/${PROCESSED_DIR}/${reportFile}
    getEmailReceivers ${reportFile}
    pushd ${CWD}/${OUTPUT_DIR}
    SUBJECT="CHOX: MI Report "${xlsFile}
    if [ "${ACTIVE}" -eq "3" ]; then
# Email unzipped and without password
        if [ "${MODE}" == "EXPORT" ]; then
/usr/bin/mutt -s "${SUBJECT}" -b ${BCC_RECIPIENTS} -a ${CWD}/${OUTPUT_DIR}/${xlsFile} -- ${RECIPIENTS}  <<  --EOF--
Please find the attached excel report:
        ${xlsFile}
--EOF--
        fi
    elif [ "${ACTIVE}" -eq "4" ]; then
# SFTP with trigger, unzipped and without password
        if [ "${MODE}" == "EXPORT" -o "${MODE}" == "NOEMAIL" ]; then
        /home/chox/bin/transferToGatewayWithTrigger.sh ${FTP_CLIENT} ${CWD}/${OUTPUT_DIR}/${xlsFile}
        fi
    elif [ "${ACTIVE}" -eq "5" ]; then
    # SFTP unzipped and without password
        if [ "${MODE}" == "EXPORT" -o "${MODE}" == "NOEMAIL" ]; then
            /home/chox/bin/transferToGateway.sh ${FTP_CLIENT} ${CWD}/${OUTPUT_DIR}/${xlsFile}
        fi
    else
    getPassword ${reportFile}

#    actualsize=$(du -k "${CWD}/${OUTPUT_DIR}/${reportFile/%.txt}.xls" | cut -f 1)
#    if [ $actualsize -ge $maxSize ]; then
#        /bin/gzip ${CWD}/${OUTPUT_DIR}/${reportFile/%.txt}.xls
#        zipFile=${reportFile/%.txt}.xls.gz
#    else
#        zipFile=${reportFile/%.txt}.xls
#    fi

    zipFile=${reportFile/%.txt}.zip
    /usr/bin/zip -P ${PASSWORD} ${zipFile} ${xlsFile}

    if [ "${ACTIVE}" -eq "1" ]; then
#   email zipped with password
        RECIPIENTS=${EMAIL_RECEIVERS}
        if [ "${MODE}" == "EXPORT" ]; then
/usr/bin/mutt -s "${SUBJECT}" -b ${BCC_RECIPIENTS} -a ${CWD}/${OUTPUT_DIR}/${zipFile} -- ${RECIPIENTS}  <<  --EOF--
Please find the attached excel report:
        ${zipFile}
--EOF--
        fi
    elif [ "${ACTIVE}" -eq "2" ]; then
        if [ "${MODE}" == "EXPORT" -o "${MODE}" == "NOEMAIL" ]; then
#   Deliver password-protected zip file via SFTP
        /home/chox/bin/transferToGatewayWithTrigger.sh ${FTP_CLIENT} ${CWD}/${OUTPUT_DIR}/${zipFile}
        fi
    else # ACTIVE == 0
#   email password-protected zip file to internal (bcc) recipients only
        RECIPIENTS=${BCC_RECIPIENTS}

        if [ "${MODE}" == "EXPORT" ]; then
/usr/bin/mutt -s "${SUBJECT}" -a ${CWD}/${OUTPUT_DIR}/${zipFile} -- ${RECIPIENTS}  <<  --EOF--
Please find the attached excel report:
    ${zipFile}

Note that this report has NOT been directly sent to a client (internal only).
If activated, this would be sent to the following addresses: ${EMAIL_RECEIVERS}.
--EOF--
        fi
    fi
    /bin/rm ${zipFile}
    fi
    popd
done

popd

echo "**************************************************************************"

exit 0

