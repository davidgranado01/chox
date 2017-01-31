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
import idas.chox.core.util.DateHelper;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class PaymentNotReceived extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentNotReceived.class);
    private BigDecimal amountReceived = null;
    private BreBandService breBandService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    @Override
    protected void doProcess(Claim claim) {

        LOG.debug("Reverting status for claim: {} (id={})", claim.getChoReference(), claim.getId());
        String originalStatus = claim.getStatus();
        Date originalStatusModifiedDate = claim.getStatusModifiedDate();

        if (claimService.revertClaim(claim.getId()) != null) {
            LOG.info("Claim status reverted for claim with id={} (Supplier reference '{}') : {} -> {}",
                    new Object[]{claim.getId(), claim.getChoReference(), originalStatus, claim.getStatus()});
            if (claim.getInsurer().isPaymentDisputesEnable()) {
                claim.setPaymentDispute(true);
            }

            // Insurer has reverted back from InvoicePaymentLogged - add a note
            Comment comment;
            if (amountReceived == null) {
                comment = Comment.newComment(0, "The claim was marked as 'Invoice Payment Logged' on "
                        + DateHelper.getLocalDateTimeFormat().format(originalStatusModifiedDate)
                        + ", however the CHO has not received the payment. Please check the payment details in your claim system and if available add the cheque/BACS reference, date cashed, amount raised and reference the payment was sent under.");
            } else {
                comment = Comment.newComment(0, "The claim was marked as 'Invoice Payment Logged' on "
                        + DateHelper.getLocalDateTimeFormat().format(originalStatusModifiedDate)
                        + ", however the CHO has not received the full amount and has marked the payment as an Interim Payment of £"
                        + amountReceived + " as there is an amount outstanding. Please check "
                        + "the payment details in your claim system and mark the claim as 'Invoice Payment Logged' when the outstanding amount has been paid.");
            }
            claim.addComment(comment);

            if (claim.getBreBand() == null) {
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
            }

            if (claim.getInvoice() != null && claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType())
                    && claim.getInvoice().getInvoicedDays() > 30 && getWorkflowContext().getSecurityInfoProvider().getIsCHO()
                    && ((originalStatus.equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT))
                    || (originalStatus.equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED) && claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO))
                    || (originalStatus.equals(ClaimStatus.CLAIM_CLOSED) && (claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT) || claim.getStatus().equals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION)
                    || claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO) || claim.getStatus().equals(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS)
                    || claim.getStatus().equals(ClaimStatus.INVOICE_APPROVED_BY_BRE) || claim.getStatus().equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)
                    || claim.getStatus().equals(ClaimStatus.INVOICE_ESCALATED) || claim.getStatus().equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)
                    || claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_CH) || claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_ENG))))) {
                setMessage("Claim reverted to status '" + claim.getStatus()
                        + "' and the penalty charge counter started " + claim.getInvoice().getInvoicedDays()
                        + " days ago, please confirm the correct penalty charges have been applied to the invoice.");
            } else {
                setMessage("Claim successfully reverted back from '" + originalStatus + "' to '" + claim.getStatus() + "'.");
            }
        } else {
            LOG.warn("Failed to revert claim status for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        }
    }


    /*
     * We'll overide the afterProcess as we do not want to log a state change for a revert operation.
     * The claim is also saved in the service, so we do not need to do this either.
     */
    @Override
    protected void afterProcess(Claim claim) throws Exception {
//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

}
