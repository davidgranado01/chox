package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;

public class MakeInterimPayment extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(MakeInterimPayment.class);
    
    private BigDecimal newTotalInterimPayment;
    private BigDecimal additionalInterimPayment;

    public BigDecimal getAdditionalInterimPayment() {
        return additionalInterimPayment;
    }

    public void setAdditionalInterimPayment(BigDecimal additionalInterimPayment) {
        this.additionalInterimPayment = additionalInterimPayment;
    }

    public BigDecimal getNewTotalInterimPayment() {
        return newTotalInterimPayment;
    }

    public void setNewTotalInterimPayment(BigDecimal newTotalInterimPayment) {
        this.newTotalInterimPayment = newTotalInterimPayment;
    }


    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void doProcess(Claim claim) {
        Comment comment = null;
        if (newTotalInterimPayment != null && newTotalInterimPayment.compareTo(BigDecimal.ZERO) >= 0
                && additionalInterimPayment != null && additionalInterimPayment.compareTo(BigDecimal.ZERO) == 0) {

            if (newTotalInterimPayment.compareTo(BigDecimal.ZERO) == 0) {
                newTotalInterimPayment = null;
                comment = Comment.newComment(0, "The interim payment has been removed");
            } else if (claim.getInvoice().getInterimPaymentMade() != null) {
                comment = Comment.newComment(0, "The interim payment made has been modified to a new total of £" + newTotalInterimPayment.toString());
            } else {
                comment = Comment.newComment(0, "An interim payment of £" + newTotalInterimPayment.toString() + " has been made.");
            }

            claim.getInvoice().setInterimPaymentMade(newTotalInterimPayment);
        } else if (additionalInterimPayment != null && additionalInterimPayment.compareTo(BigDecimal.ZERO) > 0) {
            newTotalInterimPayment = claim.getInvoice().getInterimPaymentMade().add(additionalInterimPayment);
            claim.getInvoice().setInterimPaymentMade(newTotalInterimPayment);
            comment = Comment.newComment(0, "An additional interim payment of £" + additionalInterimPayment.toString() + " has been made."
                    + " The total interim payment amount is now £" + claim.getInvoice().getInterimPaymentMade());
        }
        claim.addComment(comment);
    }

}