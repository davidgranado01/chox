package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.TaskService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.security.access.AccessDeniedException;

public class InvoicePaymentReceived extends BaseActivity {
    private TaskService taskService;
    private BigDecimal partialInterimPayment;
    
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
    	
    	final Date currentTime = new Date();
    	final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    	claim.addComment(Comment.New(0, "The claim was marked as 'Invoice Payment Logged' on " + sdf.format(currentTime) + "; " +
    			"however the CHO has marked the Payment as an Interim Payment of £" + partialInterimPayment + "as there is an amount outstanding. Please check " +
    					"the payment details in your claim system and mark the claim as Invoice Payment Logged when the outstanding amount has been paid."));
    	claim.getInvoice().setInterimPaymentReceived(partialInterimPayment);
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        // Close open tasks on claim
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }

	public BigDecimal getPartialInterimPayment() {
		return partialInterimPayment;
	}

	public void setPartialInterimPayment(BigDecimal partialInterimPayment) {
		this.partialInterimPayment = partialInterimPayment;
	}

}