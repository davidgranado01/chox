/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.dashboard;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.services.DataService;
import chox.web.dashboard.viewdata.DashBoardViewData;
import java.util.Date;
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

    public DashBoardViewData getMonthToDate() {
        Map queryParameters = getQueryParameters();

        Date now = new Date();
        queryParameters.put("pSelectedStartDate", DateHelper.getFirstDateOfTheMonth(now));
        queryParameters.put("pSelectedEndDate", now);

        return build(queryParameters);
    }

    public DashBoardViewData getWeekToDate() {
        Map queryParameters = getQueryParameters();

        Date now = new Date();
        queryParameters.put("pSelectedStartDate", DateHelper.getFirstDateOfTheWeek(now));
        queryParameters.put("pSelectedEndDate", now);

        return build(queryParameters);
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();

        queryParameters.put("pSelectedStartDate", DateHelper.getMinDate());
        queryParameters.put("pSelectedEndDate", DateHelper.getMaxDate());

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
        
        String insurerIdRaw = ((String[])this.extParameters.get("insurerId"))[0].toString();
        if(!insurerIdRaw.isEmpty()){
            insurerId = Integer.parseInt(insurerIdRaw);
        }

        queryParameters.put("pInsId", insurerId);

        return queryParameters;
    } 

    private String getQuery()
    {
        String query = "select chorganisation.id, chorganisation.name,"
        +"(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and update_date between :pSelectedStartDate and :pSelectedEndDate) as noOfClaimNotificationsSubmitted,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from claim where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as noOfClaimNotificationsAccepted,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from claim where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('ClaimRejectionAccepted')) as noOfClaimNotificationsRejected,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from claim where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('ClaimUnacknowledgedRouted',' ClaimRejected',' ClaimRejectionContested',' ClaimReferredToFNOL',' ClaimReferredToEngineer')) as noOfClaimNotificationsPending,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate) as noOfInvoicesSubmitted,"
        +"(select case when sum(total_to_pay-panalty_charge) is null then 0.00 else sum(total_to_pay-panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate) as valueOfInvoicesSubmitted,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('InvoicePaymentLogged')) as noOfInvoicesAccepted,"
        +"(select case when sum(total_to_pay-panalty_charge) is null then 0.00 else sum(total_to_pay-panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('InvoicePaymentLogged')) as valueOfInvoicesAccepted,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('InvoiceRejectionAccepted')) as noOfInvoicesRejected,"
        +"(select case when sum(total_to_pay-panalty_charge) is null then 0.00 else sum(total_to_pay-panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('InvoiceRejectionAccepted')) as valueOfInvoicesRejected,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment')) as noOfInvoicesPending,"
        +"(select case when sum(total_to_pay-panalty_charge) is null then 0.00 else sum(total_to_pay-panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','AwaitingInvoicePayment')) as valueOfInvoicesPending,"
        +"(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('ClaimClosed')) as noOfInvoicesClosed,"
        +"(select case when sum(total_to_pay-panalty_charge) is null then 0.00 else sum(total_to_pay-panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate and status in ('ClaimClosed')) as valueOfInvoicesClosed,"
        +"(select case when sum(panalty_charge) is null then 0.00 else sum(panalty_charge) end as no_sum from rpt_claim_invoice where chorganisation_id=chorganisation.id and (insurer_id = :pInsId or :pInsId < 0) and created_date between :pSelectedStartDate and :pSelectedEndDate) as totalValueOfPenaltyChargesApplied"
        +" from chorganisation chorganisation where chorganisation.id = :pChorganisationId";
        
        return query;
    }

    public void setExtParameters(Map extParameters) {
        this.extParameters = extParameters;
    }
}
