package idas.chox.service.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Insurer;
import idas.chox.data.services.BaseDataService;

public class InsurerDashboardBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerDashboardBuilder.class);
    private static final Logger logger = LoggerFactory.getLogger(InsurerDashboardBuilder.class);
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

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        // Number of Claim Notifications Submitted
        sb.append("sum(num_claims_submitted_w) as  n_ClaimNotificationsSubmitted, ");
        sb.append("sum(num_claims_accepted_w) as  n_ClaimNotificationsAccepted, ");
        sb.append("sum(num_claimrejections_accepted_w) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("sum(num_claims_closed_w) as n_ClaimNotificationsClosed, ");
        sb.append("sum(num_invoices_submitted_w) as n_InvoicesSubmitted, ");
        sb.append("sum(val_invoices_submitted_w) as v_InvoicesSubmitted, ");
        sb.append("sum(num_invoices_accepted_w) as n_InvoicesAccepted, ");
        sb.append("sum(val_invoices_accepted_w) as v_InvoicesAccepted, ");
        sb.append("sum(num_invoices_rejected_w) as n_InvoicesRejected, ");
        sb.append("sum(val_invoices_rejected_w) as v_InvoicesRejected, ");
        sb.append("sum(num_invoices_closed_w) as n_InvoicesClosed, ");
        sb.append("sum(val_invoices_closed_w) as v_InvoicesClosed, ");
        sb.append("sum(num_invoices_logged_w) as n_InvoicesPaymentLogged, ");
        sb.append("sum(val_invoices_logged_w) as v_InvoicesPaymentLogged, ");
        sb.append("sum(num_invoices_received_w) as n_InvoicesPaymentReceived, ");
        sb.append("sum(val_invoices_received_w) as v_InvoicesPaymentReceived, ");
        sb.append("sum(val_penalty_charges_w) as v_PenaltyChargesApplied, ");
        sb.append("sum(val_penalty_charges_paid_w) as v_PenaltyChargesPaid, ");
        sb.append("avg(avg_inv_payment_time_w) as Avg_InvPaymentTime, ");
        sb.append("sum(num_insurer_claims_submitted_w) as n_insurer_ClaimsSubmitted, ");
        sb.append("sum(num_manual_invoices_submitted_w) as n_manual_InvoicesSubmitted, ");
        sb.append("sum(val_manual_invoices_submitted_w) as v_manual_InvoicesSubmitted, ");
        sb.append("sum(num_manual_invoices_paid_w) as n_manual_InvoicesPaid, ");
        sb.append("sum(val_manual_invoices_paid_w) as v_manual_InvoicesPaid, ");
        sb.append("sum(num_manual_invoices_closed_w) as n_manual_InvoicesClosed, ");
        sb.append("sum(val_manual_invoices_closed_w) as v_manual_InvoicesClosed, ");
        sb.append("sum(num_invoices_awaiting_litigation_outcome_w) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("sum(val_invoices_awaiting_litigation_outcome_w) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = :pInsId and complete=true");
        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getMonthToDate() {

        Map queryParameters = getQueryParameters();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        // Number of Claim Notifications Submitted
        sb.append("sum(num_claims_submitted_m) as  n_ClaimNotificationsSubmitted, ");
        sb.append("sum(num_claims_accepted_m) as  n_ClaimNotificationsAccepted, ");
        sb.append("sum(num_claimrejections_accepted_m) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("sum(num_claims_closed_m) as n_ClaimNotificationsClosed, ");
        sb.append("sum(num_invoices_submitted_m) as n_InvoicesSubmitted, ");
        sb.append("sum(val_invoices_submitted_m) as v_InvoicesSubmitted, ");
        sb.append("sum(num_invoices_accepted_m) as n_InvoicesAccepted, ");
        sb.append("sum(val_invoices_accepted_m) as v_InvoicesAccepted, ");
        sb.append("sum(num_invoices_rejected_m) as n_InvoicesRejected, ");
        sb.append("sum(val_invoices_rejected_m) as v_InvoicesRejected, ");
        sb.append("sum(num_invoices_closed_m) as n_InvoicesClosed, ");
        sb.append("sum(val_invoices_closed_m) as v_InvoicesClosed, ");
        sb.append("sum(num_invoices_logged_m) as n_InvoicesPaymentLogged, ");
        sb.append("sum(val_invoices_logged_m) as v_InvoicesPaymentLogged, ");
        sb.append("sum(num_invoices_received_m) as n_InvoicesPaymentReceived, ");
        sb.append("sum(val_invoices_received_m) as v_InvoicesPaymentReceived, ");
        sb.append("sum(val_penalty_charges_m) as v_PenaltyChargesApplied, ");
        sb.append("sum(val_penalty_charges_paid_m) as v_PenaltyChargesPaid, ");
        sb.append("avg(avg_inv_payment_time_m) as Avg_InvPaymentTime, ");
        sb.append("sum(num_insurer_claims_submitted_m) as n_insurer_ClaimsSubmitted, ");
        sb.append("sum(num_manual_invoices_submitted_m) as n_manual_InvoicesSubmitted, ");
        sb.append("sum(val_manual_invoices_submitted_m) as v_manual_InvoicesSubmitted, ");
        sb.append("sum(num_manual_invoices_paid_m) as n_manual_InvoicesPaid, ");
        sb.append("sum(val_manual_invoices_paid_m) as v_manual_InvoicesPaid, ");
        sb.append("sum(num_manual_invoices_closed_m) as n_manual_InvoicesClosed, ");
        sb.append("sum(val_manual_invoices_closed_m) as v_manual_InvoicesClosed, ");
        sb.append("sum(num_invoices_awaiting_litigation_outcome_m) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("sum(val_invoices_awaiting_litigation_outcome_m) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = :pInsId and complete=true");
        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        //Number of Claim Notifications Submitted
        sb.append("sum(num_claims_submitted_c) as  n_ClaimNotificationsSubmitted, ");
        sb.append("sum(num_claims_accepted_c) as  n_ClaimNotificationsAccepted, ");
        sb.append("sum(num_claimrejections_accepted_c) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("sum(num_claims_pending_c) as n_ClaimsAwaitingToBeProcessed, ");
        sb.append("sum(num_claims_closed_c) as n_ClaimNotificationsClosed, ");
        sb.append("sum(num_invoices_submitted_c) as n_InvoicesSubmitted, ");
        sb.append("sum(val_invoices_submitted_c) as v_InvoicesSubmitted, ");
        sb.append("sum(num_invoices_accepted_c) as n_InvoicesAccepted, ");
        sb.append("sum(val_invoices_accepted_c) as v_InvoicesAccepted, ");
        sb.append("sum(num_invoices_rejected_c) as n_InvoicesRejected, ");
        sb.append("sum(val_invoices_rejected_c) as v_InvoicesRejected, ");
        sb.append("sum(num_invoices_pending_c) as n_InvoicesPending, ");
        sb.append("sum(val_invoices_pending_c) as v_InvoicesPending, ");
        sb.append("sum(num_invoices_awaiting_liability_c) as n_InvoicesAwaitingLiabilityResolution, ");
        sb.append("sum(val_invoices_awaiting_liability_c) as v_InvoicesAwaitingLiabilityResolution, ");
        sb.append("sum(num_invoices_closed_c) as n_InvoicesClosed, ");
        sb.append("sum(val_invoices_closed_c) as v_InvoicesClosed, ");
        sb.append("sum(num_invoices_logged_c) as n_InvoicesPaymentLogged, ");
        sb.append("sum(val_invoices_logged_c) as v_InvoicesPaymentLogged, ");
        sb.append("sum(num_invoices_received_c) as n_InvoicesPaymentReceived, ");
        sb.append("sum(val_invoices_received_c) as v_InvoicesPaymentReceived, ");
        sb.append("sum(val_penalty_charges_c) as v_PenaltyChargesApplied, ");
        sb.append("sum(val_penalty_charges_paid_c) as v_PenaltyChargesPaid, ");
        sb.append("avg(avg_inv_payment_time_c) as Avg_InvPaymentTime, ");
        sb.append("sum(num_insurer_claims_submitted_c) as n_insurer_ClaimsSubmitted, ");
        sb.append("sum(num_manual_invoices_submitted_c) as n_manual_InvoicesSubmitted, ");
        sb.append("sum(val_manual_invoices_submitted_c) as v_manual_InvoicesSubmitted, ");
        sb.append("sum(num_manual_invoices_paid_c) as n_manual_InvoicesPaid, ");
        sb.append("sum(val_manual_invoices_paid_c) as v_manual_InvoicesPaid, ");
        sb.append("sum(num_manual_invoices_closed_c) as n_manual_InvoicesClosed, ");
        sb.append("sum(val_manual_invoices_closed_c) as v_manual_InvoicesClosed, ");
        sb.append("sum(num_invoices_awaiting_litigation_outcome_c) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("sum(val_invoices_awaiting_litigation_outcome_c) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = :pInsId and complete=true");
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
        Integer workgroupId = -1;
        Integer claimOwnerId = -1;

        String choOrgIdRaw = ((String[]) this.extParameters.get("supplierId"))[0].toString();
        if (!choOrgIdRaw.isEmpty()) {
            choOrgId = Integer.parseInt(choOrgIdRaw);
        }

        String workgroupIdRaw = ((String[]) this.extParameters.get("workgroupId"))[0].toString();
        if (!workgroupIdRaw.isEmpty()) {
            workgroupId = Integer.parseInt(workgroupIdRaw);
        }

        String claimOwnerIdRaw = ((String[]) this.extParameters.get("claimOwnerId"))[0].toString();
        if (!claimOwnerIdRaw.isEmpty()) {
            claimOwnerId = Integer.parseInt(claimOwnerIdRaw);
        }

        queryParameters.put("pInsId", insurer.getId());
        queryParameters.put("pChorganisationId", choOrgId);
        queryParameters.put("pWorkgroupId", workgroupId);
        queryParameters.put("pClaimOwnerId", claimOwnerId);
        logger.debug("workgroupId :" + workgroupId + "claimOwnerId :" + claimOwnerId + "pInsId :" + insurer.getId() + "choOrgId :" + choOrgId);
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
