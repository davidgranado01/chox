package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.internal.util.StringHelper;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;

public class ClaimReviewByEng extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private String supportingLiabilityNotes;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">

    // </editor-fold>

    @Override
    protected void beforeProcess(Claim claim) {
        claim.setIndemnityAmount(getIndemnityAmount());
        claim.setIsInvoiceReviewRequired(isIsInvoiceReviewRequired());
        claim.setIsQuantumDispute(isIsQuantumDispute());
    }

    @Override
    protected void doProcess(Claim claim) {

        boolean disablePrivateNotes = getCurrentUser().getInsurer() != null ? 
                        getCurrentUser().getInsurer().isDisablePrivateNotes() : 
                            getCurrentUser().getChorganisation() != null ?
                        getCurrentUser().getChorganisation().isDisablePrivateNotes() :
                            claim.getInsurer().isDisablePrivateNotes();

        if (StringHelper.isNotEmpty(getEngineerClaimReviewNotes())) {
            if(!disablePrivateNotes){
                claim.addComment(Comment.newComment(1, getEngineerClaimReviewNotes(), true));
            }else{
                claim.addComment(Comment.newComment(0, getEngineerClaimReviewNotes(), true));
            }
        }

        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.newComment(0, "Supporting Liability Notes: " + supportingLiabilityNotes, true));
        }
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
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

    /**
     * @return the supportingLiabilityNotes
     */
    public String getSupportingLiabilityNotes() {
        return supportingLiabilityNotes;
    }

    /**
     * @param supportingLiabilityNotes the supportingLiabilityNotes to set
     */
    public void setSupportingLiabilityNotes(String supportingLiabilityNotes) {
        this.supportingLiabilityNotes = supportingLiabilityNotes;
    }
}
