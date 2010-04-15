package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.util.StringHelper;

public class ClaimRejection extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRejection.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private int reasonOfRejectionId;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">
    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

    public void setClaimNumber(String claimNumber) {
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claimNumber.trim();
        }
        this.claimNumber = claimNumber;
    }

    public void setPercentageLiabilityAccepted(BigDecimal percentageLiabilityAccepted) {
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }

    public void setIsQuantumDispute(boolean isQuantumDispute) {
        this.isQuantumDispute = isQuantumDispute;
    }

    public void setIsInvoiceReviewRequired(boolean isInvoiceReviewRequired) {
        this.isInvoiceReviewRequired = isInvoiceReviewRequired;
    }

    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    public void setReasonOfRejectionId(int reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }
    // </editor-fold>

    @Override
    protected void beforeProcess(Claim claim) {
        LOG.debug("beforeProcess start claim version = {}", claim.getVersion());
        if (indemnityAmount != null)
            claim.setIndemnityAmount(indemnityAmount);
        if (percentageLiabilityAccepted != null)
            claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
        claim.setIsInvoiceReviewRequired(isInvoiceReviewRequired);
        claim.setIsQuantumDispute(isQuantumDispute);
        claim.setReasonOfRejection(getReasonOfRejection());
        claim.setIsFnolReviewed(false);
        if (claimNumber != null)
            claim.setClaimNumber(claimNumber);
        LOG.debug("beforeProcess end claim version = {}", claim.getVersion());
    }

    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("doProcess begin claim version = {}", claim.getVersion());

        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.New(1, engineerClaimReviewNotes));
        }

        if (getReasonOfRejection() != null) {
            claim.addComment(Comment.New(0, "Reason For Rejection: " + getReasonOfRejection().getName()));
        }
        
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        LOG.debug("doProcess end claim version = {}", claim.getVersion());
    }

    protected ReasonOfRejection getReasonOfRejection() {
        ReasonOfRejection reasonOfRejection = null;
        if (reasonOfRejectionId > 0) {
            reasonOfRejection = (ReasonOfRejection) this.getDataService().get(ReasonOfRejection.class, reasonOfRejectionId);
        }
        return reasonOfRejection;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
    }
}
