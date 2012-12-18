package idas.chox.service.workflow.activities;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.TaskService;

public class InvoicePaymentReceived extends BaseActivity {
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
        if (claim.getInvoice().getInterimPaymentMade() != null && (claim.getInvoice().getInterimPaymentReceived() == null
                || claim.getInvoice().getInterimPaymentMade().compareTo(claim.getInvoice().getInterimPaymentReceived()) != 0)) {
            claim.getInvoice().setInterimPaymentReceived(claim.getInvoice().getInterimPaymentMade());
            claim.addComment(Comment.New(0, "Updating interim payments received to £" + claim.getInvoice().getInterimPaymentReceived()
                    + " (as full payment has been marked as received)."));
        }
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
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

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }

}