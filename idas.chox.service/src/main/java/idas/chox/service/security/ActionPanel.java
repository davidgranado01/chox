package idas.chox.service.security;

import java.util.ArrayList;
import java.util.List;

public class ActionPanel {

    public static final String CLAIM_LINE_OF_BUSINESS = "updateclaimlineofbusiness";
    public static final String CLAIM_INSURER_DETAIL = "updateclaiminsurerdetail";
    public static final String CONTEST_OR_ACCEPT_REJECTED_CLAIM = "contestOrAcceptRejectedClaim";
    public static final String APPROVE_CONTESTED_CLAIM = "approveContestedClaim";
    public static final String CLAIM_CAR_HIRE_DETAIL = "updateclaimcarhiredetail";
    public static final String UPLOAD_INVOICE_DATA = "uploadInvoiceData";
    public static final String INVOICE_DETAIL_FOR_CALCULATION_INCORRECT = "updateinvoicedtailforcalculationincorrect";
    public static final String APPROVE_BRE_PASSED_CLAIM = "approveBREPassedClaim";
    public static final String APPROVE_ESCALATED_INVOICE = "approveEscalatedInvoice";
    public static final String APPROVE_CONTESTED_INVOICE = "approveContestedInvoice";
    public static final String RESUBMIT_OR_ACCEPT_CONTESTED_INVOICE = "resubmitOrAcceptContestedInvoice";
    public static final String LOG_INVOICE_PAYMENT = "logInvoicePayment";
    public static final String REVIEW_BY_ENGINEER = "reviewByEngineer";
    public static final String REGISTERED_CLAIM_BY_FNOL = "registeredclaimbyfnol";
    public static final String CLAIM_PENDING = "claimPending";
    public static final String INVOICE_REFERRED_TO_CLAIMS_HANDLER = "updateInvoiceReferredByEngineer";
    public static final String UPDATE_PAYMENT_RECEIVED = "updatePaymentReceived";
    public static final String UPDATE_CLAIM_UPDATED_BY_ENG = "updateClaimUpdatedByEngineer";
    public static final String APPROVE_CONTESTED_INVOICE_TO_ENG = "approveContestedInvoiceToEng";
    public static final String APPROVE_BRE_PASSED_BY_CLAIM_HANDLER = "approveBREPassedByClaimHandler";
    public static final String ASSIGN_CLAIM_OWNERSHIP = "assignClaimOwnership";
    public static final String ASSIGN_INVOICE_OWNERSHIP = "assignInvoiceOwnership";
    public static final String UPDATE_LIABILITY = "updateLiability";
    public static final String UPDATE_MANUAL_INVOICE = "updateManualInvoice";

    public static List<String> getPanelActions() {
        List<String> action = new ArrayList<String>();
        action.add(CLAIM_LINE_OF_BUSINESS);
        action.add(CLAIM_INSURER_DETAIL);
        action.add(APPROVE_CONTESTED_CLAIM);
        action.add(CONTEST_OR_ACCEPT_REJECTED_CLAIM);
        action.add(CLAIM_CAR_HIRE_DETAIL);
        action.add(UPLOAD_INVOICE_DATA);
        action.add(INVOICE_DETAIL_FOR_CALCULATION_INCORRECT);
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
        action.add(UPDATE_MANUAL_INVOICE);
        return action;
    }
}
