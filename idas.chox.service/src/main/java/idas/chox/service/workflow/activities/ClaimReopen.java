package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.TaskService;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class ClaimReopen extends BaseActivity {
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }


    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        System.out.print(securityInfoProvider.getCurrentUser().getDisplayName() + "  "+securityInfoProvider.getIsCHOXAdmin());
        if (!securityInfoProvider.getIsCHO() && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to re-open claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(claim.getPreviousStatus());
        // Close open tasks on claim
        taskService.autoUndoCompleteTasksForClaim(claim.getId());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
    }
}