package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.WeekSummary;
import idas.chox.service.reports.viewdata.WeekSummaryReportObject;

public class AdminWeeklyOverviewReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(AdminWeeklyOverviewReport.class);

    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_AdminWeeklyOverviewReport.xls";
    }

    @Override
    public String getReportCode() {
        return "RPT018";
    }


    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public AdminWeeklyOverviewReport() {
        reportParameterNames = new ArrayList<>();
    }

    @Override
    public Map<String, Object> getReportParameters() throws Exception {

        Map<String, Object> reportParameters = new HashMap<>();
        Date startDate = null;
        Date endDate = null;
        WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));

        try {
            

            Integer selectedSupplierId = -1;
            Integer selectedInsurerId = -1;
            
            if(((String[]) externalParameter.get("supplierId"))!=null){
                selectedSupplierId = TextHelper.getId(((String[]) externalParameter.get("supplierId"))[0]);
            }

            if(((String[]) externalParameter.get("insurerId"))!=null){
                selectedInsurerId = TextHelper.getId(((String[]) externalParameter.get("insurerId"))[0]);
            }

            if(((String[]) externalParameter.get("DateStart"))!=null){
                startDate = DateHelper.parse(((String[]) externalParameter.get("DateStart"))[0]);
            }

            if(((String[]) externalParameter.get("DateStart"))!=null){
                endDate = DateHelper.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            } 

            if(endDate == null || startDate == null) {
                throw new Exception("Start and End dates cannot be empty.");
            }

            if(endDate.before(startDate)) {
                throw new Exception("End date (" + endDate.toString() + ") is before start date (" + startDate.toString() +  ") ");
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
                    reportHeaderTitle = "CHOX Report - Insurer Weekly Overview Report";

                    if (selectedSupplierId>0) {
                        selectedOrgName = getChorganisation(selectedSupplierId).getName();
                    }

                } else {

                    Chorganisation chorg = currentUser.getChorganisation();
                    selectedSupplierId = chorg.getId();
                    userOrgName = chorg.getName();

                    userOrgLabel = "Credit Hire Organisation";
                    selectedOrgLabel = "Insurer";
                    reportHeaderTitle = "CHOX Report - Credit Hire Weekly Overview Report";

                    if (selectedInsurerId>0) {
                        selectedOrgName = getInsurer(selectedInsurerId).getName();
                    }

                }
                
            }

            /*
             *  GET START DATE AND GET END DATE (dare restrictions) are commented out for bug 997. Date restrictions are implemented in UI (Extjs). 
             *  
             */
            // GET START DATE
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);
            Date dateFirstMonday = startDate;
            Date currentMonday = dateFirstMonday;

            // GET END DATE
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);
            Date dateLastSunday = c2.getTime();
            
            List<WeekSummary> weekSummaries = new ArrayList<>();

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

                LOG.debug("StartDate = {}, EndDate = {}", startOfTheWeek, endOfTheWeek);
                HashMap queryParameters = new HashMap();
                queryParameters.put("pSelectedStartDate", startOfTheWeek);
                queryParameters.put("pSelectedEndDate", endOfTheWeek);
                queryParameters.put("pInsId", selectedInsurerId);
                queryParameters.put("pChorganisationId", selectedSupplierId);

                List result = reportDataService.getReportData(query, queryParameters);

                for (Object o : result) {

                    Map data = (Map) o;
                    data.put("weekCycleDate", DateHelper.getLocalDateFormat().format(startOfTheWeek));
                    WeekSummary weekSummary = WeekSummary.getObject(data);

                    iClaimsNotificationAcceptedByInsurerHis += weekSummary.getClaimsNotificationAcceptedByInsurer();
                    iClaimsInvoicedHis += weekSummary.getClaimsInvoiced();
                    iInvoicePaidByInsurerHis += weekSummary.getClaimsPaid();
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
            LOG.error("Exception thrown generating Admin Weekly Overview Report: {} [user={}]", ex.getMessage(), currentUser.getId());
            LOG.error("Report params were: startDate={}, endDate={}", startDate, endDate);
            throw ex;
        }

        return reportParameters;
    }

    private String getReportQuery(boolean isInsurer){

        StringBuilder sb = new StringBuilder();

        if (isInsurer) {

            sb.append("select ");
            sb.append("((select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) < :pSelectedStartDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) < :pSelectedStartDate AND a.new_status not in ('ClaimClosed','InvoicePaymentLogged','PaymentReceived','ClaimRejectionAccepted', 'InvoiceRejectionAccepted', 'ManualInvoicePaid'))) as claimsBFwd, ");
            sb.append("((select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status not in ('ClaimClosed','InvoicePaymentLogged','PaymentReceived','ClaimRejectionAccepted', 'InvoiceRejectionAccepted', 'ManualInvoicePaid'))) as claimsCFwd, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id and date(update_date) between :pSelectedStartDate and :pSelectedEndDate and ((new_status='ClaimUnacknowledgedUnrouted' and original_status!='ClaimClosed' and original_status!='ClaimRejected') or (new_status='AwaitingInvoiceData' and claim_type=").append(ClaimType.TPI.getClaimTypeValue()).append("))) as claimsNotification, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND (a.invoice_id is NOT NULL) AND a.new_status in ('ClaimClosed') and a.claim_id not in (select claim_id from audit_trail where reverted=false and date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (new_status='InvoicePaymentLogged' or original_status='InvoicePaymentLogged'))) as insurerClaimsClosed, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id AND new_status = 'InvoicePaymentLogged' and date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate) as claimsPaid, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.new_status= 'ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))) as claimsNotificationContestedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimRejectionContested', 'ClaimRejected', 'ClaimPending', 'ClaimUpdatedByEngineer')) as claimsPendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND ((a.new_status = 'AwaitingCarHireInfo' AND not exists (select * from rpt_claim_audit_trail c where c.claim_id=a.claim_id and c.new_status in ('AwaitingCarHireInfo') and date(c.update_date) < date(a.update_date))) or (a.new_status = 'AwaitingInvoiceData' and b.claim_type=").append(ClaimType.TPI.getClaimTypeValue()).append("))) as claimsNotificationAcceptedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimReferredToFNOL')) as claimsFNOLReferrals, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.original_status in ('ClaimReferredToFNOL')) as claimsFNOLCreatedByInsurer, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_invoice WHERE (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id AND date(created_date) BETWEEN :pSelectedStartDate AND :pSelectedEndDate) as claimsInvoiced, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceRejectionAccepted')) as invoiceContestedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'InvoiceEscalated', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned')) as invoicePendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as invoiceApprovedByInsurer, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail a where (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') and date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate and not exists (select * from rpt_claim_audit_trail r where r.claim_id = a.claim_id and r.update_date BETWEEN a.update_date and :pSelectedEndDate and r.original_status = 'InvoicePaymentLogged' and r.new_status != 'PaymentReceived')) as invoicePaidByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as claimsToBeInvoiced ");
            sb.append("from insurer insurer where insurer.id = :pInsId ");
        } else {
            sb.append("select ");
            sb.append("((select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) < :pSelectedStartDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) < :pSelectedStartDate AND a.new_status not in ('ClaimClosed','PaymentReceived','ClaimRejectionAccepted', 'InvoiceRejectionAccepted', 'ManualInvoicePaid'))) as claimsBFwd, ");
            sb.append("((select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status not in ('ClaimClosed','PaymentReceived','ClaimRejectionAccepted', 'InvoiceRejectionAccepted', 'ManualInvoicePaid'))) as claimsCFwd, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail WHERE (insurer_id = :pInsId or :pInsId < 0) AND chorganisation_id=chorganisation.id and date(update_date) between :pSelectedStartDate and :pSelectedEndDate and ((new_status='ClaimUnacknowledgedUnrouted' and original_status!='ClaimClosed' and original_status!='ClaimRejected') or (new_status = 'AwaitingInvoiceData' and claim_type=").append(ClaimType.TPI.getClaimTypeValue()).append("))) as claimsNotification, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND (a.invoice_id is NOT NULL) AND a.new_status in ('ClaimClosed') and a.claim_id not in (select claim_id from audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (new_status='InvoicePaymentLogged' or original_status='InvoicePaymentLogged'))) as insurerClaimsClosed, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail WHERE (insurer_id = :pInsId or :pInsId < 0) AND chorganisation_id=chorganisation.id AND new_status = 'InvoicePaymentLogged' and date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate) as claimsPaid, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.new_status='ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))) as claimsNotificationContestedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimRejectionContested', 'ClaimRejected', 'ClaimPending', 'ClaimUpdatedByEngineer')) as claimsPendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND ((a.new_status in ('AwaitingCarHireInfo') AND a.reverted=false AND not exists (select * from rpt_claim_audit_trail c where c.claim_id=a.claim_id and c.new_status in ('AwaitingCarHireInfo') and date(c.update_date) < date(a.update_date))) or (a.new_status = 'AwaitingInvoiceData' and b.claim_type=").append(ClaimType.TPI.getClaimTypeValue()).append("))) as claimsNotificationAcceptedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimReferredToFNOL')) as claimsFNOLReferrals, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.original_status in ('ClaimReferredToFNOL')) as claimsFNOLCreatedByInsurer, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_invoice WHERE (insurer_id = :pInsId or :pInsId < 0) AND chorganisation_id=chorganisation.id AND date(created_date) BETWEEN :pSelectedStartDate AND :pSelectedEndDate) as claimsInvoiced, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceRejectionAccepted')) as invoiceContestedByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'InvoiceEscalated', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer', 'InvoiceUnassigned', 'ManualInvoiceBREApproved', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceUnassigned')) as invoicePendingByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as invoiceApprovedByInsurer, ");
            sb.append("(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail a WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.new_status='InvoicePaymentLogged' and date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate and not exists (select * from rpt_claim_audit_trail r where r.claim_id = a.claim_id and r.update_date BETWEEN a.update_date and :pSelectedEndDate and r.original_status = 'InvoicePaymentLogged' and r.new_status != 'PaymentReceived')) as invoicePaidByInsurer, ");
            sb.append("(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.insurer_id = :pInsId or :pInsId < 0) AND a.chorganisation_id=chorganisation.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as claimsToBeInvoiced ");
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
        } catch (Exception e) {
            LOG.error("Exception thrown getting Chorganisation from id={}: {}", orgId, e.getMessage());
        }

        return chorg;
    }

    private Insurer getInsurer(int orgId) {
        Insurer ins = new Insurer();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", orgId));
            ins = (Insurer) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Exception thrown getting Insurer from id={}: {}", orgId, e.getMessage());
        }

        return ins;
    }

    @Override
    public short[] getColumnsToHide() {
        return null;
    }

    @Override
    public boolean isBrandingReportFormat() {
        return externalParameter.get("isBrandingReport")==null ? false : (Boolean)externalParameter.get("isBrandingReport");
    }
}