package idas.chox.service.workflow.activities;

import idas.chox.core.model.BreBand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.TaskService;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class ClaimRevert extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRevert.class);
    private ClaimService claimService;
    private TaskService taskService;
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((securityInfoProvider.getIsCHO() && !claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REJECTION_ACCEPTED) && !claim.getStatus().equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED)
                && !claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REJECTION_CONTESTED))
                || (securityInfoProvider.getIsINS() && !claim.getStatus().equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)
                && !claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT) && !claim.getStatus().equals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REF_TO_ENG) && !claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_ENG)
                && !claim.getStatus().equals(ClaimStatus.MANUAL_INVOICE_PAID) && !claim.getStatus().equals(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED)
                && !claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA))) {
            throw new AccessDeniedException("Not in correct role to revert claim in status '" + claim.getStatus() + "'.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        boolean reOpenTasks = false;
        boolean reCloseTasks = false;
        if (ClaimStatus.CLAIM_CLOSED.equals(claim.getStatus())
                || ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getStatus())
                || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getStatus())
                || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getStatus()))
            reOpenTasks = true; // indicates reverting from a closed to an open state
        if (ClaimStatus.CLAIM_CLOSED.equals(claim.getPreviousStatus())
                || ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getPreviousStatus())
                || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getPreviousStatus())
                || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getPreviousStatus()))
            reCloseTasks = true; // Indicates reverting to a closed state
        LOG.debug("Reverting status for claim: {} (id={})", claim.getChoReference(), claim.getId());
        String originalStatus = claim.getStatus();
        if (claimService.revertClaim(claim.getId()) != null) {
            LOG.info("Claim status reverted for claim with id={} (Supplier reference '{}') : {} -> {}", new Object[] {claim.getId(), claim.getChoReference(), originalStatus, claim.getStatus()});
            if (reOpenTasks)
                taskService.autoUndoCompleteTasksForClaim(claim.getId());
            else if (reCloseTasks)
                taskService.autoCompleteTasksForClaim(claim.getId());

            if (claim.getInvoice() != null && claim.getBreBand().isAllowPenaltyCharges() && claim.getInvoice().getInvoicedDays() > 30 && getWorkflowContext().getSecurityInfoProvider().getIsCHO()
                    && ((originalStatus.equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT))
                    || (originalStatus.equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED) && claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO))
                    || (originalStatus.equals(ClaimStatus.CLAIM_CLOSED) && (
                            claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT) || claim.getStatus().equals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION)
                            || claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO) || claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS)
                            || claim.getStatus().equals(ClaimStatus.INVOICE_APPROVED_BY_BRE) || claim.getStatus().equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)
                            || claim.getStatus().equals(ClaimStatus.INVOICE_ESCALATED) || claim.getStatus().equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)
                            || claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_CH) || claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_ENG))
                    ))) {
                setMessage("Claim reverted to status '" + claim.getStatus()
                        + "' and the penalty charge counter started " + claim.getInvoice().getInvoicedDays()
                        + " days ago, please confirm the correct penalty charges have been applied to the invoice.");
            }
            else
                setMessage("Claim successfully reverted back from '" + originalStatus + "' to '" + claim.getStatus() + "'.");
        }
        else
            LOG.warn("Failed to revert claim status for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
    }
    
    @Override
    protected void afterProcess(Claim claim) throws Exception {
// Claim already saved in the service, so we shouldn't need to do this
//        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
//        getDataService().save(claim);

        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }


    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_PAID);
    }
}