/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.security;

import java.util.Set;

/**
 *
 * @author Emmanuel
 */
public class FilterAccessibility {

    private boolean rejectedClaimsAccessible;
    private boolean incorrectInvoiceDataCalculationsAccessible;
    private boolean contestedInvoicesReferredToCHOAccessible;
    private boolean claimsAwaitingHireMonitoringInformationAccessible;
    private boolean claimsAwaitingAcknowledgementAccessible;
    private boolean reSubmittedClaimsAwaitingAcknowledgementAccessible;
    private boolean hireUpdateAnomaliesAccessible;
    private boolean newClaimsToBeroutedAccessible;
    private boolean claimsAwaitingClaimsHandlingPaymentAccessible;
    private boolean approvedInvoicesAwaitingPaymentAccessible;
    private boolean escalatedInvoicesAccessible;
    private boolean invoiceEscalatedToHandlerAccessible;
    private boolean contestedInvoicesReferredToInsurerAccessible;
    private boolean invoicesApprovedByBREAccessible;
    private boolean claimReferredToEngineerAccessible;
    private boolean claimReferredToFNOLAccessible;
    private boolean penaltyChargesAppliedAccessible;
    private boolean claimPendingAccessible;
    private boolean InvoiceReferredToClaimsHandlerAccessible;
    private boolean InvoicePaymentLoggedAccessible;
    private boolean ClaimUpdatedByEngineerAccessible;
    private boolean contestedInvoiceReferToEngAccessible;
    private boolean claimUnacknowledgedUnassignedAccessible;
    private boolean awaitingInvoiceDataAccessible;

    public FilterAccessibility(ApplicationAccessibility accessibility, Set roles) {

        rejectedClaimsAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_REJECTED_CLAIMS, roles) > 0;
        incorrectInvoiceDataCalculationsAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INCORRECT_INVOICE_DATA_COLC, roles) > 0;
        contestedInvoicesReferredToCHOAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CONTESTED_INVOICE_REF_CHO, roles) > 0;
        claimsAwaitingHireMonitoringInformationAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_HIRE_MONITORING_INFO, roles) > 0;
        claimsAwaitingAcknowledgementAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_ACK, roles) > 0;
        reSubmittedClaimsAwaitingAcknowledgementAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_RESUBMIT_CLAIM_AWAITING_ACK, roles) > 0;
        hireUpdateAnomaliesAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_HIRE_UPDATE_ANOMALIES, roles) > 0;
        newClaimsToBeroutedAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_NEW_CLAIM_TO_BE_ROUTED, roles) > 0;
        claimsAwaitingClaimsHandlingPaymentAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_CLAIM_HANDLING_PAYMENT, roles) > 0;
        approvedInvoicesAwaitingPaymentAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_APPROVED_INVOICE_AWAITING_PAYMENT, roles) > 0;
        escalatedInvoicesAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_ESCALATED_INVOICE, roles) > 0;
        invoiceEscalatedToHandlerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_ESCALATED_INVOICE_TO_CH, roles) > 0;
        contestedInvoicesReferredToInsurerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CONTESTED_INVOICE_REF_INS, roles) > 0;
        invoicesApprovedByBREAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INVOICE_APPROVED_BY_BRE, roles) > 0;
        claimReferredToEngineerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_REF_ENG, roles) > 0;
        claimReferredToFNOLAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_REF_FNOL, roles) > 0;
        penaltyChargesAppliedAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_PENALTY_CHARGES_APPLIED, roles) > 0;
        claimPendingAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_PENDING, roles) > 0;
        InvoiceReferredToClaimsHandlerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INVOICE_REF_TO_CH, roles) > 0;
        InvoicePaymentLoggedAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INVOICE_PAYMENT_LOGGED, roles) > 0;
        ClaimUpdatedByEngineerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_UPDATED_BY_ENGINEER, roles) > 0;
        contestedInvoiceReferToEngAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INVOICE_REF_TO_ENG, roles) > 0;
        claimUnacknowledgedUnassignedAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_OWNERSHIP, roles) > 0;
        awaitingInvoiceDataAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_AWAITING_INVOICE_DATA, roles) > 0;
    }

    public boolean getIsAwaitingInvoiceDataAccessible() {
        return awaitingInvoiceDataAccessible;
    }

    public boolean getIsRejectedClaimsAccessible() {
        return rejectedClaimsAccessible;
    }

    public boolean getIsIncorrectInvoiceDataCalculationsAccessible() {
        return incorrectInvoiceDataCalculationsAccessible;
    }

    public boolean getIsContestedInvoicesReferredToCHOAccessible() {
        return contestedInvoicesReferredToCHOAccessible;
    }

    public boolean getIsClaimsAwaitingHireMonitoringInformationAccessible() {
        return claimsAwaitingHireMonitoringInformationAccessible;
    }

    public boolean getIsClaimsAwaitingAcknowledgementAccessible() {
        return claimsAwaitingAcknowledgementAccessible;
    }

    public boolean getIsReSubmittedClaimsAwaitingAcknowledgementAccessible() {
        return reSubmittedClaimsAwaitingAcknowledgementAccessible;
    }

    public boolean getIsHireUpdateAnomaliesAccessible() {
        return hireUpdateAnomaliesAccessible;
    }

    public boolean getIsNewClaimsToBeroutedAccessible() {
        return newClaimsToBeroutedAccessible;
    }

    public boolean getIsClaimsAwaitingClaimsHandlingPaymentAccessible() {
        return claimsAwaitingClaimsHandlingPaymentAccessible;
    }

    public boolean getIsApprovedInvoicesAwaitingPaymentAccessible() {
        return approvedInvoicesAwaitingPaymentAccessible;
    }

    public boolean getIsEscalatedInvoicesAccessible() {
        return escalatedInvoicesAccessible;
    }

    public boolean getIsInvoiceEscalatedToHandlerAccessible() {
        return invoiceEscalatedToHandlerAccessible;
    }

    public boolean getIsContestedInvoicesReferredToInsurerAccessible() {
        return contestedInvoicesReferredToInsurerAccessible;
    }

    public boolean getIsInvoicesApprovedByBREAccessible() {
        return invoicesApprovedByBREAccessible;
    }

    public boolean getIsClaimReferredToEngineerAccessible() {
        return claimReferredToEngineerAccessible;
    }

    public boolean getIsPenaltyChargesAppliedAccessible() {
        return penaltyChargesAppliedAccessible;
    }

    public boolean getIsClaimReferredToFNOLAccessible() {
        return claimReferredToFNOLAccessible;
    }

    public boolean getIsClaimPendingAccessible() {
        return claimPendingAccessible;
    }

    public boolean getIsInvoiceReferredToClaimsHandlerAccessible() {
        return InvoiceReferredToClaimsHandlerAccessible;
    }

    public boolean getIsInvoicePaymentLoggedAccessible() {
        return InvoicePaymentLoggedAccessible;
    }

    public boolean getIsClaimUpdatedByEngineerAccessible() {
        return ClaimUpdatedByEngineerAccessible;
    }

    public boolean getIsContestedInvoiceReferToEngAccessible() {
        return contestedInvoiceReferToEngAccessible;
    }

    public boolean getIsClaimUnacknowledgedUnassignedAccessible() {
        return claimUnacknowledgedUnassignedAccessible;
    }
}
