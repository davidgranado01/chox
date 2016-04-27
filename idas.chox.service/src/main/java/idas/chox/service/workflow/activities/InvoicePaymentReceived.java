package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.services.TaskService;

public class InvoicePaymentReceived extends BaseActivity {
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }


    @Override
    protected void doProcess(Claim claim) {
        if (claim.getInvoice().getInterimPaymentMade() != null && (claim.getInvoice().getInterimPaymentReceived() == null
                || claim.getInvoice().getInterimPaymentMade().compareTo(claim.getInvoice().getInterimPaymentReceived()) != 0)) {
            claim.getInvoice().setInterimPaymentReceived(claim.getInvoice().getInterimPaymentMade());
            claim.addComment(Comment.newComment(0, "Updating interim payments received to £" + claim.getInvoice().getInterimPaymentReceived()
                    + " (as full payment has been marked as received)."));
        }
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        
        if (claim.getBreBand() == null) {
            claim.setBreBand(getWorkflowContext().getBreBandService().getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        }
        
        selectRandomlyForAuditReview(claim);
        if (claim.getInvoice().getFinalPayment() != null) {
            if (claim.getInvoice().getInterimPaymentMade() != null) {
                claim.getInvoice().setTotalToPay(claim.getInvoice().getInterimPaymentMade().add(claim.getInvoice().getFinalPayment()).setScale(2));
            }
            else {
                claim.getInvoice().setTotalToPay(claim.getInvoice().getFinalPayment());
            }
        }
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

}