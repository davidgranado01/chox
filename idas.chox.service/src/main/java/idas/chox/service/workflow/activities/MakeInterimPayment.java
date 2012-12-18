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
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((!securityInfoProvider.isInRoleOf("ROLE_INS") && !securityInfoProvider.getIsCHOXAdmin())
                || (securityInfoProvider.isInRoleOf("ROLE_CHO")
                    && (claim.getChorganisation().getId().compareTo(
                            securityInfoProvider.getCurrentUser().getChorganisation().getId())) != 0)) {
            throw new AccessDeniedException("Not in correct role to make an interim Payment.");
        }

    }

    @Override
    protected void doProcess(Claim claim) {
        Comment comment = null;
        if (newTotalInterimPayment != null && newTotalInterimPayment.compareTo(BigDecimal.ZERO) >= 0
                && additionalInterimPayment != null && additionalInterimPayment.compareTo(BigDecimal.ZERO) == 0) {

            if (newTotalInterimPayment.compareTo(BigDecimal.ZERO) == 0) {
                newTotalInterimPayment = null;
                comment = Comment.New(0, "The interim payment has been removed");
            } else if (claim.getInvoice().getInterimPaymentMade() != null) {
                comment = Comment.New(0, "The interim payment made has been modified to a new total of £" + newTotalInterimPayment.toString());
            } else {
                comment = Comment.New(0, "An interim payment of £" + newTotalInterimPayment.toString() + " has been made.");
            }

            claim.getInvoice().setInterimPaymentMade(newTotalInterimPayment);
        } else if (additionalInterimPayment != null && additionalInterimPayment.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal paymentSum = claim.getInvoice().getInterimPaymentMade().add(additionalInterimPayment);
            claim.getInvoice().setInterimPaymentMade(paymentSum);
            comment = Comment.New(0, "An additional interim payment of £" + additionalInterimPayment.toString() + " has been made."
                    + " The total interim payment amount is now £" + claim.getInvoice().getInterimPaymentMade());
        }
        claim.addComment(comment);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.INVOICE_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
    }

}