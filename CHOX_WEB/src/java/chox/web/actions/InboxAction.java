/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.ClaimStatus;
import chox.services.ClaimService;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class InboxAction extends BaseAction {

    private ClaimService service;

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public Long getUnacknowledgedClaimCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED);
    }

    public Long getAcknowledgedClaimCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_ACKNOWLEDGED);
    }

    public Long getAwaitingPaymentPackCount() {
        return (Long) service.getCountByStatus(ClaimStatus.AWAITING_PAYMENT_PACK);
    }

    public Long getAwaitingCarHireCount() {
        return (Long) service.getCountByStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
    }

    public Long getAwaitingInvoiceCount() {
        return (Long) service.getCountByStatus(ClaimStatus.AWAITING_INVOICE_DATA);
    }

    public Long getClaimRejectedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_REJECTED);
    }

    public Long getClaimRejectedAcceptedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_REJECTED_ACCEPTED);
    }

    public Long getDisputedInvoiceCount() {
        return (Long) service.getCountByStatus(ClaimStatus.DISPUTED_INVOICE);
    }

    public Long getInvoiceApprovedPackCount() {
        return (Long) service.getCountByStatus(ClaimStatus.INVOICE_APPROVED);
    }

    public Long getInvoiceEccalatedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.INVOICE_ESCALATED);
    }

    public Long getInvoicePaymentLoggedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }

    public Long getPaymentPackSuppliedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.PAYMENT_PACK_SUPPLIED);
    }

    public Long getInvoiceRejectedAcceptedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
    }

    public Long getClaimUnrountedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_UNROUNTED);
    }

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }
}