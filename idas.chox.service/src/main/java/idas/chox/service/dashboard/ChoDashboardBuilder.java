package idas.chox.service.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idas.chox.core.model.Chorganisation;
import idas.chox.data.services.BaseDataService;

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
        sb.append("sum(num_claims_submitted_w) as n_ClaimNotificationsSubmitted, ");
        sb.append("sum(num_claims_accepted_w) as n_ClaimNotificationsAccepted, ");
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
        sb.append("avg(avg_inv_payment_time_w) as Avg_InvPaymentTime, ");
        sb.append("sum(num_invoices_awaiting_litigation_outcome_w) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("sum(val_invoices_awaiting_litigation_outcome_w) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from dashboard where (insurer_id in (:pInsurerId) or -1 in (:pInsurerId)) and  (cho_claim_owner_id in (:pCHOClaimOwnerId) or -1 in (:pCHOClaimOwnerId)) and chorganisation_id = :pChorganisationId and complete=true");

        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getMonthToDate() {
        Map queryParameters = getQueryParameters();
        StringBuilder sb = new StringBuilder();

        sb.append("select  ");
        // Number of Claim Notifications Submitted
        sb.append("sum(num_claims_submitted_m) as n_ClaimNotificationsSubmitted, ");
        sb.append("sum(num_claims_accepted_m) as n_ClaimNotificationsAccepted, ");
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
        sb.append("avg(avg_inv_payment_time_m) as Avg_InvPaymentTime, ");
        sb.append("sum(num_invoices_awaiting_litigation_outcome_m) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("sum(val_invoices_awaiting_litigation_outcome_m) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from dashboard where (insurer_id in (:pInsurerId) or -1 in (:pInsurerId)) and  (cho_claim_owner_id in (:pCHOClaimOwnerId) or -1 in (:pCHOClaimOwnerId)) and chorganisation_id = :pChorganisationId and complete=true");
        
        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();
        StringBuilder sb = new StringBuilder();

        sb.append("select ");
        // Number of Claim Notifications Submitted
        sb.append("sum(num_claims_submitted_c) as n_ClaimNotificationsSubmitted, ");
        sb.append("sum(num_claims_accepted_c) as n_ClaimNotificationsAccepted, ");
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
        sb.append("avg(avg_inv_payment_time_c) as Avg_InvPaymentTime, ");
        sb.append("sum(num_invoices_awaiting_litigation_outcome_c) as n_InvoicesAwaitingLitigationOutcome, ");
        sb.append("sum(val_invoices_awaiting_litigation_outcome_c) as v_InvoicesAwaitingLitigationOutcome ");
        sb.append("from dashboard where (insurer_id in (:pInsurerId) or -1 in (:pInsurerId)) and  (cho_claim_owner_id in (:pCHOClaimOwnerId) or -1 in (:pCHOClaimOwnerId)) and chorganisation_id = :pChorganisationId and complete=true");
        
        return build(queryParameters, sb.toString());
    }

    private DashBoardViewData build(Map<String, Object> queryParameters, String query) {
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
        Map<String, Object> queryParameters = new HashMap();

        List<Integer> insurerIds = new ArrayList<>();
        List<Integer> claimOwnerIds = new ArrayList<>();

        String insurerIdRaw = ((String[]) this.extParameters.get("insurerId"))[0];
        if (!insurerIdRaw.isEmpty()) {
            for (String insId : insurerIdRaw.split(",")) {
                insurerIds.add(Integer.parseInt(insId));
            }
        } else {
            insurerIds.add(-1);
        }

        String choClaimOwnerIdRaw = ((String[]) this.extParameters.get("choClaimOwnerId"))[0];
        if (!choClaimOwnerIdRaw.isEmpty()) {
            for (String ownerId : choClaimOwnerIdRaw.split(",")) {
                claimOwnerIds.add(Integer.parseInt(ownerId));
            }
        } else {
            claimOwnerIds.add(-1);
        }

        queryParameters.put("pChorganisationId", chorganisation.getId());
        queryParameters.put("pInsurerId", insurerIds);
        queryParameters.put("pCHOClaimOwnerId", claimOwnerIds);

        logger.debug("pChorganisationId :" + chorganisation.getId() + "pInsurerId :" + insurerIds.toString() + "choClaimOwnerId :" + claimOwnerIds.toString());
        
        return queryParameters;
        
    }

    public void setExtParameters(Map extParameters) {
        this.extParameters = extParameters;
    }
}
