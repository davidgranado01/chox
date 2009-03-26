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
    private boolean claimReferredToEngineerAccessible;
    private boolean claimReferredToFNOLAccessible;
    private boolean penaltyChargesAppliedAccessible;
    private boolean claimPendingAccessible;
    private boolean InvoiceReferredToClaimsHandlerAccessible;
    private boolean InvoicePaymentLoggedAccessible;
    private boolean ClaimUpdatedByEngineerAccessible;
    
    public FilterAccessibility(ApplicationAccessibility accessibility,GrantedAuthority[] grantedAuthorities) {
        
        rejectedClaimsAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_REJECTED_CLAIMS, grantedAuthorities) > 0;
        incorrectInvoiceDataCalculationsAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INCORRECT_INVOICE_DATA_COLC, grantedAuthorities) > 0;
        contestedInvoicesReferredToCHOAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CONTESTED_INVOICE_REF_CHO, grantedAuthorities) > 0;
        claimsAwaitingHireMonitoringInformationAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_HIRE_MONITORING_INFO, grantedAuthorities) > 0;
        claimsAwaitingAcknowledgementAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_ACK, grantedAuthorities) > 0;
        reSubmittedClaimsAwaitingAcknowledgementAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_RESUBMIT_CLAIM_AWAITING_ACK, grantedAuthorities) > 0;
        hireUpdateAnomaliesAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_HIRE_UPDATE_ANOMALIES, grantedAuthorities) > 0;
        newClaimsToBeroutedAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_NEW_CLAIM_TO_BE_ROUTED, grantedAuthorities) > 0;
        claimsAwaitingClaimsHandlingPaymentAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_AWAITING_CLAIM_HANDLING_PAYMENT, grantedAuthorities) > 0;
        approvedInvoicesAwaitingPaymentAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_APPROVED_INVOICE_AWAITING_PAYMENT, grantedAuthorities) > 0;
        escalatedInvoicesAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_ESCALATED_INVOICE, grantedAuthorities) > 0;
        contestedInvoicesReferredToInsurerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CONTESTED_INVOICE_REF_INS, grantedAuthorities) > 0;
        invoicesApprovedByBREAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INVOICE_APPROVED_BY_BRE, grantedAuthorities) > 0;
        claimReferredToEngineerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_REF_ENG, grantedAuthorities) > 0;
        claimReferredToFNOLAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_REF_FNOL, grantedAuthorities) > 0;
        penaltyChargesAppliedAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_PENALTY_CHARGES_APPLIED, grantedAuthorities) > 0;               
        claimPendingAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_PENDING, grantedAuthorities) > 0;   
        InvoiceReferredToClaimsHandlerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INVOICE_REF_TO_CH, grantedAuthorities) > 0; 
        InvoicePaymentLoggedAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_INVOICE_PAYMENT_LOGGED, grantedAuthorities) > 0; 
        ClaimUpdatedByEngineerAccessible = accessibility.checkFilterAccessibility(ApplicationAccessibility.FILTER_CLAIM_UPDATED_BY_ENGINEER, grantedAuthorities) > 0; 
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
    
    public boolean getIsClaimUpdatedByEngineerAccessible(){
        return ClaimUpdatedByEngineerAccessible;
    }
    
}
