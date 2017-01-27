package idas.chox.service.workflow.activities;

import idas.chox.core.model.BreBand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.TaskService;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class ReopenClaim extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ReopenClaim.class);
    private TaskService taskService;
    private BreBandService breBandService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Re-opening (reverting) claim: {} (id={})", claim.getChoReference(), claim.getId());
        if (claimService.revertClaim(claim.getId()) != null) {
            LOG.info("Claim re-opened for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
            // Re-open automatically closed tasks on a re-opened claim
            if (!(ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getStatus())
                    || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getStatus())
                    || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getStatus()))) {  
                taskService.autoUndoCompleteTasksForClaim(claim.getId());
            }
            
            if (claim.getBreBand() == null) {
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
            }
                        
            if (claim.getInvoice() != null && claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType()) 
                    && claim.getInvoice().getInvoicedDays() > 30 && getWorkflowContext().getSecurityInfoProvider().getIsCHO()
                    && (
                            claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT) || claim.getStatus().equals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION)
                            || claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO) || claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS)
                            || claim.getStatus().equals(ClaimStatus.INVOICE_APPROVED_BY_BRE) || claim.getStatus().equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)
                            || claim.getStatus().equals(ClaimStatus.INVOICE_ESCALATED) || claim.getStatus().equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)
                            || claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_CH) || claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_ENG))
                    ) {
                setMessage("Claim has been re-opened and the penalty charge counter started " + claim.getInvoice().getInvoicedDays()
                        + " days ago, please confirm the correct penalty charges have been applied to the invoice.");
            }
            else {
                setMessage("Claim successfully re-opened.");
            }
        }
        else {
            LOG.warn("Failed to re-open claim for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        }
    }

    /*
     * As we do not wish to log this status change in the audit trail table, we will
     * override the 'afterProcess' method.
     */
    @Override
    protected void afterProcess(Claim claim) throws Exception {
//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getMBassador().post(event).now();
        });
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

}