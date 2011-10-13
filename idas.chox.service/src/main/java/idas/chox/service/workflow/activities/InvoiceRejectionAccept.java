package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.TaskService;
import java.util.List;
import org.hibernate.util.StringHelper;
import org.springframework.security.AccessDeniedException;

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
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")
                && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to accept invoice rejection.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {

        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.New(0, "Supporting Notes: " + supportingLiabilityNotes));
        }

        claim.setStatus(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), null, claim.getInvoice().getReasonOfRejection());

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }
}
