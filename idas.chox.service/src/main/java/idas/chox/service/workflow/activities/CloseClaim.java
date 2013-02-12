package idas.chox.service.workflow.activities;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.TaskService;

public class CloseClaim extends BaseActivity {
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }


    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")  
                && !securityInfoProvider.getIsCHOXAdmin()
                && !(securityInfoProvider.isInRoleOf("ROLE_INS") 
                      && (claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_APPROVED) 
                           || claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_REJECTED)
                           || claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_UNASSIGNED)
                           || claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_CONTESTED)))) {
            throw new AccessDeniedException("Not in correct role to close a claim.");
        }
        
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
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_APPROVED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_REJECTED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
    }
}