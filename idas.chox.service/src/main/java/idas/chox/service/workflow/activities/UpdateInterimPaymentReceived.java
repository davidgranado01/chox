package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;

public class UpdateInterimPaymentReceived extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateInterimPaymentFullAndFinal.class);
    
    private BigDecimal partialInterimPayment;
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin())
                || (securityInfoProvider.isInRoleOf("ROLE_CHO") && (claim.getChorganisation().getId().compareTo(securityInfoProvider.getCurrentUser().getChorganisation().getId())) != 0)) {
            throw new AccessDeniedException("Not in correct role to update interim Payment.");
        }

    }

    @Override
    protected void doProcess(Claim claim) {
		if (claim.getInvoice().getInterimPaymentMade().compareTo(BigDecimal.ZERO) > 0) {
			claim.addComment(Comment.New(0, "An interim payment of £" + partialInterimPayment.toString() + " has been received."));
			claim.getInvoice().setInterimPaymentReceived(partialInterimPayment);
		} else {
			LOG.error("Trying to update interim payment received when there is no interim payment amount for this claim: {} by {}",
					claim.getChoReference(), this.getWorkflowContext()
							.getSecurityInfoProvider().getCurrentUser()
							.getDisplayName());
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
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.INVOICE_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_APPROVED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_REJECTED);
        expectingStatuses.add(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
    }

	public BigDecimal getPartialInterimPayment() {
		return partialInterimPayment;
	}

	public void setPartialInterimPayment(BigDecimal partialInterimPayment) {
		this.partialInterimPayment = partialInterimPayment;
	}
}