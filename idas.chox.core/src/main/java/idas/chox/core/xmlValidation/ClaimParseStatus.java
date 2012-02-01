package idas.chox.core.xmlValidation;

public enum ClaimParseStatus {

    newClaim,
    newSubscriberClaim,
    existClaim,
    existSubscriberClaim,
    ClaimNotEditable,
    newInvoice,
    existInvoice,
    invalidSchema,
    invalidHireState,
    tpiNotRecognized,
    tpiIntervention,
    tpiNotAcceptedByInsurer,
    subscriberNotAcceptedByInsurer,
    hireMonitoringAndNewInvoice,
    existingSupplementaryInvoice,
    newSupplementaryInvoice,
    hireMonitoring,
    invalidClaimStatus,
    insurerVsInsurerInvoice,
    insurerUpload
}