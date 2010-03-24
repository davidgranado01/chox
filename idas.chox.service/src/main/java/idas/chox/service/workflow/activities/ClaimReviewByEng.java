package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.util.StringHelper;

public class ClaimReviewByEng extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">

    // </editor-fold>

    @Override
    protected void beforeProcess(Claim claim) {
        /*
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
        }
         
        claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
        claim.setLiabilityAgreedDate(liabilityAgreedDate);
        claim.setPercentageLiabilityCho(percentageLiabilityCho);
        claim.setLiabilityStatus(liabilityStatus);
         */
        claim.setIndemnityAmount(getIndemnityAmount());
        claim.setIsInvoiceReviewRequired(isIsInvoiceReviewRequired());
        claim.setIsQuantumDispute(isIsQuantumDispute());
        claim.setIsFnolReviewed(false);

    }

    @Override
    protected void doProcess(Claim claim) {

        if (StringHelper.isNotEmpty(getEngineerClaimReviewNotes())) {
            claim.addComment(Comment.New(1, getEngineerClaimReviewNotes()));
        }

        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
    }

    /**
     * @return the indemnityAmount
     */
    public BigDecimal getIndemnityAmount() {
        return indemnityAmount;
    }

    /**
     * @param indemnityAmount the indemnityAmount to set
     */
    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

    /**
     * @return the percentageLiabilityAccepted
     */
    public BigDecimal getPercentageLiabilityAccepted() {
        return percentageLiabilityAccepted;
    }

    /**
     * @param percentageLiabilityAccepted the percentageLiabilityAccepted to set
     */
    public void setPercentageLiabilityAccepted(BigDecimal percentageLiabilityAccepted) {
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }

    /**
     * @return the isQuantumDispute
     */
    public boolean isIsQuantumDispute() {
        return isQuantumDispute;
    }

    /**
     * @param isQuantumDispute the isQuantumDispute to set
     */
    public void setIsQuantumDispute(boolean isQuantumDispute) {
        this.isQuantumDispute = isQuantumDispute;
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
     * @return the engineerClaimReviewNotes
     */
    public String getEngineerClaimReviewNotes() {
        return engineerClaimReviewNotes;
    }

    /**
     * @param engineerClaimReviewNotes the engineerClaimReviewNotes to set
     */
    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    /**
     * @return the percentageLiabilityCho
     */
    public BigDecimal getPercentageLiabilityCho() {
        return percentageLiabilityCho;
    }

    /**
     * @param percentageLiabilityCho the percentageLiabilityCho to set
     */
    public void setPercentageLiabilityCho(BigDecimal percentageLiabilityCho) {
        this.percentageLiabilityCho = percentageLiabilityCho;
    }

    /**
     * @return the liabilityAgreedDate
     */
    public Date getLiabilityAgreedDate() {
        return liabilityAgreedDate;
    }

    /**
     * @param liabilityAgreedDate the liabilityAgreedDate to set
     */
    public void setLiabilityAgreedDate(Date liabilityAgreedDate) {
        this.liabilityAgreedDate = liabilityAgreedDate;
    }

    /**
     * @return the liabilityStatus
     */
    public LiabilityStatus getLiabilityStatus() {
        return liabilityStatus;
    }

    /**
     * @param liabilityStatus the liabilityStatus to set
     */
    public void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }
}
