package idas.chox.core.model;

import java.util.ArrayList;
import java.util.List;

public class ClaimStatus {
    public static final String CLAIM_UNACKNOWLEDGED_UNROUTED = "ClaimUnacknowledgedUnrouted";
    public static final String CLAIM_UNACKNOWLEDGED_ROUTED = "ClaimUnacknowledgedRouted";
    public static final String CLAIM_REJECTED = "ClaimRejected";
    public static final String CLAIM_REJECTION_ACCEPTED = "ClaimRejectionAccepted";
    public static final String CLAIM_REJECTION_CONTESTED = "ClaimRejectionContested";
    public static final String CLAIM_AWAITING_CAR_HIRE_INFO = "AwaitingCarHireInfo";
    public static final String CLAIM_AWAITING_INVOICE_DATA = "AwaitingInvoiceData";
    public static final String INVOICE_DATA_CALCULATION_INCORRECT = "InvoiceDataCalculationIncorrect";
    public static final String INVOICE_APPROVED_BY_BRE = "InvoiceApprovedByBRE";
    public static final String INVOICE_ESCALATED = "InvoiceEscalated";
    public static final String INVOICE_ESCALATED_TO_CH = "InvoiceEscalatedToHandler";
    public static final String CONTESTED_INVOICE_REF_TO_INS = "ContestedInvoiceReferredToInsurer";
    public static final String CONTESTED_INVOICE_REF_TO_CHO = "ContestedInvoiceReferredToCHO";
    public static final String INVOICE_REJECTED_ACCEPTED = "InvoiceRejectionAccepted";
    public static final String AWAITING_INVOICE_PAYMENT = "AwaitingInvoicePayment";
    public static final String INVOICE_PAYMENT_LOGGED = "InvoicePaymentLogged";
    public static final String CLAIM_REF_TO_ENG = "ClaimReferredToEngineer";
    public static final String CLAIM_REFERRED_TO_FNOL = "ClaimReferredToFNOL";
    public static final String CLAIM_CLOSED = "ClaimClosed";
    public static final String CLAIM_PENDING = "ClaimPending";
    public static final String INVOICE_REF_TO_CH = "InvoiceReferredToClaimsHandler";
    public static final String INVOICE_PAYMENT_RECEIVED = "PaymentReceived";
    public static final String CLAIM_UPDATE_BY_ENG = "ClaimUpdatedByEngineer";
    public static final String INVOICE_REF_TO_ENG = "InvoiceReferredToEngineer";
    public static final String CLAIM_UNACKNOWLEDGED_UNASSIGNED = "ClaimUnacknowledgedUnassigned";
    public static final String AWAITING_LIABILITY_RESOLUTION = "AwaitingLiabilityResolution";
    public static final String INVOICE_UNASSIGNED = "InvoiceUnassigned";
    public static final String MANUAL_INVOICE_APPROVED = "ManualInvoiceBREApproved";
    public static final String MANUAL_INVOICE_REJECTED = "ManualInvoiceBRERejected";
    public static final String MANUAL_INVOICE_PAID = "ManualInvoicePaid";


    public static List<String> getStatus(boolean isWorkgroupEnabled, boolean isClaimOwnershipEnabled,
                                         boolean isFnolEnabled, boolean isEngineersEnabled, boolean isTpiEnabled, boolean isManualInvoiceAllowed) {
        List<String> status = new ArrayList<String>();
        status.add(CLAIM_AWAITING_CAR_HIRE_INFO);
        status.add(CLAIM_AWAITING_INVOICE_DATA);
        status.add(AWAITING_INVOICE_PAYMENT);
        status.add(AWAITING_LIABILITY_RESOLUTION);
        status.add(CLAIM_CLOSED);
        status.add(CLAIM_PENDING);
        if (isFnolEnabled) {
            status.add(CLAIM_REFERRED_TO_FNOL);
        }
        if (isEngineersEnabled) {
            status.add(CLAIM_REF_TO_ENG);
        }
        status.add(CLAIM_REJECTED);
        status.add(CLAIM_REJECTION_ACCEPTED);
        status.add(CLAIM_REJECTION_CONTESTED);
        status.add(CLAIM_UNACKNOWLEDGED_ROUTED);
        if (isClaimOwnershipEnabled) {
            status.add(CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }
        if (isWorkgroupEnabled) {
            status.add(CLAIM_UNACKNOWLEDGED_UNROUTED);
        }
        if (isEngineersEnabled) {
            status.add(CLAIM_UPDATE_BY_ENG);
        }
        status.add(CONTESTED_INVOICE_REF_TO_CHO);
        status.add(CONTESTED_INVOICE_REF_TO_INS);
        status.add(INVOICE_APPROVED_BY_BRE);
        status.add(INVOICE_DATA_CALCULATION_INCORRECT);
        if (isEngineersEnabled) {
            status.add(INVOICE_ESCALATED);
        }
        status.add(INVOICE_ESCALATED_TO_CH);
        status.add(INVOICE_PAYMENT_LOGGED);
        if (isEngineersEnabled) {
            status.add(INVOICE_REF_TO_CH);
            status.add(INVOICE_REF_TO_ENG);
        }
        status.add(INVOICE_REJECTED_ACCEPTED);
        if (isTpiEnabled){
            status.add(INVOICE_UNASSIGNED);
        }
        status.add(INVOICE_PAYMENT_RECEIVED);
        
        if (isManualInvoiceAllowed) {
            status.add(MANUAL_INVOICE_APPROVED);
            status.add(MANUAL_INVOICE_REJECTED);
            status.add(MANUAL_INVOICE_PAID);
        }

        return status;
    }

    public static List<String> getClosedStatus(boolean isManualInvoiceAllowed){
        List<String> status = new ArrayList<String>();
        status = getCompletedStatus();
        status.add(CLAIM_CLOSED);
        status.add(INVOICE_PAYMENT_LOGGED);
        if (isManualInvoiceAllowed) {
            status.add(MANUAL_INVOICE_PAID);
        }
        return status;
    }

    public static List<String> getCompletedStatus(){
        List<String> status = new ArrayList<String>();
        status.add(CLAIM_REJECTION_ACCEPTED);
        status.add(INVOICE_REJECTED_ACCEPTED);
        status.add(INVOICE_PAYMENT_RECEIVED);
        return status;
    }
}