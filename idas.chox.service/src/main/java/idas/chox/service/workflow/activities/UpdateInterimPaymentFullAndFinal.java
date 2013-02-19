package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.services.ClaimService;

public class UpdateInterimPaymentFullAndFinal extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateInterimPaymentFullAndFinal.class);
    private ClaimService claimService;

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (claim.getInvoice().getInterimPaymentMade().compareTo(BigDecimal.ZERO) <= 0) {
            LOG.error("Trying to update interim payment received full and final when there is no interim payment amount for this claim: {} by {}",
                    claim.getChoReference(), this.getWorkflowContext().getSecurityInfoProvider().getCurrentUser().getDisplayName());
            throw new Exception("No interim payment has been made on this claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {

        claim.getInvoice().setInterimPaymentReceivedFullAndFinal(true);
        claim.getInvoice().setInterimPaymentReceived(claim.getInvoice().getInterimPaymentMade());
        claim.getInvoice().setTotalToPay(claim.getInvoice().getInterimPaymentMade());
        claim.addComment(Comment.New(0, "An interim payment of £" + claim.getInvoice().getInterimPaymentMade().toString()
                + " has been received and accepted as a full and final payment."));

        if (claim.getStatus().equals(ClaimStatus.CLAIM_CLOSED)) {
            try {
                claimService.revertClaim(claim.getId());
            } catch (Exception ex) {
                LOG.debug(" Exception thrown while reverting claim in InterimPaymentFullAndFinal activity: ", ex);
            }
        }

        if (!claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)
                && !claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)) {
            if (!claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                logTransaction(claim, claim.getStatus(), ClaimStatus.AWAITING_INVOICE_PAYMENT, 0);
            }
            setCurrentStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        } else if (claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)) {
            // if the claim status is payment received then do not change
            // the claim status via paymentreceived chain activity.
            super.setChainActivity(null);
        }

    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}