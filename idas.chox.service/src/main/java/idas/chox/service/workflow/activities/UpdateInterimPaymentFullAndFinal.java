package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;

public class UpdateInterimPaymentFullAndFinal extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateInterimPaymentFullAndFinal.class);
    private ClaimService claimService;

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin())
                || (securityInfoProvider.isInRoleOf("ROLE_CHO")
                && (claim.getChorganisation().getId().compareTo(
                        securityInfoProvider.getCurrentUser().getChorganisation().getId())) != 0)) {
            throw new AccessDeniedException("Not in correct role to update interim Payment full and final.");
        }

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
        expectingStatuses.add(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}