package idas.chox.service.workflow.activities;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.services.TaskService;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class SubscriberClaimRejectionAccept extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriberClaimRejectionAccept.class);
    private ReasonOfRejectionService reasonOfRejectionService;
    private TaskService taskService;
    private boolean hireCarInfoProvided = false;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public boolean isHireCarInfoProvided() {
        return hireCarInfoProvided;
    }

    @Override
    protected void doProcess(Claim claim) {
        /* Subscriber claims move straight to 'AwaitingInvoiceData',
         * except for claims rejected with reason 'Subscriber - Indemnity Issues'
         * or 'Subscriber - Fraud Issues' in which case the claim shall move to
         * the status 'ClaimRejectionAccepted'
         */
        if (reasonOfRejectionService.isSubscriberClaimRejected(claim.getReasonOfRejection())) {
             claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
            // Close open tasks on claim
            taskService.autoCompleteTasksForClaim(claim.getId());
       } else {
            setCurrentStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
            logTransaction(claim, ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getReasonOfRejection(), null);
            hireCarInfoProvided = true;
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), claim.getReasonOfRejection(), null);
//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }
    

    public void setReasonOfRejectionService(ReasonOfRejectionService reasonOfRejectionService) {
        this.reasonOfRejectionService = reasonOfRejectionService;
    }
    
}
