package idas.chox.service.security;

import java.util.ArrayList;
import java.util.List;

public class ActionPanel {

    // Activity Based
    public static final String ROUTE_CLAIM = "AssignWorkgroup";
    public static final String ACKNOWLEDGE_CLAIM = "AcknowledgeClaim";
    public static final String CONTEST_OR_ACCEPT_REJECTED_CLAIM = "ClaimRejectionContest";
    public static final String AWAITING_CAR_HIRE_INFO = "ClaimAwaitingCarHireInfo";
    public static final String INVOICE_RESUBMIT = "InvoiceResubmit";
    public static final String APPROVE_BRE_PASSED_CLAIM = "InvoiceAccepted";
    public static final String LOG_INVOICE_PAYMENT = "InvoicePaymentLogged";
    public static final String REGISTERED_CLAIM_BY_FNOL = "ClaimRegisterByFnol";
    public static final String CLAIM_PENDING = "ClaimPending";
    public static final String UPDATE_PAYMENT_RECEIVED = "InvoicePaymentReceived";
    public static final String ASSIGN_CLAIM_OWNERSHIP = "AssignOwner";
    public static final String UPDATE_LIABILITY = "ResolveLiability";
    public static final String UPDATE_MANUAL_INVOICE = "UpdateManualInvoicePaid";
    public static final String UPDATE_MANUAL_INVOICE_WG_AND_OWNER = "AssignManualInvoiceOwner";
    public static final String APPROVE_CONTESTED_INVOICE_TO_ENG = "InvoiceReferToCH";
    
    // Non-Activity Based panels - no unique activity to assign, therefore the following panels are assocuated to 'fake' activities
    public static final String CONTEST_OR_ACCEPT_REJECTED_SUBSCRIBER_CLAIM = "contestOrAcceptRejectedSubscriberClaim";
    public static final String UPLOAD_INVOICE_DATA = "uploadInvoiceData";
    public static final String APPROVE_ESCALATED_INVOICE = "approveEscalatedInvoice";
    public static final String APPROVE_CONTESTED_INVOICE = "approveContestedInvoice";
    public static final String RESUBMIT_OR_ACCEPT_CONTESTED_INVOICE = "resubmitOrAcceptContestedInvoice";
    public static final String REVIEW_BY_ENGINEER = "reviewByEngineer";
    public static final String INVOICE_REFERRED_TO_CLAIMS_HANDLER = "updateInvoiceReferredByEngineer";
    public static final String UPDATE_CLAIM_UPDATED_BY_ENG = "updateClaimUpdatedByEngineer";
    public static final String APPROVE_BRE_PASSED_BY_CLAIM_HANDLER = "approveBREPassedByClaimHandler";
    public static final String ASSIGN_INVOICE_OWNERSHIP = "assignInvoiceOwnership";

    public static List<String> getPanelActions() {
        List<String> action = new ArrayList<String>();
        action.add(ROUTE_CLAIM);
        action.add(ACKNOWLEDGE_CLAIM);
        action.add(CONTEST_OR_ACCEPT_REJECTED_CLAIM);
        action.add(CONTEST_OR_ACCEPT_REJECTED_SUBSCRIBER_CLAIM);
        action.add(AWAITING_CAR_HIRE_INFO);
        action.add(UPLOAD_INVOICE_DATA);
        action.add(INVOICE_RESUBMIT);
        action.add(APPROVE_BRE_PASSED_CLAIM);
        action.add(APPROVE_ESCALATED_INVOICE);
        action.add(APPROVE_CONTESTED_INVOICE);
        action.add(RESUBMIT_OR_ACCEPT_CONTESTED_INVOICE);
        action.add(LOG_INVOICE_PAYMENT);
        action.add(REVIEW_BY_ENGINEER);
        action.add(REGISTERED_CLAIM_BY_FNOL);
        action.add(CLAIM_PENDING);
        action.add(INVOICE_REFERRED_TO_CLAIMS_HANDLER);
        action.add(UPDATE_PAYMENT_RECEIVED);
        action.add(UPDATE_CLAIM_UPDATED_BY_ENG);
        action.add(APPROVE_CONTESTED_INVOICE_TO_ENG);
        action.add(APPROVE_BRE_PASSED_BY_CLAIM_HANDLER);
        action.add(ASSIGN_CLAIM_OWNERSHIP);
        action.add(UPDATE_LIABILITY);
        action.add(ASSIGN_INVOICE_OWNERSHIP);
        action.add(UPDATE_MANUAL_INVOICE_WG_AND_OWNER);
        action.add(UPDATE_MANUAL_INVOICE);
        return action;
    }
}
