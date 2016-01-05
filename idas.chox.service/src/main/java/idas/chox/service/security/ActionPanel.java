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
    public static final String UPDATE_MANUAL_INVOICE_AGREE = "UpdateManualInvoiceAgreeQuantum";
    public static final String UPDATE_MANUAL_INVOICE_PAID = "UpdateManualInvoicePaid";
    public static final String UPDATE_MANUAL_INVOICE_WG_AND_OWNER = "AssignManualInvoiceOwner";
    public static final String APPROVE_CONTESTED_INVOICE_TO_ENG = "InvoiceReferToCH";
    public static final String REVIEW_BY_ENGINEER = "ClaimReviewByEng";
    public static final String CONTEST_OR_ACCEPT_REJECTED_SUBSCRIBER_CLAIM = "SubscriberClaimRejectionAccept";

    // Non-Activity Based panels - no unique activity to assign, therefore the following panels are associated to 'fake' activities
    public static final String UPLOAD_INVOICE_DATA = "uploadInvoiceData";
    public static final String APPROVE_ESCALATED_INVOICE = "approveEscalatedInvoice";
    public static final String APPROVE_CONTESTED_INVOICE = "approveContestedInvoice";
    public static final String RESUBMIT_OR_ACCEPT_CONTESTED_INVOICE = "resubmitOrAcceptContestedInvoice";
    public static final String INVOICE_REFERRED_TO_CLAIMS_HANDLER = "updateInvoiceReferredByEngineer";
    public static final String UPDATE_CLAIM_UPDATED_BY_ENG = "updateClaimUpdatedByEngineer";
    public static final String APPROVE_BRE_PASSED_BY_CLAIM_HANDLER = "approveBREPassedByClaimHandler";
    public static final String ASSIGN_INVOICE_OWNERSHIP = "assignInvoiceOwnership";

    private static final List<String> actionPanelList = new ArrayList<String>(25);
    static {
        actionPanelList.add(ACKNOWLEDGE_CLAIM);
        actionPanelList.add(APPROVE_BRE_PASSED_CLAIM);
        actionPanelList.add(APPROVE_BRE_PASSED_BY_CLAIM_HANDLER);
        actionPanelList.add(APPROVE_CONTESTED_INVOICE);
        actionPanelList.add(APPROVE_CONTESTED_INVOICE_TO_ENG);
        actionPanelList.add(APPROVE_ESCALATED_INVOICE);
        actionPanelList.add(ASSIGN_CLAIM_OWNERSHIP);
        actionPanelList.add(ASSIGN_INVOICE_OWNERSHIP);
        actionPanelList.add(AWAITING_CAR_HIRE_INFO);
        actionPanelList.add(CLAIM_PENDING);
        actionPanelList.add(CONTEST_OR_ACCEPT_REJECTED_CLAIM);
        actionPanelList.add(CONTEST_OR_ACCEPT_REJECTED_SUBSCRIBER_CLAIM);
        actionPanelList.add(INVOICE_REFERRED_TO_CLAIMS_HANDLER);
        actionPanelList.add(INVOICE_RESUBMIT);
        actionPanelList.add(LOG_INVOICE_PAYMENT);
        actionPanelList.add(REGISTERED_CLAIM_BY_FNOL);
        actionPanelList.add(RESUBMIT_OR_ACCEPT_CONTESTED_INVOICE);
        actionPanelList.add(REVIEW_BY_ENGINEER);
        actionPanelList.add(ROUTE_CLAIM);
        actionPanelList.add(UPDATE_CLAIM_UPDATED_BY_ENG);
        actionPanelList.add(UPLOAD_INVOICE_DATA);
        actionPanelList.add(UPDATE_LIABILITY);
        actionPanelList.add(UPDATE_MANUAL_INVOICE_WG_AND_OWNER);
        actionPanelList.add(UPDATE_MANUAL_INVOICE_AGREE);
        actionPanelList.add(UPDATE_MANUAL_INVOICE_PAID);
        actionPanelList.add(UPDATE_PAYMENT_RECEIVED);
    }

    public static List<String> getPanelActions() {
        return actionPanelList;
    }
}
