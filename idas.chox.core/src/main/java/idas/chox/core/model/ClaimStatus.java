package idas.chox.core.model;

import java.util.ArrayList;
import java.util.List;

public class ClaimStatus {
    public static final String CLAIM_UNACKNOWLEDGED_UNROUTED = "ClaimUnacknowledgedUnrouted";
    public static final String CLAIM_UNACKNOWLEDGED_ROUTED = "ClaimUnacknowledgedRouted";
    public static final String CLAIM_REJECTED = "ClaimRejected";
    public static final String SUBSCRIBER_CLAIM_REJECTED = "SubscriberClaimRejected";
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
    public static final String MANUAL_INVOICE_CONTESTED = "ManualInvoiceContested";
    public static final String AWAITING_LITIGATION_OUTCOME = "AwaitingLitigationOutcome";
    public static final String MANUAL_INVOICE_UNASSIGNED = "ManualInvoiceUnassigned";
    private static final List<String> invoiceWithInsurerStatuses = new ArrayList<String>(12);
    private static final List<String> awaitingLiabilityStatuses = new ArrayList<String>(1);
    private static final List<String> invoiceWithCHOStatuses = new ArrayList<String>(3);
    private static final List<String> handlerOutstandingStatusList = new ArrayList<String>(10);

    static {
        invoiceWithInsurerStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        invoiceWithInsurerStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        invoiceWithInsurerStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        invoiceWithInsurerStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        invoiceWithInsurerStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        invoiceWithInsurerStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        invoiceWithInsurerStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        invoiceWithInsurerStatuses.add(ClaimStatus.INVOICE_UNASSIGNED);
        invoiceWithInsurerStatuses.add(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
        invoiceWithInsurerStatuses.add(ClaimStatus.MANUAL_INVOICE_APPROVED);
        invoiceWithInsurerStatuses.add(ClaimStatus.MANUAL_INVOICE_REJECTED);
        invoiceWithInsurerStatuses.add(ClaimStatus.MANUAL_INVOICE_CONTESTED);

        awaitingLiabilityStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);

        invoiceWithCHOStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        invoiceWithCHOStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        invoiceWithCHOStatuses.add(ClaimStatus.AWAITING_LITIGATION_OUTCOME);

        handlerOutstandingStatusList.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        handlerOutstandingStatusList.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        handlerOutstandingStatusList.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        handlerOutstandingStatusList.add(ClaimStatus.INVOICE_REF_TO_CH);
        handlerOutstandingStatusList.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        handlerOutstandingStatusList.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        handlerOutstandingStatusList.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        handlerOutstandingStatusList.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        handlerOutstandingStatusList.add(ClaimStatus.CLAIM_PENDING);
        handlerOutstandingStatusList.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        handlerOutstandingStatusList.add(ClaimStatus.MANUAL_INVOICE_APPROVED);
        handlerOutstandingStatusList.add(ClaimStatus.MANUAL_INVOICE_REJECTED);
        handlerOutstandingStatusList.add(ClaimStatus.MANUAL_INVOICE_CONTESTED);
    }

    public static List<String> getAwaitingLiabilityStatusList() {
        return awaitingLiabilityStatuses;
    }

    public static List<String> getInvoiceWithCHOStatusList() {
        return invoiceWithCHOStatuses;
    }

    public static List<String> getInvoiceWithInsurerStatusList() {
        return invoiceWithInsurerStatuses;
    }


    public static List<String> getAvailableStatus(boolean isWorkgroupEnabled, boolean isClaimOwnershipEnabled,
                                         boolean isFnolEnabled, boolean isEngineersEnabled, boolean isTpiEnabled,
                                         boolean isManualInvoiceAllowed, boolean isSubscriberActivated) {
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
        if (isSubscriberActivated) {
            status.add(SUBSCRIBER_CLAIM_REJECTED);
        }
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
        status.add(AWAITING_LITIGATION_OUTCOME);
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
            status.add(MANUAL_INVOICE_UNASSIGNED);
            status.add(MANUAL_INVOICE_APPROVED);
            status.add(MANUAL_INVOICE_REJECTED);
            status.add(MANUAL_INVOICE_PAID);
            status.add(MANUAL_INVOICE_CONTESTED);
        }

