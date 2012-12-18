package idas.chox.service.workflow.activities;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.TaskService;

public class FullInvoicePaymentReceived extends BaseActivity {
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }


    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to move claim to 'InvoicePaymentReceived'.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
    	claim.getInvoice().setInterimPaymentReceived(claim.getInvoice().getInterimPaymentMade());
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }

}