/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.data;

import chox.Util.RoleHelper;
import chox.model.ClaimStatus;
import chox.services.ClaimService;

public class FilterRecordCounter {  
    
    private ClaimService service;

    public FilterRecordCounter(ClaimService service) {
        this.service = service;
    }

    public long getRejectedClaimsCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REJECTED, false);
    }

    public long getIncorrectInvoiceDataCalculationsCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT, false);
    }

    public long getContestedInvoicesReferredToCHOCount() {
        return service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO, false);
    }

    public long getClaimsAwaitingHireMonitoringInformationCount() {
        return service.getCountByStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO, false);
    }

    public long getNewClaimsToBeroutedCount() {
       return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, false);
    }

    public long getClaimsAwaitingClaimsHandlingPaymentCount() {
        return service.getNonDEPaymentLogCount();
    }

    public long getEscalatedInvoicesCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_ESCALATED, false);
    }
    
    public long getClaimReferredToEngineerCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REF_TO_ENG, false);
    }
    
    public long getClaimReferredToFNOLCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL, false);
    }    
    
    public long getPenaltyChargesAppliedCount() {
        return service.getPenaltyChargeAppliedCount();
    }
    
    public long getInvoicePaymentLoggedCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED, false);
    }     
    
    public long getContestedInvoiceReferToEngAccessibleCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_REF_TO_ENG, false);
    }  

    /***********************************************
     * CREDIT HIRE AND CLAIM OWNERSHIP QUEUE
     ***********************************************/

    boolean isWorkgroupQueue = true;

    public long getClaimsAwaitingAcknowledgementCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, isWorkgroupQueue);
    }

    public long getReSubmittedClaimsAwaitingAcknowledgementCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED, isWorkgroupQueue);
    }

    public long getClaimPendingCount()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_PENDING, isWorkgroupQueue);
    }

    public long getClaimUpdatedByEngineerCount()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG, isWorkgroupQueue);
    }

    public long getApprovedInvoicesAwaitingPaymentCount() {
        return service.getCountByStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT, isWorkgroupQueue);
    }

    public long getContestedInvoicesReferredToInsurerCount() {
        return service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS, isWorkgroupQueue);
    }

    public long getInvoicesApprovedByBRECount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE, isWorkgroupQueue);
    }
    
    public long getEscalatedInvoicesToHandlerCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_ESCALATED_TO_CH, isWorkgroupQueue);
    }

    public long getInvoiceReferredToClaimsHandlerCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_REF_TO_CH, isWorkgroupQueue);
    }

    public long getClaimUnacknowledgedUnassignedAccessible()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, isWorkgroupQueue);
    }

    public long getHireUpdateAnomaliesCount() {
       return service.getHireUpdateWarningCountNumber(isWorkgroupQueue);
    }

}
