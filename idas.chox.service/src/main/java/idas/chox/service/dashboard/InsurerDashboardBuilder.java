/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.dashboard;

import idas.chox.core.model.Insurer;
import idas.chox.data.services.BaseDataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsurerDashboardBuilder {

    // INPUT DATA
    private BaseDataService baseDataService;
    private Insurer insurer;
    private Map extParameters;

    public InsurerDashboardBuilder(BaseDataService baseDataService, Insurer insurer, Map extParameters) {
        this.setDataService(baseDataService);
        this.setExtParameters(extParameters);
        this.setInsurer(insurer);
    }

    public DashBoardViewData getWeekToDate() {
        Map queryParameters = getQueryParameters();

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(select sum(w_total_claim_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status='AwaitingCarHireInfo') as n_ClaimNotificationsAcceptedAcc, ");
        sb.append("(select sum(w_total_claim_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status='ClaimRejectionAccepted') as n_ClaimNotificationsRejectedAcc, ");
        sb.append("(select sum(w_claim_created_count) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(w_count - w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(w_count - w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimRejectionAccepted')) as n_ClaimNotificationsRejected, ");
        sb.append("(select sum(w_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimPending','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimRejected','ClaimRejectionContested','ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnrouted','ClaimUpdatedByEngineer', 'ClaimUnacknowledgedUnassigned')) as n_ClaimNotificationsPending, ");
        sb.append("(select sum(w_count - w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(w_inv_created_count) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as n_InvoicesSubmitted, ");
        sb.append("(select sum(w_inv_created_amt) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as v_InvoicesSubmitted, ");
        sb.append("(select sum(w_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingInvoicePayment')) as n_InvoicesAccepted, ");
        sb.append("(select sum(w_total_to_pay - w_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingInvoicePayment')) as v_InvoicesAccepted, ");
        sb.append("(select sum(w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoiceRejectionAccepted')) as n_InvoicesRejected, ");
        sb.append("(select sum(w_total_to_pay - w_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoiceRejectionAccepted')) as v_InvoicesRejected, ");
        sb.append("(select sum(w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as n_InvoicesPending, ");
        sb.append("(select sum(w_total_to_pay - w_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as v_InvoicesPending, ");
        sb.append("(select sum(w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as n_InvoicesClosed, ");
        sb.append("(select sum(w_total_to_pay - w_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as v_InvoicesClosed, ");
        sb.append("(select sum(w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoicePaymentLogged')) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(w_total_to_pay - w_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoicePaymentLogged')) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(w_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('PaymentReceived')) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(w_total_to_pay - w_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('PaymentReceived')) as v_InvoicesPaymentReceived, ");
        sb.append("(select sum(w_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as v_PenaltyChargesApplied ");
        sb.append("from insurer insurer where insurer.id = :pInsId ");
        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getMonthToDate() {

        Map queryParameters = getQueryParameters();

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(select sum(m_total_claim_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status='AwaitingCarHireInfo') as n_ClaimNotificationsAcceptedAcc, ");
        sb.append("(select sum(m_total_claim_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status='ClaimRejectionAccepted') as n_ClaimNotificationsRejectedAcc, ");
        sb.append("(select sum(m_claim_created_count) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(m_count - m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(m_count - m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimRejectionAccepted')) as n_ClaimNotificationsRejected, ");
        sb.append("(select sum(m_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimPending','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimRejected','ClaimRejectionContested','ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnrouted','ClaimUpdatedByEngineer', 'ClaimUnacknowledgedUnassigned')) as n_ClaimNotificationsPending, ");
        sb.append("(select sum(m_count - m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(m_inv_created_count) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as n_InvoicesSubmitted, ");
        sb.append("(select sum(m_inv_created_amt) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as v_InvoicesSubmitted,	 ");
        sb.append("(select sum(m_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingInvoicePayment')) as n_InvoicesAccepted, ");
        sb.append("(select sum(m_total_to_pay - m_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingInvoicePayment')) as v_InvoicesAccepted, ");
        sb.append("(select sum(m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoiceRejectionAccepted')) as n_InvoicesRejected, ");
        sb.append("(select sum(m_total_to_pay - m_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoiceRejectionAccepted')) as v_InvoicesRejected, ");
        sb.append("(select sum(m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as n_InvoicesPending, ");
        sb.append("(select sum(m_total_to_pay - m_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as v_InvoicesPending, ");
        sb.append("(select sum(m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as n_InvoicesClosed, ");
        sb.append("(select sum(m_total_to_pay - m_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as v_InvoicesClosed, ");
        sb.append("(select sum(m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoicePaymentLogged')) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(m_total_to_pay - m_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoicePaymentLogged')) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(m_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('PaymentReceived')) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(m_total_to_pay - m_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('PaymentReceived')) as v_InvoicesPaymentReceived, ");
        sb.append("(select sum(m_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as v_PenaltyChargesApplied ");
        sb.append("from insurer insurer where insurer.id = :pInsId ");

        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(select sum(a_total_claim_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status='AwaitingCarHireInfo') as n_ClaimNotificationsAcceptedAcc, ");
        sb.append("(select sum(a_total_claim_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status='ClaimRejectionAccepted') as n_ClaimNotificationsRejectedAcc, ");
        sb.append("(select sum(a_claim_created_count) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(a_count - a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(a_count - a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimRejectionAccepted')) as n_ClaimNotificationsRejected, ");
        sb.append("(select sum(a_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimPending','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimRejected','ClaimRejectionContested','ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnrouted','ClaimUpdatedByEngineer', 'ClaimUnacknowledgedUnassigned')) as n_ClaimNotificationsPending, ");
        sb.append("(select sum(a_count - a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(a_inv_created_count) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as n_InvoicesSubmitted, ");
        sb.append("(select sum(a_inv_created_amt) as no_count from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as v_InvoicesSubmitted, ");
        sb.append("(select sum(a_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingInvoicePayment')) as n_InvoicesAccepted, ");
        sb.append("(select sum(a_total_to_pay - a_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('AwaitingInvoicePayment')) as v_InvoicesAccepted, ");
        sb.append("(select sum(a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoiceRejectionAccepted')) as n_InvoicesRejected, ");
        sb.append("(select sum(a_total_to_pay - a_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoiceRejectionAccepted')) as v_InvoicesRejected, ");
        sb.append("(select sum(a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as n_InvoicesPending, ");
        sb.append("(select sum(a_total_to_pay - a_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated','ClaimPending', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) as v_InvoicesPending, ");
        sb.append("(select sum(a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as n_InvoicesClosed, ");
        sb.append("(select sum(a_total_to_pay - a_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('ClaimClosed')) as v_InvoicesClosed, ");
        sb.append("(select sum(a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoicePaymentLogged')) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(a_total_to_pay - a_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('InvoicePaymentLogged')) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(a_inv_count) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('PaymentReceived')) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(a_total_to_pay - a_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id and status in ('PaymentReceived')) as v_InvoicesPaymentReceived, ");
        sb.append("(select sum(a_penalty_charge) from vw_claim_summary where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and insurer_id = insurer.id) as v_PenaltyChargesApplied ");
        sb.append("from insurer insurer where insurer.id = :pInsId ");

        return build(queryParameters, sb.toString());
    }

    private DashBoardViewData build(Map queryParameters, String query) {

        DashBoardViewData viewData = new DashBoardViewData();
        List result = baseDataService.externalQuery(query, queryParameters);
        if (!result.isEmpty()) {
            viewData = DashBoardViewData.getObject((Map) result.get(0));
        }
        return viewData;
    }

    private Map getQueryParameters() {
        Map queryParameters = new HashMap();

        Integer choOrgId = -1;

        String choOrgIdRaw = ((String[]) this.extParameters.get("supplierId"))[0].toString();
        if (!choOrgIdRaw.isEmpty()) {
            choOrgId = Integer.parseInt(choOrgIdRaw);
        }

        queryParameters.put("pInsId", insurer.getId());
        queryParameters.put("pChorganisationId", choOrgId);
        
        return queryParameters;
    }

    private void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    private void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    private void setExtParameters(Map extParameters) {
        this.extParameters = extParameters;
    }
}
