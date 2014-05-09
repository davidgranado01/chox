package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.PenaltyChargeService;
import idas.chox.core.services.TaskService;
import idas.chox.core.util.DateHelper;

public class RevertClaim extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(RevertClaim.class);
    private BigDecimal amountReceived = null;
    private TaskService taskService;
    private BreBandService breBandService;
    private PenaltyChargeService penaltyChargeService;

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setPenaltyChargeService(PenaltyChargeService penaltyChargeService) {
        this.penaltyChargeService = penaltyChargeService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }
    
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }


    @Override
    protected void doProcess(Claim claim) {
        boolean reOpenTasks = false;
        boolean reCloseTasks = false;

        if (ClaimStatus.CLAIM_CLOSED.equals(claim.getStatus())
                || ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getStatus())
                || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getStatus())
                || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getStatus())) {
            reOpenTasks = true;
        } // indicates reverting from a closed to an open state
        if (ClaimStatus.CLAIM_CLOSED.equals(claim.getPreviousStatus())
                || ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getPreviousStatus())
                || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getPreviousStatus())
                || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getPreviousStatus())) {
            reCloseTasks = true;
        } // Indicates reverting to a closed state
        LOG.debug("Reverting status for claim: {} (id={})", claim.getChoReference(), claim.getId());
        String originalStatus = claim.getStatus();
        Date originalStatusModifiedDate = claim.getStatusModifiedDate();
        
        if (claimService.revertClaim(claim.getId()) != null) {
            LOG.info("Claim status reverted for claim with id={} (Supplier reference '{}') : {} -> {}",
                    new Object[] {claim.getId(), claim.getChoReference(), originalStatus, claim.getStatus()});
            if (reOpenTasks && !reCloseTasks) {
                taskService.autoUndoCompleteTasksForClaim(claim.getId());
            }
            else if (reCloseTasks) {
                taskService.autoCompleteTasksForClaim(claim.getId());
            }
            // Make sure we have a BRE Band
            if (ClaimStatus.AWAITING_INVOICE_PAYMENT.equals(claim.getStatus()) && getCurrentUser().isCHO()) {
                // CHO has reverted back from InvoicePaymentLogged - add a note
                Comment comment;
                if (amountReceived == null) {
                    comment = Comment.newComment(0, "The claim was marked as 'Invoice Payment Logged' on "
                            + DateHelper.getLocalDateTimeFormat().format(originalStatusModifiedDate)
                            + ", however the CHO has not received the payment. Please check the payment details in your claim system and if available add the cheque/BACS reference, date cashed, amount raised and reference the payment was sent under.");
                }
                else {
                    comment = Comment.newComment(0, "The claim was marked as 'Invoice Payment Logged' on "
                            + DateHelper.getLocalDateTimeFormat().format(originalStatusModifiedDate)
                            + ", however the CHO has not received the full amount and has marked the payment as an Interim Payment of £"
                            + amountReceived + " as there is an amount outstanding. Please check " 
                            + "the payment details in your claim system and mark the claim as 'Invoice Payment Logged' when the outstanding amount has been paid.");
                }
                claim.addComment(comment);
            } else if (ClaimStatus.AWAITING_INVOICE_PAYMENT.equals(claim.getStatus())) { // and we are an Insurer or CHOX Admin
                // we need to remove the note added when the claim moved to INVOICE_PAYMENT_LOGGED
                for (Comment comment : claim.getComments()) {
                    if (!comment.isReverted() && (comment.getComment().startsWith("A payment amount of") || comment.getComment().startsWith("A full payment amount of"))
                            && comment.getComment().contains("has been made")) {
                        if (comment.getCreatedDate().getTime() - 500 < originalStatusModifiedDate.getTime()
                                && originalStatusModifiedDate.getTime() < comment.getCreatedDate().getTime() + 500) {
                            LOG.debug("Marking comment with id={} as deleted: '{}'", comment.getId(), comment.getComment());
                            comment.setReverted(true);
                          break;
                        }
                    }
                }
            }

            if (claim.getBreBand() == null) {
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
            }

            if (claim.getInvoice() != null && claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType())
                    && claim.getInvoice().getInvoicedDays() > penaltyChargeService.getFirstPenaltyBand(claim) && getWorkflowContext().getSecurityInfoProvider().getIsCHO()
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
            else {
                setMessage("Claim successfully reverted back from '" + originalStatus + "' to '" + claim.getStatus() + "'.");
            }
        }
        else {
            LOG.warn("Failed to revert claim status for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        }
    }


    /*
     * We'll overide the afterProcess as we do not want to log a state change for a revert operation.
     * The claim is also saved in the service, so we do not need to do this either.
     */
    @Override
    protected void afterProcess(Claim claim) throws Exception {
        activityEventGenerator.generate(claim, this);
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }


}