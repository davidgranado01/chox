/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.dashboard;

import idas.chox.core.model.Chorganisation;
import idas.chox.data.services.DataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * :author Emmanuel
 */
public class ChoDashboardBuilder {

    private DataService dataService;
    private Chorganisation chorganisation;
    private Map extParameters;

    public ChoDashboardBuilder(DataService dataService, Chorganisation chorganisation, Map extParameters) {
        this.setDataService(dataService);
        this.setExtParameters(extParameters);
        this.setChorganisation(chorganisation);
    }

    public DashBoardViewData getMonthToDate() {
        Map queryParameters = getQueryParameters();
        queryParameters.put("pQueryType", 1);

        /*
        Date now = new Date();
        queryParameters.put("pSelectedStartDate", DateHelper.getFirstDateOfTheMonth(now));
        queryParameters.put("pSelectedEndDate", now);
         */

        return build(queryParameters);
    }

    public DashBoardViewData getWeekToDate() {
        Map queryParameters = getQueryParameters();
        queryParameters.put("pQueryType", 0);
        /*
        Date now = new Date();
        queryParameters.put("pSelectedStartDate", DateHelper.getFirstDateOfTheWeek(now));
        queryParameters.put("pSelectedEndDate", now);
         */
        return build(queryParameters);
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();
        queryParameters.put("pQueryType", 2);
        /*
        queryParameters.put("pSelectedStartDate", DateHelper.getMinDate());
        queryParameters.put("pSelectedEndDate", DateHelper.getMaxDate());
         */
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

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    private Map getQueryParameters() {
        Map queryParameters = new HashMap();

        queryParameters.put("pChorganisationId", chorganisation.getId());

        Integer insurerId = -1;

        String insurerIdRaw = ((String[]) this.extParameters.get("insurerId"))[0].toString();
        if (!insurerIdRaw.isEmpty()) {
            insurerId = Integer.parseInt(insurerIdRaw);
        }

        queryParameters.put("pInsId", insurerId);

        return queryParameters;
    }

    private String getQuery() {
        /*
        String query = "select chorganisation.id, chorganisation.name,"
        + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from claim where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate) as noOfClaimNotificationsSubmitted,"
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE a.chorganisation_id=chorganisation.id and (a.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo')) as noOfClaimNotificationsAcceptedAccumulative,"
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, claim b WHERE a.chorganisation_id=chorganisation.id and (a.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted')) as noOfClaimNotificationsRejectedAccumulative,"      
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.claim_id=c.id AND a.chorganisation_id=chorganisation.id and (a.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingCarHireInfo', 'AwaitingInvoiceData')) as noOfClaimNotificationsAccepted, "
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.claim_id=c.id AND a.chorganisation_id=chorganisation.id and (a.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimRejectionAccepted')) as noOfClaimNotificationsRejected, "
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.claim_id=c.id AND a.chorganisation_id=chorganisation.id and (a.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimPending','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimRejected','ClaimRejectionContested','ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnrouted', 'ClaimUpdatedByEngineer')) as noOfClaimNotificationsPending, "
        + "(select case when count(distinct a.claim_id) is null then 0 else count(distinct a.claim_id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, claim c WHERE a.claim_id=c.id AND a.chorganisation_id=chorganisation.id and (a.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND (c.invoice_id is null or c.invoice_id<=0) AND a.new_status in ('ClaimClosed')) as noOfClaimNotificationsClosed, "
        + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate) as noOfInvoicesSubmitted,"
        + "(select case when sum(total_to_pay-panalty_charge) is null then 0.00 else sum(total_to_pay-panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate) as valueOfInvoicesSubmitted,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as noOfInvoicesAccepted,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('AwaitingInvoicePayment')) as valueOfInvoicesAccepted,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoiceRejectionAccepted')) as noOfInvoicesRejected,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoiceRejectionAccepted')) as valueOfInvoicesRejected,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler')) as noOfInvoicesPending,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler')) as valueOfInvoicesPending,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimClosed')) as noOfInvoicesClosed,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('ClaimClosed')) as valueOfInvoicesClosed,"
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as noOfInvoicesPaymentLogged,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('InvoicePaymentLogged')) as valueOfInvoicesPaymentLogged," 
        + "(select case when count(distinct a.id) is null then 0 else count(distinct a.id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('PaymentReceived')) as noOfInvoicesPaymentReceived,"
        + "(select case when sum(c.total_to_pay-c.panalty_charge) is null then 0.00 else sum(c.total_to_pay-c.panalty_charge) end as no_sum from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where date(update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate group by claim_id) b, rpt_claim_invoice c WHERE c.chorganisation_id=chorganisation.id and (c.insurer_id = :pInsId or :pInsId < 0) AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.claim_id=c.claim_id AND date(a.update_date) BETWEEN :pSelectedStartDate and :pSelectedEndDate AND a.new_status in ('PaymentReceived')) as valueOfInvoicesPaymentReceived," 
        + "(select case when sum(panalty_charge) is null then 0.00 else sum(panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and date_trunc('day', penalty_charge_applied_date) between :pSelectedStartDate and :pSelectedEndDate) as totalValueOfPenaltyChargesApplied "
        + "from chorganisation chorganisation where chorganisation.id = :pChorganisationId";
         */
        String query = "select * from SqlGetCreditHireDashboardByType(:pQueryType, :pInsId, :pChorganisationId)";
        return query;
    }

    public void setExtParameters(Map extParameters) {
        this.extParameters = extParameters;
    }
}
