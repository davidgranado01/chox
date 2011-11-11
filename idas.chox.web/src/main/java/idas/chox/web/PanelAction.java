package idas.chox.web;

import java.util.ArrayList;
import java.util.List;

public class PanelAction {

    public static final String ACTION_claimlineofbusiness = "updateclaimlineofbusiness";
    public static final String ACTION_claiminsurerdetail = "updateclaiminsurerdetail";
    public static final String ACTION_contestOrAcceptRejectedClaim = "contestOrAcceptRejectedClaim";
    public static final String ACTION_approveContestedClaim = "approveContestedClaim";
    public static final String ACTION_claimcarhiredetail = "updateclaimcarhiredetail";
    public static final String ACTION_uploadInvoiceData = "uploadInvoiceData";
    public static final String ACTION_invoicedtailforcalculationincorrect = "updateinvoicedtailforcalculationincorrect";
    public static final String ACTION_approveBREPassedClaim = "approveBREPassedClaim";
    public static final String ACTION_approveEscalatedInvoice = "approveEscalatedInvoice";
    public static final String ACTION_approveContestedInvoice = "approveContestedInvoice";
    public static final String ACTION_resubmitOrAcceptContestedInvoice = "resubmitOrAcceptContestedInvoice";
    public static final String ACTION_logInvoicePayment = "logInvoicePayment";
    public static final String ACTION_reviewByEngineer = "reviewByEngineer";
    public static final String ACTION_registeredclaimbyfnol = "registeredclaimbyfnol";
    public static final String ACTION_claimPending = "claimPending";
    public static final String ACTION_InvoiceReferredToClaimsHandler = "updateInvoiceReferredByEngineer";
    public static final String ACTION_updatePaymentReceived = "updatePaymentReceived";
    public static final String ACTION_updateClaimUpdatedByEngineer = "updateClaimUpdatedByEngineer";
    public static final String ACTION_approveContestedInvoiceToEng = "approveContestedInvoiceToEng";
    public static final String ACTION_approveBREPassedByClaimHandler = "approveBREPassedByClaimHandler";
    public static final String ACTION_assignClaimOwnership = "assignClaimOwnership";
    public static final String ACTION_assignInvoiceOwnership = "assignInvoiceOwnership";
    public static final String ACTION_updateLiability = "updateLiability";
    public static final String ACTION_updateManualInvoice = "updateManualInvoice";

    public static List<String> getPanelActions() {
        List<String> action = new ArrayList<String>();
        action.add(ACTION_claimlineofbusiness);
        action.add(ACTION_claiminsurerdetail);
        action.add(ACTION_approveContestedClaim);
        action.add(ACTION_contestOrAcceptRejectedClaim);
        action.add(ACTION_claimcarhiredetail);
        action.add(ACTION_uploadInvoiceData);
        action.add(ACTION_invoicedtailforcalculationincorrect);
        action.add(ACTION_approveBREPassedClaim);
        action.add(ACTION_approveEscalatedInvoice);
        action.add(ACTION_approveContestedInvoice);
        action.add(ACTION_resubmitOrAcceptContestedInvoice);
        action.add(ACTION_logInvoicePayment);
        action.add(ACTION_reviewByEngineer);
        action.add(ACTION_registeredclaimbyfnol);
        action.add(ACTION_claimPending);
        action.add(ACTION_InvoiceReferredToClaimsHandler);
        action.add(ACTION_updatePaymentReceived);
        action.add(ACTION_updateClaimUpdatedByEngineer);
        action.add(ACTION_approveContestedInvoiceToEng);
        action.add(ACTION_approveBREPassedByClaimHandler);
        action.add(ACTION_assignClaimOwnership);
        action.add(ACTION_updateLiability);
        action.add(ACTION_assignInvoiceOwnership);
        action.add(ACTION_updateManualInvoice);
        return action;
    }
}
