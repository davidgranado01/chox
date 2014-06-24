package idas.chox.core.model;

import idas.chox.core.search.ClaimSearchCriteria;

/**
 *
 * @author emmanuel
 */
public interface Filter { 

    public static final String FILTER_REJECTED_CLAIMS = "RejectedClaims";
    public static final String FILTER_REJECTED_SUBSCRIBER_CLAIMS = "RejectedSubscriberClaims";
    public static final String FILTER_REJECTED_FIXEDFEE_CLAIMS = "RejectedFixedFeeClaims";
    public static final String FILTER_INCORRECT_INVOICE_DATA_COLC = "IncorrectInvoiceDataCalculations";
    public static final String FILTER_CONTESTED_INVOICE_REF_CHO = "ContestedInvoicesReferredToCHO";
    public static final String FILTER_CLAIM_AWAITING_HIRE_MONITORING_INFO = "ClaimsAwaitingHireMonitoringInformation";
    public static final String FILTER_CLAIM_AWAITING_ACK = "ClaimsAwaitingAcknowledgement";
    public static final String FILTER_RESUBMIT_CLAIM_AWAITING_ACK = "ReSubmittedClaimsAwaitingAcknowledgement";
    public static final String FILTER_HIRE_UPDATE_ANOMALIES = "HireUpdateAnomalies";
    public static final String FILTER_NEW_CLAIM_TO_BE_ROUTED = "NewClaimsToBeRouted";
    public static final String FILTER_CLAIM_AWAITING_CLAIM_HANDLING_PAYMENT = "ClaimsAwaitingClaimsHandlingPayment";
    public static final String FILTER_APPROVED_INVOICE_AWAITING_PAYMENT = "ApprovedInvoicesAwaitingPayment";
    public static final String FILTER_PAYMENT_TEAM = "PaymentTeam";
    public static final String FILTER_ESCALATED_INVOICE = "EscalatedInvoices";
    public static final String FILTER_ESCALATED_INVOICE_TO_CH = "InvoiceEscalatedToHandler";
    public static final String FILTER_CONTESTED_INVOICE_REF_INS = "ContestedInvoicesReferredToInsurer";
    public static final String FILTER_INVOICE_APPROVED_BY_BRE = "InvoicesApprovedByBRE";
    public static final String FILTER_CLAIM_REF_ENG = "ClaimReferredToEngineer";
    public static final String FILTER_CLAIM_REF_FNOL = "ClaimReferredToFNOL";
    public static final String FILTER_PENALTY_CHARGES_APPLIED = "PenaltyChargesApplied";
    public static final String FILTER_CLAIM_PENDING = "ClaimPending";
    public static final String FILTER_INVOICE_REF_TO_CH = "InvoiceReferredToClaimsHandler";
    public static final String FILTER_INVOICE_PAYMENT_LOGGED = "InvoicePaymentLogged";
    public static final String FILTER_CLAIM_UPDATED_BY_ENGINEER = "ClaimUpdatedByEngineer";
    public static final String FILTER_INVOICE_REF_TO_ENG = "InvoicesReferredToEngineer";
    public static final String FILTER_CLAIM_OWNERSHIP = "ClaimUnacknowledgedUnassigned";
    public static final String FILTER_AWAITING_INVOICE_DATA = "AwaitingInvoiceData";
    public static final String FILTER_INVOICE_UNASSIGNED = "InvoiceUnassigned";
    public static final String FILTER_MANUAL_INVOICE_APPROVED = "ManualInvoiceBREApproved";
    public static final String FILTER_MANUAL_INVOICE_REJECTED = "ManualInvoiceBRERejected";
    public static final String FILTER_MANUAL_INVOICE_CONTESTED = "ManualInvoiceContested";
    public static final String FILTER_ESCALATED_INVOICES_TO_SUPERVISOR = "EscalatedInvoicesToSupervisor";
    public static final String FILTER_MANUAL_INVOICES_TO_BE_ASSIGNED = "ManualInvoicesToBeAssigned";

    String getKey();

    String getName();

    boolean getIsFilterWorkGroup();
    boolean getIsFilterOwnership();
    boolean getIsFilterSupplierOwnership();
    boolean getIsCheckWorkGroup();
    boolean getIsCheckOwnership();
    boolean getIsCheckFnol();
    boolean getIsCheckEngineers();
    boolean getIsManualFilter();

    ClaimSearchCriteria getClaimSearchCriteria(Boolean isCHO, boolean paymentsTeamActive, ClaimSearchCriteria claimSearchCriteria);
    
}
