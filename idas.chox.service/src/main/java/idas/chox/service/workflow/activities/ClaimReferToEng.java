package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.service.notifications.LiabilityStatusUpdatedNotification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.util.StringHelper;

public class ClaimReferToEng extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private int reasonOfRejectionId;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;



    public void setClaimNumber(String claimNumber) {
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claimNumber.trim();
        }
        this.claimNumber = claimNumber;
    }


    @Override
    protected void beforeProcess(Claim claim) {
        if ( claim.getLiabilityStatus()==null ||! claim.getLiabilityStatus().equals(liabilityStatus) ){
                String note;
                if ( claim.getLiabilityStatus()==null ){
                    note = "Liability status changed to '" + liabilityStatus+"'";
                }else{
                    note = "Liability status changed from '" + claim.getLiabilityStatus() + "' to '" + liabilityStatus+"'";
                }
                

                Comment comment = Comment.New(0, note);
                comment.setClaim(claim);
                claim.getComments().add(comment);
                claim.AddNotification(new LiabilityStatusUpdatedNotification(claim));        
        }
        claim.setClaimNumber(claimNumber);
        claim.setIndemnityAmount(indemnityAmount);
        claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
        claim.setIsInvoiceReviewRequired(isIsInvoiceReviewRequired());
        claim.setIsQuantumDispute(isQuantumDispute);
        claim.setReasonOfRejection(getReasonOfRejection());
        claim.setIsFnolReviewed(false);
        claim.setPercentageLiabilityCho(percentageLiabilityCho);
        claim.setLiabilityAgreedDate(liabilityAgreedDate);
        claim.setLiabilityStatus(liabilityStatus);
        
    }

    @Override
    protected void doProcess(Claim claim) {

        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.New(1, engineerClaimReviewNotes));
        }

        claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
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
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
    }

    /**
     * @param indemnityAmount the indemnityAmount to set
     */
    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

    /**
     * @param percentageLiabilityAccepted the percentageLiabilityAccepted to set
     */
    public void setPercentageLiabilityAccepted(BigDecimal percentageLiabilityAccepted) {
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }

    /**
     * @return the isInvoiceReviewRequired
     */
    public boolean isIsInvoiceReviewRequired() {
        return isInvoiceReviewRequired;
    }

    /**
     * @param isInvoiceReviewRequired the isInvoiceReviewRequired to set
     */
    public void setIsInvoiceReviewRequired(boolean isInvoiceReviewRequired) {
        this.isInvoiceReviewRequired = isInvoiceReviewRequired;
    }

    /**
     * @param engineerClaimReviewNotes the engineerClaimReviewNotes to set
     */
    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    /**
     * @param reasonOfRejectionId the reasonOfRejectionId to set
     */
    public void setReasonOfRejectionId(int reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

    /**
     * @param percentageLiabilityCho the percentageLiabilityCho to set
     */
    public void setPercentageLiabilityCho(BigDecimal percentageLiabilityCho) {
        this.percentageLiabilityCho = percentageLiabilityCho;
    }

    /**
     * @param liabilityAgreedDate the liabilityAgreedDate to set
     */
    public void setLiabilityAgreedDate(Date liabilityAgreedDate) {
        this.liabilityAgreedDate = liabilityAgreedDate;
    }

    /**
     * @param liabilityStatus the liabilityStatus to set
     */
    public void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }


}
