package idas.chox.core.xmlValidation;

public enum ClaimParseStatus {

    newClaim,
    existClaim,
    ClaimNotEditable,
    newInvoice,
    existInvoice,
    invalidSchema,
    tpiNotRecognized,
    tpiIntervention,
    tpiNotAcceptedByInsurer,
    hireMonitoringAndNewInvoice,
    existingSupplementaryInvoice,
    newSupplementaryInvoice,
    hireMonitoring,
    invalidClaimStatus,
    insurerVsInsurerInvoice
}