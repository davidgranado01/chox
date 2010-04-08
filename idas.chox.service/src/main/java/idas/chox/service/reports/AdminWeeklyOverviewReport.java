package idas.chox.service.reports;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.WeekSummary;
import idas.chox.service.reports.viewdata.WeekSummaryReportObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AdminWeeklyOverviewReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private BaseDataService baseDataService;

    @Override
    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    @Override
    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_AdminWeeklyOverviewReport.xls";
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public AdminWeeklyOverviewReport() {
        reportParameterNames = new ArrayList<String>();
    }

    @Override
    public HashMap getReportParameters() {

        HashMap reportParameters = new HashMap();

        try {
            
            WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));

            Integer selectedSupplierId = -1;
            Integer selectedInsurerId = -1;
            Date startDate = null;
            Date endDate = null;
            
            if(((String[]) externalParameter.get("supplierId"))!=null){
                selectedSupplierId = TextHelper.getId(((String[]) externalParameter.get("supplierId"))[0]);
            }

            if(((String[]) externalParameter.get("insurerId"))!=null){
                selectedInsurerId = TextHelper.getId(((String[]) externalParameter.get("insurerId"))[0]);
            }

            if(((String[]) externalParameter.get("DateStart"))!=null){
                startDate = DateHelper.Parse(((String[]) externalParameter.get("DateStart"))[0]);
            }

            if(((String[]) externalParameter.get("DateStart"))!=null){
                endDate = DateHelper.Parse(((String[]) externalParameter.get("DateEnd"))[0]);
            }

            String userOrgLabel = "";
            String userOrgName = "";
            String selectedOrgName = "All";
            String selectedOrgLabel = "";
            String reportHeaderTitle = "";

            if(!currentUser.isCHOXAdmin()){
                
                if (currentUser.getInsurer()!=null) {

                    Insurer ins = currentUser.getInsurer();
                    selectedInsurerId = ins.getId();
                    userOrgName = ins.getName();

                    userOrgLabel = "Insurer";
                    selectedOrgLabel = "Credit Hire Organisation";
                    reportHeaderTitle = "iDAS CHOX Report - Insurer Weekly Overview Report";

                    if (selectedSupplierId>0) {
                        selectedOrgName = getChorganisation(selectedSupplierId).getName();
                    }

                } else {

                    Chorganisation chorg = currentUser.getChorganisation();
                    selectedSupplierId = chorg.getId();
                    userOrgName = chorg.getName();

                    userOrgLabel = "Credit Hire Organisation";
                    selectedOrgLabel = "Insurer";
                    reportHeaderTitle = "iDAS CHOX Report - Credit Hire Weekly Overview Report";

                    if (selectedInsurerId>0) {
                        selectedOrgName = getInsurer(selectedInsurerId).getName();
                    }

                }
                
            }

            // GET START DATE
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);
            Integer dayOfWeek1 = c1.get(Calendar.DAY_OF_WEEK);
            c1.add(Calendar.DATE, -dayOfWeek1 + 2);
            Date dateFirstMonday = c1.getTime();
            Date currentMonday = dateFirstMonday;

            // GET END DATE
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);
            Integer dayOfWeek2 = c2.get(Calendar.DAY_OF_WEEK);
            c2.add(Calendar.DATE, -dayOfWeek2 + 2);
            Date dateLastMonday = c2.getTime();
            c2.add(Calendar.DATE, 6);
            Date dateLastSunday = c2.getTime();
            
            List<WeekSummary> weekSummaries = new ArrayList<WeekSummary>();

            Integer iClaimsInvoicedHis = 0;
            Integer iInvoicePaidByInsurerHis = 0;
            Integer iClaimsNotificationAcceptedByInsurerHis = 0;

            do {

                Calendar c = Calendar.getInstance();
                c.setTime(currentMonday);
                Date startOfTheWeek = c.getTime();
                c.add(Calendar.DATE, 6);
                Date endOfTheWeek = c.getTime();

                boolean isIns = (currentUser.getInsurer()!=null);
                String query = getReportQuery(isIns);

                HashMap queryParameters = new HashMap();
                queryParameters.put("pSelectedStartDate", startOfTheWeek);
                queryParameters.put("pSelectedEndDate", endOfTheWeek);
                queryParameters.put("pInsId", selectedInsurerId);
                queryParameters.put("pChorganisationId", selectedSupplierId);

                List result = baseDataService.externalQuery(query, queryParameters);

                for (Object o : result) {

                    Map data = (Map) o;
                    data.put("weekCycleDate", DateHelper.LocalDateFormat.format(startOfTheWeek));
                    WeekSummary weekSummary = WeekSummary.getObject(data);

                    iClaimsNotificationAcceptedByInsurerHis = iClaimsNotificationAcceptedByInsurerHis + weekSummary.getClaimsNotificationAcceptedByInsurer();
                    iClaimsInvoicedHis = iClaimsInvoicedHis + weekSummary.getClaimsInvoiced();
                    iInvoicePaidByInsurerHis = iInvoicePaidByInsurerHis + weekSummary.getClaimsPaid();
                    weekSummary.setInvoicePaidAsPercentageOfInvoicing(iClaimsInvoicedHis, iInvoicePaidByInsurerHis);

                    weekSummaries.add(weekSummary);
                }
        
                c.add(Calendar.DATE, 1);
                currentMonday = c.getTime();

            } while (currentMonday.before(dateLastSunday));
            
            WeekSummaryReportObject reportObject = new WeekSummaryReportObject();
            reportObject.setWeekCycleFrom(dateFirstMonday);
            reportObject.setWeekCycleTo(dateLastSunday);
            reportObject.setCreatedDate(new Date());

            reportParameters.put("weekSummaries", weekSummaries);
            reportParameters.put("reportObj", reportObject);
            reportParameters.put("userOrgLabel", userOrgLabel);
            reportParameters.put("userOrgName", userOrgName);
            reportParameters.put("selectedOrgLabel", selectedOrgLabel);
            reportParameters.put("selectedOrgName", selectedOrgName);
            reportParameters.put("reportHeaderTitle", reportHeaderTitle);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return reportParameters;
    }

    private String getReportQuery(boolean isInsurer){

        StringBuffer sb = new StringBuffer();

        if(isInsurer){

            sb.append("select ");
            sb.append("((select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) < :pSelectedStartDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) < :pSelectedStartDate AND a.new_status not in ('ClaimClosed','InvoicePaymentLogged','PaymentReceived','ClaimRejectionAccepted'))) as claimsBFwd, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id and date(update_date) between :pSelectedStartDate and :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted' and original_status!='ClaimClosed') as claimsNotification, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, min(update_date) as min_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.min_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.original_status in ('ClaimClosed')) as reopenClaims, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND a.claim_reason_of_rejection in (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%')) as claimsOutOfScope, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND a.claim_reason_of_rejection not in (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%')) as rejectedClaims, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.invoice_id is NULL) AND a.new_status in ('ClaimClosed')) as nonThisInsurerClaims, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.invoice_id is NOT NULL) AND a.new_status in ('ClaimClosed') and a.claim_id not in (select claim_id from audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (new_status='InvoicePaymentLogged' or original_status='InvoicePaymentLogged'))) as insurerClaimsClosed, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id AND new_status = 'InvoicePaymentLogged' and date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate) as claimsPaid, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND (b.reason_of_rejection_id != (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%') OR b.reason_of_rejection_id is null)) as claimsNotificationContestedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimRejectionContested', 'ClaimRejected', 'ClaimPending', 'ClaimUpdatedByEngineer')) as claimsPendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo')) as claimsNotificationAcceptedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimReferredToFNOL')) as claimsFNOLReferrals, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.original_status in ('ClaimReferredToFNOL')) as claimsFNOLCreatedByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice WHERE (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id AND date(created_date) BETWEEN :pSelectedStartDate AND :pSelectedEndDate) as claimsInvoiced, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceRejectionAccepted')) as invoiceContestedByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'InvoiceEscalated', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as invoicePendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as invoiceApprovedByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id AND new_status='InvoicePaymentLogged' and date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate) as invoicePaidByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as claimsToBeInvoiced ");
            sb.append("from insurer insurer where insurer.id = :pInsId ");
        }else{
            sb.append("select ");
            sb.append("((select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) < :pSelectedStartDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) < :pSelectedStartDate AND a.new_status not in ('ClaimClosed','InvoicePaymentLogged','PaymentReceived','ClaimRejectionAccepted'))) as claimsBFwd, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where (insurer_id = :pInsId or :pInsId < 0) AND chorganisation_id=chorganisation.id and date(update_date) between :pSelectedStartDate and :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted' and original_status!='ClaimClosed') as claimsNotification, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, min(update_date) as min_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.min_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.original_status in ('ClaimClosed')) as reopenClaims, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND a.claim_reason_of_rejection in (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%')) as claimsOutOfScope, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND a.claim_reason_of_rejection not in (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%')) as rejectedClaims, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.invoice_id is NULL) AND a.new_status in ('ClaimClosed')) as nonThisInsurerClaims, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.invoice_id is NOT NULL) AND a.new_status in ('ClaimClosed') and a.claim_id not in (select claim_id from audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (new_status='InvoicePaymentLogged' or original_status='InvoicePaymentLogged'))) as insurerClaimsClosed, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail where (insurer_id = :pInsId or :pInsId < 0) AND chorganisation_id=chorganisation.id AND new_status = 'InvoicePaymentLogged' and date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate) as claimsPaid, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND (b.reason_of_rejection_id != (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%') OR b.reason_of_rejection_id is null)) as claimsNotificationContestedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimRejectionContested', 'ClaimRejected', 'ClaimPending', 'ClaimUpdatedByEngineer')) as claimsPendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo')) as claimsNotificationAcceptedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimReferredToFNOL')) as claimsFNOLReferrals, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.original_status in ('ClaimReferredToFNOL')) as claimsFNOLCreatedByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice WHERE (insurer_id = :pInsId or :pInsId < 0) AND chorganisation_id=chorganisation.id AND date(created_date) BETWEEN :pSelectedStartDate AND :pSelectedEndDate) as claimsInvoiced, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceRejectionAccepted')) as invoiceContestedByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'InvoiceEscalated', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as invoicePendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as invoiceApprovedByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail where (insurer_id = :pInsId or :pInsId < 0) AND chorganisation_id=chorganisation.id AND new_status='InvoicePaymentLogged' and date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate) as invoicePaidByInsurer, ");
            sb.append("(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as claimsToBeInvoiced ");
            sb.append("from chorganisation chorganisation where chorganisation.id = :pChorganisationId ");
        }

        return sb.toString();
    }

    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return chorg;
    }

    private Insurer getInsurer(int orgId) {
        Insurer ins = new Insurer();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", orgId));
            ins = (Insurer) baseDataService.getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return ins;
    }
    
}