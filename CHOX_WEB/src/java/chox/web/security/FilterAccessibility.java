/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.security;

import org.acegisecurity.GrantedAuthority;

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
    private boolean contestedInvoicesReferredToInsurerAccessible;
    private boolean invoicesApprovedByBREAccessible;

    public FilterAccessibility(GrantedAuthority[] grantedAuthorities, String claimStatus) {
        ApplicationAccessibility accessibility = ApplicationAccessibility.getInstance();

        rejectedClaimsAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_REJECTED_CLAIMS,
                grantedAuthorities, claimStatus) > 0;
        incorrectInvoiceDataCalculationsAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_INCORRECT_INVOICE_DATA_COLC,
                grantedAuthorities, claimStatus) > 0;
        contestedInvoicesReferredToCHOAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_CONTESTED_INVOICE_REF_CHO,
                grantedAuthorities, claimStatus) > 0;
        claimsAwaitingHireMonitoringInformationAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_HIRE_MONITORING_INFO,
                grantedAuthorities, claimStatus) > 0;
        claimsAwaitingAcknowledgementAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_ACK,
                grantedAuthorities, claimStatus) > 0;
        reSubmittedClaimsAwaitingAcknowledgementAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_RESUBMIT_CLAIM_AWAITING_ACK,
                grantedAuthorities, claimStatus) > 0;
        hireUpdateAnomaliesAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_HIRE_UPDATE_ANOMALIES,
                grantedAuthorities, claimStatus) > 0;
        newClaimsToBeroutedAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_NEW_CLAIM_TO_BE_ROUTED,
                grantedAuthorities, claimStatus) > 0;
        claimsAwaitingClaimsHandlingPaymentAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_CLAIM_HANDLING_PAYMENT,
                grantedAuthorities, claimStatus) > 0;
        approvedInvoicesAwaitingPaymentAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_APPROVED_INVOICE_AWAITING_PAYMENT,
                grantedAuthorities, claimStatus) > 0;
        escalatedInvoicesAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_ESCALATED_INVOICE,
                grantedAuthorities, claimStatus) > 0;
        contestedInvoicesReferredToInsurerAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_CONTESTED_INVOICE_REF_INS,
                grantedAuthorities, claimStatus) > 0;
        invoicesApprovedByBREAccessible = accessibility.checkTabAccessibility(ApplicationAccessibility.FILTER_INVOICE_APPROVED_BY_BRE,
                grantedAuthorities, claimStatus) > 0;
    }

    public boolean isRejectedClaimsAccessible() {
        return rejectedClaimsAccessible;
    }

    public boolean isIncorrectInvoiceDataCalculationsAccessible() {
        return incorrectInvoiceDataCalculationsAccessible;
    }

    public boolean isContestedInvoicesReferredToCHOAccessible() {
        return contestedInvoicesReferredToCHOAccessible;
    }

    public boolean isClaimsAwaitingHireMonitoringInformationAccessible() {
        return claimsAwaitingHireMonitoringInformationAccessible;
    }

    public boolean isClaimsAwaitingAcknowledgementAccessible() {
        return claimsAwaitingAcknowledgementAccessible;
    }

    public boolean isReSubmittedClaimsAwaitingAcknowledgementAccessible() {
        return reSubmittedClaimsAwaitingAcknowledgementAccessible;
    }

    public boolean isHireUpdateAnomaliesAccessible() {
        return hireUpdateAnomaliesAccessible;
    }

    public boolean isNewClaimsToBeroutedAccessible() {
        return newClaimsToBeroutedAccessible;
    }

    public boolean isClaimsAwaitingClaimsHandlingPaymentAccessible() {
        return claimsAwaitingClaimsHandlingPaymentAccessible;
    }

    public boolean isApprovedInvoicesAwaitingPaymentAccessible() {
        return approvedInvoicesAwaitingPaymentAccessible;
    }

    public boolean isEscalatedInvoicesAccessible() {
        return escalatedInvoicesAccessible;
    }

    public boolean isContestedInvoicesReferredToInsurerAccessible() {
        return contestedInvoicesReferredToInsurerAccessible;
    }

    public boolean isInvoicesApprovedByBREAccessible() {
        return invoicesApprovedByBREAccessible;
    }
}
