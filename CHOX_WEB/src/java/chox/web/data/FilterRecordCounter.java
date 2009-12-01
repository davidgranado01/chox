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
        return service.getCountByStatus(ClaimStatus.CLAIM_REJECTED, false, false);
    }

    public long getIncorrectInvoiceDataCalculationsCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT, false, false);
    }

    public long getContestedInvoicesReferredToCHOCount() {
        return service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO, false, false);
    }

    public long getClaimsAwaitingHireMonitoringInformationCount() {
        return service.getCountByStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO, false, false);
    }

    public long getNewClaimsToBeroutedCount() {
       return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, false, false);
    }

    public long getClaimsAwaitingClaimsHandlingPaymentCount() {
        return service.getNonDEPaymentLogCount();
    }

    public long getEscalatedInvoicesCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_ESCALATED, false, false);
    }
    
    public long getClaimReferredToEngineerCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REF_TO_ENG, false, false);
    }
    
    public long getClaimReferredToFNOLCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL, false, false);
    }    
    
    public long getPenaltyChargesAppliedCount() {
        return service.getPenaltyChargeAppliedCount();
    }

    public long getAwaitingInvoiceDataCount()
    {
        return service.getCountByStatus(ClaimStatus.AWAITING_INVOICE_DATA, false, false);
    }

    public long getInvoicePaymentLoggedCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED, false, false);
    }     
    
    public long getContestedInvoiceReferToEngAccessibleCount()
    {
        return service.getCountByStatus(ClaimStatus.INVOICE_REF_TO_ENG, false, false);
    }  

    public long getClaimUnacknowledgedUnassignedAccessible()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, true, false);
    }

    /***********************************************
     * CREDIT HIRE AND CLAIM OWNERSHIP QUEUE
     ***********************************************/

    boolean isCheckWorkGroup = true;
    boolean isCheckOwnership = true;

    public long getClaimsAwaitingAcknowledgementCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, isCheckWorkGroup, isCheckOwnership);
    }

    public long getReSubmittedClaimsAwaitingAcknowledgementCount() {
        return service.getCountByStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED, isCheckWorkGroup, isCheckOwnership);
    }

    public long getClaimPendingCount()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_PENDING, isCheckWorkGroup, isCheckOwnership);
    }

    public long getClaimUpdatedByEngineerCount()
    {
        return service.getCountByStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG, isCheckWorkGroup, isCheckOwnership);
    }

    public long getApprovedInvoicesAwaitingPaymentCount() {
        return service.getCountByStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT, isCheckWorkGroup, isCheckOwnership);
    }

    public long getContestedInvoicesReferredToInsurerCount() {
        return service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS, isCheckWorkGroup, isCheckOwnership);
    }

    public long getInvoicesApprovedByBRECount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE, isCheckWorkGroup, isCheckOwnership);
    }
    
    public long getEscalatedInvoicesToHandlerCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_ESCALATED_TO_CH, isCheckWorkGroup, isCheckOwnership);
    }

    public long getInvoiceReferredToClaimsHandlerCount() {
        return service.getCountByStatus(ClaimStatus.INVOICE_REF_TO_CH, isCheckWorkGroup, isCheckOwnership);
    }

    public long getHireUpdateAnomaliesCount() {
       return service.getHireUpdateWarningCountNumber(isCheckWorkGroup, isCheckOwnership);
    }

}
