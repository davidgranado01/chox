package idas.chox.service.workflow.activities;

import org.hibernate.util.StringHelper;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.services.TaskService;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class InvoiceRejectionAccept extends BaseActivity {

    private TaskService taskService;
    private String supportingLiabilityNotes;

    public String getSupportingLiabilityNotes() {
        return supportingLiabilityNotes;
    }

    public void setSupportingLiabilityNotes(String supportingLiabilityNotes) {
        this.supportingLiabilityNotes = supportingLiabilityNotes;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    protected void doProcess(Claim claim) {

        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.newComment(0, "Supporting Notes: " + supportingLiabilityNotes));
        }

        claim.setStatus(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), null, claim.getInvoice().getReasonOfRejection());
//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getMBassador().post(event).now();
        });

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

}
