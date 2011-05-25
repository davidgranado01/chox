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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        // Number of Claim Notifications Submitted
        sb.append("(select sum(num_claims_submitted_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(num_claims_accepted_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(num_claimrejections_accepted_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("(select sum(num_claims_closed_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(num_invoices_submitted_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesSubmitted, ");
        sb.append("(select sum(val_invoices_submitted_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesSubmitted, ");
        sb.append("(select sum(num_invoices_accepted_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesAccepted, ");
        sb.append("(select sum(val_invoices_accepted_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesAccepted, ");
        sb.append("(select sum(num_invoices_rejected_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesRejected, ");
        sb.append("(select sum(val_invoices_rejected_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesRejected, ");
        sb.append("(select sum(num_invoices_closed_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesClosed, ");
        sb.append("(select sum(val_invoices_closed_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesClosed, ");
        sb.append("(select sum(num_invoices_logged_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(val_invoices_logged_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(num_invoices_received_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_invoices_received_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_penalty_charges_w) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_PenaltyChargesApplied ");
        sb.append("from insurer insurer where insurer.id = :pInsId ");
        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getMonthToDate() {

        Map queryParameters = getQueryParameters();
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        // Number of Claim Notifications Submitted
        sb.append("(select sum(num_claims_submitted_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(num_claims_accepted_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(num_claimrejections_accepted_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("(select sum(num_claims_closed_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(num_invoices_submitted_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesSubmitted, ");
        sb.append("(select sum(val_invoices_submitted_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesSubmitted, ");
        sb.append("(select sum(num_invoices_accepted_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesAccepted, ");
        sb.append("(select sum(val_invoices_accepted_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesAccepted, ");
        sb.append("(select sum(num_invoices_rejected_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesRejected, ");
        sb.append("(select sum(val_invoices_rejected_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesRejected, ");
        sb.append("(select sum(num_invoices_closed_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesClosed, ");
        sb.append("(select sum(val_invoices_closed_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesClosed, ");
        sb.append("(select sum(num_invoices_logged_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(val_invoices_logged_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(num_invoices_received_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_invoices_received_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_penalty_charges_m) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_PenaltyChargesApplied ");
        sb.append("from insurer insurer where insurer.id = :pInsId ");

        return build(queryParameters, sb.toString());
    }

    public DashBoardViewData getCumulative() {
        Map queryParameters = getQueryParameters();

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        //Number of Claim Notifications Submitted
        sb.append("(select sum(num_claims_submitted_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsSubmitted, ");
        sb.append("(select sum(num_claims_accepted_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsAccepted, ");
        sb.append("(select sum(num_claimrejections_accepted_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsRejectionsAccepted, ");
        sb.append("(select sum(num_claims_pending_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimsAwaitingToBeProcessed, ");
        sb.append("(select sum(num_claims_closed_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_ClaimNotificationsClosed, ");
        sb.append("(select sum(num_invoices_submitted_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesSubmitted, ");
        sb.append("(select sum(val_invoices_submitted_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesSubmitted, ");
        sb.append("(select sum(num_invoices_accepted_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesAccepted, ");
        sb.append("(select sum(val_invoices_accepted_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesAccepted, ");
        sb.append("(select sum(num_invoices_rejected_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesRejected,");
        sb.append("(select sum(val_invoices_rejected_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesRejected, ");
        sb.append("(select sum(num_invoices_pending_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesPending, ");
        sb.append("(select sum(val_invoices_pending_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesPending, ");
        sb.append("(select sum(num_invoices_awaiting_liability_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesAwaitingLiabilityResolution, ");
        sb.append("(select sum(val_invoices_awaiting_liability_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesAwaitingLiabilityResolution, ");
        sb.append("(select sum(num_invoices_closed_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesClosed, ");
        sb.append("(select sum(val_invoices_closed_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesClosed, ");
        sb.append("(select sum(num_invoices_logged_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesPaymentLogged, ");
        sb.append("(select sum(val_invoices_logged_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesPaymentLogged, ");
        sb.append("(select sum(num_invoices_received_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as n_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_invoices_received_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_InvoicesPaymentReceived, ");
        sb.append("(select sum(val_penalty_charges_c) as no_count from dashboard where (chorganisation_id = :pChorganisationId or :pChorganisationId < 0) and (workgroup_id = :pWorkgroupId or :pWorkgroupId < 0) and (claim_owner_id = :pClaimOwnerId or :pClaimOwnerId < 0) and insurer_id = insurer.id and complete=true) as v_PenaltyChargesApplied ");
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
