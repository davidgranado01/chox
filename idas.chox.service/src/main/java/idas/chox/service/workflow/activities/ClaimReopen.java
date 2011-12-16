package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.TaskService;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimReopen extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimReopen.class);
    private TaskService taskService;
    private ClaimService claimService;

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
        if (securityInfoProvider.getIsINS() && !ClaimType.isInsurerUpload(claim.getClaimType())) {
            throw new AccessDeniedException("Not in correct role to re-open claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Re-opening (reverting) claim: {} (id={})", claim.getChoReference(), claim.getId());
        if (claimService.revertClaim(claim.getId()) != null) {
            LOG.info("Claim re-opened for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
//        claim.setStatus(claim.getPreviousStatus());
        // Close open tasks on claim
            taskService.autoUndoCompleteTasksForClaim(claim.getId());
        }
        else
            LOG.warn("Failed to re-open claim for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
    }

    /*
     * As we do not wish to log this status change in the audit trail table, we will
     * override the 'afterProcess' method.
     */
    @Override
    protected void afterProcess(Claim claim) throws Exception {
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }


    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
    }
}