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
import chox.web.actions.BaseAction;
import chox.web.report.viewdata.WeekSummary;
import chox.web.report.viewdata.WeekSummaryReportObject;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;


public class InsurerAdminWeeklyOverviewReport extends BaseAction implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;

    public InsurerAdminWeeklyOverviewReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_InsurerAdminWeeklyOverviewReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    private Chorganisation getChorganisation(int orgId){
        
        Chorganisation chorg = new Chorganisation();
                
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation)dataService.getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        } 
        
        return chorg;
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
            Integer iSupplierId = -1;
            
            String strChorganisationName = "All";
            if(!supplierId.equalsIgnoreCase("")){
                iSupplierId = Integer.parseInt(supplierId);
                strChorganisationName = getChorganisation(iSupplierId).getName();
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
            
            Integer iClaimsInvoicedHis = 0;
            Integer iInvoicePaidByInsurerHis = 0;
            Integer iClaimsNotificationAcceptedByInsurerHis = 0;
            
            do {
                
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonday);
                Date startOfTheWeek = c.getTime();
                c.add(Calendar.DATE, 6);
                Date endOfTheWeek = c.getTime();

                HashMap queryParameters = new HashMap();
                
                String query =  "select insurer.id, "

                // NEW CLAIMS SUMMARY SECTION
                + "((select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) < :pSelectedStartDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) < :pSelectedStartDate AND a.new_status not in ('ClaimClosed','InvoicePaymentLogged')) "
                + "- (select count(distinct id) from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) < :pSelectedStartDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) < :pSelectedStartDate AND a.new_status in ('ClaimRejectionAccepted') AND a.claim_reason_of_rejection in (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%'))) as claimsBFwd, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and date(update_date) between :pSelectedStartDate and :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted') as claimsNotification, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND a.claim_reason_of_rejection in (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%')) as claimsOutOfScope, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (a.invoice_id is NULL) AND a.new_status in ('ClaimClosed')) as nonThisInsurerClaims, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as claimsPaid, "

                // CLAIM ACCEPTED OR REJECTION SUMMARY
                + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted') AND (b.reason_of_rejection_id != (select id from reason_of_rejection where type='Claim' and name like '%Out of Scope%') OR b.reason_of_rejection_id is null)) as claimsNotificationContestedByInsurer, "
                // + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimRejectionContested', 'ClaimRejected', 'ClaimPending')) as claimsPendingByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimRejectionContested', 'ClaimRejected', 'ClaimPending')) as claimsPendingByInsurer, "
                + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo')) as claimsNotificationAcceptedByInsurer, "
                + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimReferredToFNOL')) as claimsFNOLCreatedByInsurer, "

                // INVOICING SUMMARY
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice WHERE (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND insurer_id=insurer.id AND date(created_date) BETWEEN :pSelectedStartDate AND :pSelectedEndDate) as claimsInvoiced, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToCHO', 'InvoiceDataCalculationIncorrect', 'InvoiceRejectionAccepted')) as invoiceContestedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'InvoiceEscalated', 'InvoiceReferredToClaimsHandler')) as invoicePendingByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as invoiceApprovedByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as invoicePaidByInsurer, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) <= :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) <= :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as claimsToBeInvoiced "
                + "from insurer insurer where insurer.id = :pInsId ";

                queryParameters.put("pSelectedStartDate", startOfTheWeek);
                queryParameters.put("pSelectedEndDate", endOfTheWeek);
                queryParameters.put("pInsId", ins.getId());
                queryParameters.put("pChorganisationId", iSupplierId);

                List result = dataService.externalQuery(query, queryParameters);
                
                for (Object o : result) {
                    
                    Map data = (Map) o;
                    data.put("weekCycleDate", DateHelper.LocalDateFormat.format(startOfTheWeek));
                    WeekSummary weekSummary = WeekSummary.getObject(data);
                    
                    iClaimsNotificationAcceptedByInsurerHis = iClaimsNotificationAcceptedByInsurerHis + weekSummary.getClaimsNotificationAcceptedByInsurer();
                    
                    // InvoicePaidAsPercentageOfInvoicing
                    iClaimsInvoicedHis = iClaimsInvoicedHis + weekSummary.getClaimsInvoiced();
                    iInvoicePaidByInsurerHis = iInvoicePaidByInsurerHis + weekSummary.getClaimsPaid();
                    weekSummary.setInvoicePaidAsPercentageOfInvoicing(iClaimsInvoicedHis, iInvoicePaidByInsurerHis);
                    
                    //weekSummary.setClaimsToBeInvoiced(iClaimsNotificationAcceptedByInsurerHis - weekSummary.getInvoicePaidByInsurer());
                    
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
    
}
