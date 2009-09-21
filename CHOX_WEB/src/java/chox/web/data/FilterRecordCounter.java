/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.data;

import chox.model.ClaimStatus;
import chox.services.ClaimService;

public class FilterRecordCounter {  
    
    private ClaimService service;

    public FilterRecordCounter(ClaimService service) {
        this.service = service;
    }

    public long getRejectedClaimsCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REJECTED);
    }

    public long getIncorrectInvoiceDataCalculationsCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
    }

    public long getContestedInvoicesReferredToCHOCount() {
        return service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }

    public long getClaimsAwaitingHireMonitoringInformationCount() {
        return service.getCountByStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
    }

    public long getClaimsAwaitingAcknowledgementCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
    }

    public long getReSubmittedClaimsAwaitingAcknowledgementCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
    }

    public long getHireUpdateAnomaliesCount() {
       return service.getHireUpdateWarningCountNumber();
    }

    public long getNewClaimsToBeroutedCount() {
       return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }

    public long getClaimsAwaitingClaimsHandlingPaymentCount() {
        return service.getNonDEPaymentLogCount();
    }

    public long getApprovedInvoicesAwaitingPaymentCount() {
        return service.getCountByStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
    }

    public long getEscalatedInvoicesCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_ESCALATED);
    }

    public long getContestedInvoicesReferredToInsurerCount() {
        return service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
    }

    public long getInvoicesApprovedByBRECount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
    }
    
    public long getClaimReferredToEngineerCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REF_TO_ENG);
    }
    
    public long getClaimReferredToFNOLCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
    }    
    
    public long getPenaltyChargesAppliedCount() {
        return service.getPenaltyChargeAppliedCount();
    }
    
    public long getClaimPendingCount()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_PENDING);
    }

    public long getInvoiceReferredToClaimsHandlerCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_REF_TO_CH);
    }  
    
    public long getInvoicePaymentLoggedCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }     
    
    public long getClaimUpdatedByEngineerCount()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
    }     
    
    public long getContestedInvoiceReferToEngAccessibleCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_REF_TO_ENG);
    }         
}
