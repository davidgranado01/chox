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

    public Long getRejectedClaimsCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_REJECTED);
    }

    public Long getIncorrectInvoiceDataCalculationsCount() {
        return (Long) service.getCountByStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
    }

    public Long getContestedInvoicesReferredToCHOCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }

    public Long getClaimsAwaitingAcknowledgementCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
    }

//    public Long getReSubmittedClaimsAwaitingAcknowledgementCount() {
//        return (Long) service.getCountByStatus(ClaimStatus.);
//    }

//    public Long getHireUpdateAnomaliesCount() {
//        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
//    }

    public Long getNewClaimsToBeRoutedCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }

//    public Long getClaimsAwaitingClaimsHandlingPaymentCount() {
//        return (Long) service.getCountByStatus(ClaimStatus.);
//    }

    public Long getClaimsAwaitingInvoicePaymentCount() {
        return (Long) service.getCountByStatus(ClaimStatus.AWAITING_INVOICE_DATA);
    }

    public Long getApprovedInvoicesAwaitingPaymentCount() {
        return (Long) service.getCountByStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
    }

    public Long getEscalatedInvoicesCount() {
        return (Long) service.getCountByStatus(ClaimStatus.INVOICE_ESCALATED);
    }

    public Long getContestedInvoicesReferredToInsurerCount() {
        return (Long) service.getCountByStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
    }

    public Long getInvoicesApprovedByBRECount() {
        return (Long) service.getCountByStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
    }

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }
}