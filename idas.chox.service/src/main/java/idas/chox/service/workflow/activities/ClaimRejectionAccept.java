package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.TaskService;
import idas.chox.events.BaseActivityEvent;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class ClaimRejectionAccept extends BaseActivity {

    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), claim.getReasonOfRejection(), null);
//        activityEventGenerator.generate(claim, this);
        for (BaseActivityEvent event : activityEventGenerator.getEvents(claim, this)) {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        }

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

}