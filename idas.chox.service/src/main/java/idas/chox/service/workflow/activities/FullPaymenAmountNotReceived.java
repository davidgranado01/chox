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

public class FullPaymenAmountNotReceived extends BaseActivity {
    private TaskService taskService;
    private BigDecimal interimPaymentMade;
    
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
    			"however the CHO has marked the Payment as an Interim Payment of £" + interimPaymentMade + "as there is an amount outstanding. Please check " +
    					"the payment details in your claim system and mark the claim as Invoice Payment Logged when the outstanding amount has been paid."));
    	claim.getInvoice().setInterimPaymentReceived(interimPaymentMade);
    	claim.getInvoice().setInterimPaymentMade(interimPaymentMade);
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        // Close open tasks on claim
        //XXX Do we need to close the tasks???
        taskService.autoCompleteTasksForClaim(claim.getId());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
    	expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.INVOICE_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
    }

	public BigDecimal getInterimPaymentMade() {
		return interimPaymentMade;
	}

	public void setInterimPaymentMade(BigDecimal interimPaymentMade) {
		this.interimPaymentMade = interimPaymentMade;
	}

	
}