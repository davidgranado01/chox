package idas.chox.service.dashboard;

import idas.chox.core.model.Chorganisation;
import idas.chox.data.services.BaseDataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChoDashboardBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerDashboardBuilder.class);
    private static final Logger logger = LoggerFactory.getLogger(InsurerDashboardBuilder.class);

    private BaseDataService baseDataService;
    private Chorganisation chorganisation;
    private Map extParameters;

    public ChoDashboardBuilder(BaseDataService baseDataService, Chorganisation chorganisation, Map extParameters) {
        this.baseDataService = baseDataService;
        this.chorganisation = chorganisation;
        this.extParameters = extParameters;
    }

    public DashBoardViewData getWeekToDate() {
        Map queryParameters = getQueryParameters();
        StringBuilder sb = new StringBuilder();

         sb.append("select ");
        // Number of Claim Notifications Submitted
        sb.append("(select sum(num_claims_submitted_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and  (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(num_claims_accepted_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(num_claimrejections_accepted_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("(select sum(num_claims_closed_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(num_invoices_submitted_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesSubmitted, ");
        sb.append("(select sum(val_invoices_submitted_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesSubmitted, ");
        sb.append("(select sum(num_invoices_accepted_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesAccepted, ");
        sb.append("(select sum(val_invoices_accepted_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesAccepted, ");
        sb.append("(select sum(num_invoices_rejected_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesRejected, ");
        sb.append("(select sum(val_invoices_rejected_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesRejected, ");
        sb.append("(select sum(num_invoices_closed_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesClosed, ");
        sb.append("(select sum(val_invoices_closed_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesClosed, ");
        sb.append("(select sum(num_invoices_logged_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(val_invoices_logged_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(num_invoices_received_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_invoices_received_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesPaymentReceived, ");
        sb.append("(select avg(avg_inv_payment_time_w) as avg_inv_time from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as Avg_InvPaymentTime, ");
        sb.append("(select sum(num_invoices_awaiting_litigation_outcome_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("(select sum(val_invoices_awaiting_litigation_outcome_w) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from chorganisation chorganisation where chorganisation.id=:pChorganisationId ");

        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getMonthToDate() {
        Map queryParameters = getQueryParameters();
        StringBuilder sb = new StringBuilder();

        sb.append("select  ");
        // Number of Claim Notifications Submitted
        sb.append("(select sum(num_claims_submitted_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(num_claims_accepted_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(num_claimrejections_accepted_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("(select sum(num_claims_closed_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(num_invoices_submitted_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesSubmitted, ");
        sb.append("(select sum(val_invoices_submitted_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesSubmitted, ");
        sb.append("(select sum(num_invoices_accepted_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesAccepted, ");
        sb.append("(select sum(val_invoices_accepted_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesAccepted, ");
        sb.append("(select sum(num_invoices_rejected_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesRejected, ");
        sb.append("(select sum(val_invoices_rejected_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesRejected, ");
        sb.append("(select sum(num_invoices_closed_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesClosed, ");
        sb.append("(select sum(val_invoices_closed_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesClosed, ");
        sb.append("(select sum(num_invoices_logged_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(val_invoices_logged_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(num_invoices_received_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_invoices_received_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesPaymentReceived, ");
        sb.append("(select avg(avg_inv_payment_time_m) as avg_inv_time from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as Avg_InvPaymentTime, ");
        sb.append("(select sum(num_invoices_awaiting_litigation_outcome_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("(select sum(val_invoices_awaiting_litigation_outcome_m) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from chorganisation chorganisation where chorganisation.id=:pChorganisationId ");
        
        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();
        StringBuilder sb = new StringBuilder();

        sb.append("select ");
        // Number of Claim Notifications Submitted
        sb.append("(select sum(num_claims_submitted_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(num_claims_accepted_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(num_claimrejections_accepted_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("(select sum(num_claims_pending_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimsAwaitingToBeProcessed, ");
        sb.append("(select sum(num_claims_closed_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(num_invoices_submitted_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesSubmitted, ");
        sb.append("(select sum(val_invoices_submitted_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesSubmitted, ");
        sb.append("(select sum(num_invoices_accepted_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesAccepted, ");
        sb.append("(select sum(val_invoices_accepted_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesAccepted, ");
        sb.append("(select sum(num_invoices_rejected_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesRejected,");
        sb.append("(select sum(val_invoices_rejected_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesRejected, ");
        sb.append("(select sum(num_invoices_pending_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesPending, ");
        sb.append("(select sum(val_invoices_pending_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesPending, ");
        sb.append("(select sum(num_invoices_awaiting_liability_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesAwaitingLiabilityResolution, ");
        sb.append("(select sum(val_invoices_awaiting_liability_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesAwaitingLiabilityResolution, ");
        sb.append("(select sum(num_invoices_closed_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesClosed, ");
        sb.append("(select sum(val_invoices_closed_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesClosed, ");
        sb.append("(select sum(num_invoices_logged_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(val_invoices_logged_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(num_invoices_received_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_invoices_received_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesPaymentReceived, ");
        sb.append("(select avg(avg_inv_payment_time_c) as avg_inv_time from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as Avg_InvPaymentTime, ");
        sb.append("(select sum(num_invoices_awaiting_litigation_outcome_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("(select sum(val_invoices_awaiting_litigation_outcome_c) as no_count from dashboard where (insurer_id = :pInsurerId or :pInsurerId < 0) and (cho_claim_owner_id = :pCHOClaimOwnerId or :pCHOClaimOwnerId < 0) and chorganisation_id=chorganisation.id and complete=true) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from chorganisation chorganisation where chorganisation.id=:pChorganisationId ");
        
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

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    private Map getQueryParameters() {
        Map queryParameters = new HashMap();

        Integer insurerId = -1;
        Integer choClaimOwnerId = -1;

        String insurerIdRaw = ((String[]) this.extParameters.get("insurerId"))[0].toString();
        if (!insurerIdRaw.isEmpty()) {
            insurerId = Integer.parseInt(insurerIdRaw);
        }

        String choClaimOwnerIdRaw = ((String[]) this.extParameters.get("choClaimOwnerId"))[0].toString();
        if (!choClaimOwnerIdRaw.isEmpty()) {
            choClaimOwnerId = Integer.parseInt(choClaimOwnerIdRaw);
        }

        queryParameters.put("pChorganisationId", chorganisation.getId());
        queryParameters.put("pInsurerId", insurerId);
        queryParameters.put("pCHOClaimOwnerId", choClaimOwnerId);

        logger.debug("pChorganisationId :" + chorganisation.getId() + "pInsurerId :" + insurerId + "choClaimOwnerId :" + choClaimOwnerId );
        
        return queryParameters;
        
    }

    public void setExtParameters(Map extParameters) {
        this.extParameters = extParameters;
    }
}
