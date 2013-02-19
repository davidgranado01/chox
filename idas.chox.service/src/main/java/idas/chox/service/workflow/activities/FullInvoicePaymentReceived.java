package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.TaskService;

public class FullInvoicePaymentReceived extends BaseActivity {
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }


    @Override
    protected void doProcess(Claim claim) {
    	claim.getInvoice().setInterimPaymentReceived(claim.getInvoice().getInterimPaymentMade());
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

}