        return status;
    }

    public static List<String> getInsurerClosedStatus(boolean isManualInvoiceAllowed){
        List<String> status = getCompletedStatus(isManualInvoiceAllowed);
        status.add(INVOICE_PAYMENT_LOGGED);

        return status;
    }

    public static List<String> getCompletedStatus(boolean isManualInvoiceAllowed){
        List<String> status = new ArrayList<String>();
        status.add(CLAIM_REJECTION_ACCEPTED);
        status.add(INVOICE_REJECTED_ACCEPTED);
        status.add(INVOICE_PAYMENT_RECEIVED);
        status.add(CLAIM_CLOSED);
        if (isManualInvoiceAllowed) {
            status.add(MANUAL_INVOICE_PAID);
        }
        return status;
    }

    public static List<String> getInsurerOutstandingStatusList(boolean usesEngineers, boolean usesWorkgroups, boolean usesClaimOwnership, boolean usesFnol, boolean usesTPI, boolean usesInsurerUpload) {
        List<String> results = new ArrayList<String>();

        if (usesWorkgroups) {
            results.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        }

        if (usesClaimOwnership) {
            results.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }

        results.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);

        if (usesFnol) {
            results.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        }

        results.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        results.add(ClaimStatus.CLAIM_PENDING);
        if (usesTPI) {
            results.add(ClaimStatus.INVOICE_UNASSIGNED);
        }
        results.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        results.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        results.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        results.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        results.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);

        if (usesEngineers) {
            results.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            results.add(ClaimStatus.INVOICE_REF_TO_CH);
            results.add(ClaimStatus.INVOICE_ESCALATED);
            results.add(ClaimStatus.CLAIM_REF_TO_ENG);
            results.add(ClaimStatus.INVOICE_REF_TO_ENG);
        }
        
        if (usesInsurerUpload) {
            results.add(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
            results.add(ClaimStatus.MANUAL_INVOICE_APPROVED);
            results.add(ClaimStatus.MANUAL_INVOICE_REJECTED);
            results.add(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        }
        return results;
    }


    public static List<String> getHandlerOutstandingStatusList() {
        return handlerOutstandingStatusList;
    }

    public static String getHandlerOutstandingStatusListAsString() {
        return "'" + CLAIM_UNACKNOWLEDGED_ROUTED + "','" + CLAIM_REJECTION_CONTESTED + "','"
                + CLAIM_UPDATE_BY_ENG + "','" + INVOICE_REF_TO_CH + "','"
                + INVOICE_ESCALATED_TO_CH + "','" + CONTESTED_INVOICE_REF_TO_INS + "','"
                + INVOICE_APPROVED_BY_BRE + "','" + AWAITING_INVOICE_PAYMENT + "','" 
                + MANUAL_INVOICE_APPROVED + "','" + MANUAL_INVOICE_CONTESTED + "','"
                + MANUAL_INVOICE_REJECTED + "'";
    }

    public static List<String> getPenaltyChargeExclusionStatus() {
        List<String> exclusionList = new ArrayList<String>();
        exclusionList.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        exclusionList.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        exclusionList.add(ClaimStatus.CLAIM_CLOSED);
        exclusionList.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        exclusionList.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        return exclusionList;
    }

    public static boolean isInPenaltyChargeExclusionStatus(String currentStatus) {
        List<String> exclusionStatuses = getPenaltyChargeExclusionStatus();
        for (String exclusionStatus : exclusionStatuses) {
            if (currentStatus.equalsIgnoreCase(exclusionStatus)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isManualStatus(String status) {
        return status.equals(ClaimStatus.MANUAL_INVOICE_APPROVED)
                || status.equals(ClaimStatus.MANUAL_INVOICE_UNASSIGNED)
                || status.equals(ClaimStatus.MANUAL_INVOICE_CONTESTED)
                || status.equals(ClaimStatus.MANUAL_INVOICE_PAID)
                || status.equals(ClaimStatus.MANUAL_INVOICE_REJECTED);
    }
    
    public static List<String> getPreInvoiceStatus() {
        
        List<String> preInvoiceStatus = new ArrayList<String>();
        preInvoiceStatus.add(CLAIM_UNACKNOWLEDGED_UNROUTED);
        preInvoiceStatus.add(CLAIM_UNACKNOWLEDGED_ROUTED);
        preInvoiceStatus.add(CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        preInvoiceStatus.add(CLAIM_REFERRED_TO_FNOL);
        preInvoiceStatus.add(CLAIM_REF_TO_ENG);
        preInvoiceStatus.add(CLAIM_UPDATE_BY_ENG);
        preInvoiceStatus.add(CLAIM_AWAITING_CAR_HIRE_INFO);
        preInvoiceStatus.add(CLAIM_AWAITING_INVOICE_DATA);
        preInvoiceStatus.add(CLAIM_REJECTION_CONTESTED);
        preInvoiceStatus.add(CLAIM_REJECTED);
        preInvoiceStatus.add(SUBSCRIBER_CLAIM_REJECTED);
        preInvoiceStatus.add(CLAIM_PENDING);
        return preInvoiceStatus;
    }
}