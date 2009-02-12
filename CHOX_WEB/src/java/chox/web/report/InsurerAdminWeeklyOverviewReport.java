/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.services.DataService;
import chox.services.ChorganisationService;
import chox.web.report.viewdata.WeekSummary;
import chox.web.report.viewdata.WeekSummaryReportObject;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */


public class InsurerAdminWeeklyOverviewReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;
    private ChorganisationService chorganisationService;

    public InsurerAdminWeeklyOverviewReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_InsurerAdminWeeklyOverviewReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public HashMap getReportParameters() {
        
        HashMap reportParameters = new HashMap();
        
        //monday as start of week
        //sunday as end of week
        
        try {
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            Insurer ins = currentUser.getUser().getInsurer();
            
            String dataStartRaw = ((String[]) externalParameter.get("DateStart"))[0];
            String dateEndRaw = ((String[]) externalParameter.get("DateEnd"))[0];
            String supplierId = ((String[]) externalParameter.get("supplierId"))[0];
            
            String strChorganisationName = "All";
            if(!supplierId.equalsIgnoreCase("")){
                    
                // System.out.println(supplierId + " ::: supplierId ::::::::::::::::: " + Integer.parseInt(supplierId));
                // Chorganisation chorganisation = chorganisationService.getObject(Integer.parseInt(supplierId));
                // strChorganisationName = chorganisation.getName();
            }
            
            Date startDate = DateHelper.LocalDateFormat.parse(dataStartRaw);//user selected start date of report
            Date endDate = DateHelper.LocalDateFormat.parse(dateEndRaw);//user selected end date of report
            
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);
            Integer dayOfWeek1 = c1.get(Calendar.DAY_OF_WEEK);
            c1.add(Calendar.DATE, -dayOfWeek1 + 2);
            Date dateFirstMonday = c1.getTime();//actual start date of report
            
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);
            Integer dayOfWeek2 = c2.get(Calendar.DAY_OF_WEEK);
            c2.add(Calendar.DATE, -dayOfWeek2 + 2);
            Date dateLastMonday = c2.getTime();
            c2.add(Calendar.DATE, 6);
            Date dateLastSunday = c2.getTime();//actual end date of report
            
            Date currentMonday = dateFirstMonday;
            List<WeekSummary> weekSummaries = new ArrayList<WeekSummary>();
            
            do {
                
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonday);
                Date startOfTheWeek = c.getTime();
                c.add(Calendar.DATE, 6);
                Date endOfTheWeek = c.getTime();

                HashMap queryParameters = new HashMap();
                
                /*
                String query = "select 1, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where insurer_id = insurer.id and update_date between :pSelectedStartDate and :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted') as newChoxNotification, "
                + "0 as claimWithdrawn, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where insurer_id = insurer.id and update_date < :pSelectedStartDate and new_status='ClaimUnacknowledgedUnrouted') as existingClaim, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where insurer_id = insurer.id and update_date <= :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted') as cumulativeClaim, "
                + "0 as claimOutOfScope, "
                + "0 as claimInScope, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimRejected','ClaimRejectionAccepted','ClaimRejectionContested')) as claimNotificationContestedByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimRejectionContested','ClaimReferredToFNOL','ClaimReferredToEngineer','ClaimUnacknowledgedUnrouted','ClaimUnacknowledgedRouted', 'ClaimPending')) as claimPendingByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingCarHireInfo')) as claimNotificationAcceptedByRsa, "
                + "0 as inScopeClaimContestedPercentage, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimReferredToFNOL')) as claimFnolCreatedByRsa, "
                //+ "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated')) as claimInvoiced, "
                + "((select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where insurer_id = insurer.id and created_date between :pSelectedStartDate and :pSelectedEndDate)) as claimInvoiced, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect','InvoiceRejectionAccepted')) as contestedinvoiceByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceEscalated')) as pendingInvoiceByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingInvoicePayment')) as approvedInvoiceByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('InvoicePaymentLogged')) as paidInvoiceByRsa, "
                + "0 as paidInvoicePercentage, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as claimTobeInvoiced "
                + "from insurer insurer where insurer.id = :pInsId";
               */
                
                /*
                String query =  "select insurer_id"
                + "sum(claimsBFwd) as claimsBFwd, "
                + "sum(claimsNotification) as claimsNotification, "
                + "sum(claimsOutOfScope) as claimsOutOfScope, "
                + "sum(nonThisInsurerClaims) as nonThisInsurerClaims, "
                + "sum(claimsPaid) as claimsPaid, "
                + "sum(claimsNotificationContestedByInsurer) as claimsNotificationContestedByInsurer, "
                + "sum(claimsPendingByInsurer) as claimsPendingByInsurer, "
                + "sum(claimsNotificationAcceptedByInsurer) as claimsNotificationAcceptedByInsurer, "
                + "sum(claimsFNOLCreatedByInsurer) as claimsFNOLCreatedByInsurer, "
                + "sum(claimsInvoiced) as claimsInvoiced, SUM(rec_sum.invoiceContestedByInsurer) as invoiceContestedByInsurer, "
                + "sum(invoicePendingByInsurer) as invoicePendingByInsurer, "
                + "sum(invoiceApprovedByInsurer) as invoiceApprovedByInsurer, "
                + "sum(invoicePaidByInsurer) as invoicePaidByInsurer, "
                + "sum(claimsToBeInvoiced) as claimsToBeInvoiced from ("
                + "select insurer_chorganisation.insurer_id as insurer_id, insurer_chorganisation.chorganisation_id as chorganisation_id, "
                // String query =  "select insurer_chorganisation.insurer_id as insurer_id, "
                + "(100) as claimsBFwd, "
                //+ "(select count(distinct claim_id) from rpt_claim_audit_trail where chorganisation_id=insurer_chorganisation.chorganisation_id AND insurer_id = insurer_chorganisation.insurer_id and date(update_date) between :pSelectedStartDate and :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted') as claimsNotification, "
                + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_audit_trail) as claimsNotification, "
                + "(3) as claimsOutOfScope, "
                + "(select count(distinct id) from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.invoice_id is NULL) AND a.new_status in ('ClaimClosed')) as nonThisInsurerClaims, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as claimsPaid, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejected', 'ClaimRejectionAccepted')) as claimsNotificationContestedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimPending')) as claimsPendingByInsurer, "
                + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND c.status!='InvoicePaymentLogged' AND a.new_status in ('AwaitingCarHireInfo')) as claimsNotificationAcceptedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimReferredToFNOL')) as claimsFNOLCreatedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice WHERE chorganisation_id=insurer_chorganisation.chorganisation_id AND insurer_id=insurer_chorganisation.insurer_id AND created_date BETWEEN :pSelectedStartDate AND :pSelectedEndDate) as claimsInvoiced, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoiceRejectionAccepted')) as invoiceContestedByInsurer, " // REQUESTED AT 12 FEB 2009
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'InvoiceEscalated')) as invoicePendingByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as invoiceApprovedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as invoicePaidByInsurer, "
                + "(0) as claimsToBeInvoiced "
                + "from insurer_chorganisation insurer_chorganisation) as rec_sum where rec_sum.insurer_id = :pInsId ";
                
                if(!supplierId.equalsIgnoreCase("")){
                    query = query + "and rec_sum.chorganisation_id = :pChorganisationId ";
                    queryParameters.put("pChorganisationId", Integer.parseInt(supplierId));
                }                
                
                query = query + "group by rec_sum.insurer_id";
                
                queryParameters.put("pSelectedStartDate", startOfTheWeek);
                queryParameters.put("pSelectedEndDate", endOfTheWeek);
                queryParameters.put("pInsId", ins.getId());
                */

                String query =  "select insurer_id, "
                + "sum(claimsBFwd) as claimsBFwd, "
                + "sum(claimsNotification) as claimsNotification, "
                + "sum(claimsOutOfScope) as claimsOutOfScope, "
                + "sum(nonThisInsurerClaims) as nonThisInsurerClaims, "
                + "sum(claimsPaid) as claimsPaid, "
                + "sum(claimsNotificationContestedByInsurer) as claimsNotificationContestedByInsurer, "
                + "sum(claimsPendingByInsurer) as claimsPendingByInsurer, "
                + "sum(claimsNotificationAcceptedByInsurer) as claimsNotificationAcceptedByInsurer, "
                + "sum(claimsFNOLCreatedByInsurer) as claimsFNOLCreatedByInsurer, "
                + "sum(claimsInvoiced) as claimsInvoiced, "
                + "sum(invoiceContestedByInsurer) as invoiceContestedByInsurer, "
                + "sum(invoicePendingByInsurer) as invoicePendingByInsurer, "
                + "sum(invoiceApprovedByInsurer) as invoiceApprovedByInsurer, "
                + "sum(invoicePaidByInsurer) as invoicePaidByInsurer, "
                + "sum(claimsToBeInvoiced) as claimsToBeInvoiced from ("
                + "select insurer_chorganisation.insurer_id as insurer_id, insurer_chorganisation.chorganisation_id as chorganisation_id, "
                + "(110) as claimsBFwd, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where chorganisation_id=insurer_chorganisation.chorganisation_id AND insurer_id = insurer_chorganisation.insurer_id and date(update_date) between date('2008/12/01') and date('2009/03/01') and new_status='ClaimUnacknowledgedUnrouted') as claimsNotification, "
                + "(0) as claimsOutOfScope, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND (a.invoice_id is NULL) AND a.new_status in ('ClaimClosed')) as nonThisInsurerClaims, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('InvoicePaymentLogged')) as claimsPaid, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('ClaimRejected', 'ClaimRejectionAccepted')) as claimsNotificationContestedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimPending')) as claimsPendingByInsurer, "
                + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN date('2008/12/01') and date('2009/03/01') group by claim_id) b, claim c WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.id AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND c.status!='InvoicePaymentLogged' AND a.new_status in ('AwaitingCarHireInfo')) as claimsNotificationAcceptedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('ClaimReferredToFNOL')) as claimsFNOLCreatedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice WHERE chorganisation_id=insurer_chorganisation.chorganisation_id AND insurer_id=insurer_chorganisation.insurer_id AND created_date BETWEEN date('2008/12/01') AND date('2009/03/01')) as claimsInvoiced, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('InvoiceRejectionAccepted')) as invoiceContestedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'InvoiceEscalated')) as invoicePendingByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('AwaitingInvoicePayment')) as invoiceApprovedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between date('2008/12/01') and date('2009/03/01') group by claim_id) b WHERE a.chorganisation_id=insurer_chorganisation.chorganisation_id AND a.insurer_id=insurer_chorganisation.insurer_id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN date('2008/12/01') and date('2009/03/01') AND a.new_status in ('InvoicePaymentLogged')) as invoicePaidByInsurer, "
                + "(0) as claimsToBeInvoiced "
                + "from insurer_chorganisation insurer_chorganisation "
                + ") as rec_sum where rec_sum.insurer_id=3 "
                + "group by rec_sum.insurer_id ";

                List result = dataService.externalQuery(query, queryParameters);
                
                for (Object o : result) {
                    Map data = (Map) o;
                    data.put("weekCycleDate", DateHelper.LocalDateFormat.format(startOfTheWeek));
                    WeekSummary weekSummary = WeekSummary.getObject(data);
                    weekSummaries.add(weekSummary);
                }
                
                c.add(Calendar.DATE, 1);
                currentMonday = c.getTime();

            }while(currentMonday.before(dateLastSunday));

            WeekSummaryReportObject reportObject = new WeekSummaryReportObject();

            reportObject.setWeekCycleFrom(dateFirstMonday);
            reportObject.setWeekCycleTo(dateLastSunday);
            reportObject.setCreatedDate(new Date());

            reportParameters.put("weekSummaries", weekSummaries);
            reportParameters.put("reportObj", reportObject);            
            reportParameters.put("insurerObj", ins);
            reportParameters.put("chorganisationName", strChorganisationName);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reportParameters;
    }

    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
    
    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }
    
}
