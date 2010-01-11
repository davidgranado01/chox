package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.util.StringHelper;

public class ClaimReviewByEng extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">
    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

    public void setIsInvoiceReviewRequired(boolean isInvoiceReviewRequired) {
        this.isInvoiceReviewRequired = isInvoiceReviewRequired;
    }

    public void setIsQuantumDispute(boolean isQuantumDispute) {
        this.isQuantumDispute = isQuantumDispute;
    }

    public void setPercentageLiabilityAccepted(BigDecimal percentageLiabilityAccepted) {
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }
    // </editor-fold>

    @Override
    protected void beforeProcess(Claim claim) {
        claim.setIndemnityAmount(indemnityAmount);
        claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
        claim.setIsInvoiceReviewRequired(isInvoiceReviewRequired);
        claim.setIsQuantumDispute(isQuantumDispute);
        claim.setIsFnolReviewed(false);
    }

    @Override
    protected void doProcess(Claim claim) {

        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.New(1, engineerClaimReviewNotes));
        }

        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
    }
}
