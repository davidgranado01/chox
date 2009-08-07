/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.dashboard;

import chox.Util.DateHelper;
import chox.model.Insurer;
import chox.services.DataService;
import chox.web.dashboard.viewdata.DashBoardViewData;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsurerDashboardBuilder {

    // INPUT DATA
    private DataService dataService;
    private Insurer insurer;
    private Map extParameters;
    
    public InsurerDashboardBuilder(DataService dataService, Insurer insurer, Map extParameters){
        this.setDataService(dataService);
        this.setExtParameters(extParameters);
        this.setInsurer(insurer);
    }    

    public DashBoardViewData getWeekToDate() {
        Map queryParameters = getQueryParameters();
        queryParameters.put("pQueryType", 0);
        // Date now = new Date();
        // queryParameters.put("pSelectedStartDate", DateHelper.getFirstDateOfTheWeek(now));
        // queryParameters.put("pSelectedEndDate", now);

        return build(queryParameters);
    }
    
    public DashBoardViewData getMonthToDate() {
        
        Map queryParameters = getQueryParameters();
        queryParameters.put("pQueryType", 1);
        // Date now = new Date();
        // queryParameters.put("pSelectedStartDate", DateHelper.getFirstDateOfTheMonth(now));
        // queryParameters.put("pSelectedEndDate", now);

        return build(queryParameters);
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();
        // queryParameters.put("pSelectedStartDate", DateHelper.getMinDate());
        // queryParameters.put("pSelectedEndDate", DateHelper.getMaxDate());
        queryParameters.put("pQueryType", 2);
        return build(queryParameters);
    }
    
    private DashBoardViewData build(Map queryParameters) {
        
        DashBoardViewData viewData = new DashBoardViewData();
        String query = getQuery();
        List result = dataService.externalQuery(query, queryParameters);
        if (!result.isEmpty()) {
            viewData = DashBoardViewData.getObject((Map) result.get(0));
        }
        return viewData;
    }

    private Map getQueryParameters() {
        Map queryParameters = new HashMap();
        
        queryParameters.put("pInsId", insurer.getId());

        Integer choOrgId = -1;
        
        String choOrgIdRaw = ((String[])this.extParameters.get("supplierId"))[0].toString();
        if(!choOrgIdRaw.isEmpty()){
            choOrgId = Integer.parseInt(choOrgIdRaw);
        }

        queryParameters.put("pChorganisationId", choOrgId);

        return queryParameters;
    } 

    private String getQuery()
    {
        /*
        String query = "select insurer.id, insurer.name,"
        + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from claim where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and created_date between :pSelectedStartDate and :pSelectedEndDate) as noOfClaimNotificationsSubmitted,"
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo')) as noOfClaimNotificationsAcceptedAccumulative,"
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted')) as noOfClaimNotificationsRejectedAccumulative,"
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.claim_id=c.id AND (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND (c.invoice_id is null or c.invoice_id<=0) AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo', 'AwaitingInvoiceData')) as noOfClaimNotificationsAccepted, "
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.claim_id=c.id AND (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND (c.invoice_id is null or c.invoice_id<=0) AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted')) as noOfClaimNotificationsRejected, "
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimPending','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimRejected','ClaimRejectionContested','ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnrouted', 'ClaimUpdatedByEngineer')) as noOfClaimNotificationsPending, "
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.claim_id=c.id AND (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND (c.invoice_id is null or c.invoice_id<=0) AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimClosed')) as noOfClaimNotificationsClosed, "
        + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and created_date between :pSelectedStartDate and :pSelectedEndDate) as noOfInvoicesSubmitted,"
        + "(select case when sum(total_to_pay-panalty_charge) is null then 0.00 else sum(total_to_pay-panalty_charge) end as no_sum from rpt_claim_invoice where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and created_date between :pSelectedStartDate and :pSelectedEndDate) as valueOfInvoicesSubmitted,"       
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as noOfInvoicesAccepted,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as valueOfInvoicesAccepted,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoiceRejectionAccepted')) as noOfInvoicesRejected,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoiceRejectionAccepted')) as valueOfInvoicesRejected,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler')) as noOfInvoicesPending,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler')) as valueOfInvoicesPending,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimClosed')) as noOfInvoicesClosed,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimClosed')) as valueOfInvoicesClosed,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as noOfInvoicesPaymentLogged,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as valueOfInvoicesPaymentLogged,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('PaymentReceived')) as noOfInvoicesPaymentReceived,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE (a.chorganisation_id = :pChorganisationId or :pChorganisationId < 0) AND a.insurer_id=insurer.id AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('PaymentReceived')) as valueOfInvoicesPaymentReceived,"        
        + "(select case when sum(panalty_charge) is null then 0.00 else sum(panalty_charge) end as no_sum from rpt_claim_invoice where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and date_trunc('day', penalty_charge_applied_date) between :pSelectedStartDate and :pSelectedEndDate) as totalValueOfPenaltyChargesApplied "
        + "from insurer insurer where insurer.id = :pInsId";
        */
        String query = "select * from SqlGetInsurerDashboardByType(:pQueryType, :pInsId, :pChorganisationId)";
        return query;
    }

    private void setDataService(DataService dataService) { this.dataService = dataService; }
    private void setInsurer(Insurer insurer) { this.insurer = insurer; }
    private void setExtParameters(Map extParameters) { this.extParameters = extParameters; }
    
}
