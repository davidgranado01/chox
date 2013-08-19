package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.services.TaskService;

public class CloseClaim extends BaseActivity {
    private TaskService taskService;
    private String closeReason;
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }


    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        /*
         * bug#2380 - Claim with open Interim Payment cannot be closed
         */
        if (claim.getInvoice() != null && claim.getInvoice().getInterimPaymentMade() != null
                && claim.getInvoice().getInterimPaymentMade().compareTo(claim.getInvoice().getInterimPaymentReceived()) != 0) {
            throw new Exception("It is currently not possible to close this claim as it contains an outstanding interim payment.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.CLAIM_CLOSED);
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
        if (closeReason != null && !closeReason.isEmpty()) {
            claim.addComment(Comment.newComment(0, String.format("Claim Closed: %s", closeReason)));
        }
    }
}