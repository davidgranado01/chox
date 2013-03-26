package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;

public class UpdateInterimPaymentReceived extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateInterimPaymentFullAndFinal.class);
    
    private BigDecimal partialInterimPayment;
    
    @Override
    protected void doProcess(Claim claim) {
		if (claim.getInvoice().getInterimPaymentMade().compareTo(BigDecimal.ZERO) > 0) {
			claim.addComment(Comment.newComment(0, "An interim payment of £" + partialInterimPayment.toString() + " has been received."));
			claim.getInvoice().setInterimPaymentReceived(partialInterimPayment);
		} else {
			LOG.error("Trying to update interim payment received when there is no interim payment amount for this claim: {} by {}",
					claim.getChoReference(), this.getWorkflowContext()
							.getSecurityInfoProvider().getCurrentUser()
							.getDisplayName());
		}
    }

	public BigDecimal getPartialInterimPayment() {
		return partialInterimPayment;
	}

	public void setPartialInterimPayment(BigDecimal partialInterimPayment) {
		this.partialInterimPayment = partialInterimPayment;
	}
}