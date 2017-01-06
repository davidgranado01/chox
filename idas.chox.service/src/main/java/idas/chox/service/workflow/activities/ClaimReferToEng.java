package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ReasonOfRejection;

public class ClaimReferToEng extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimReferToEng.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    private String indemnityStance;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private String supportingLiabilityNotes;
    private Integer reasonOfRejectionId;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    protected boolean liabilityUpdated = false;
    protected boolean claimNumberUpdated = false;

    public String getIndemnityStance() {
        return indemnityStance;
    }

    public void setIndemnityStance(String indemnityStance) {
        this.indemnityStance = indemnityStance;
    }

    public void setClaimNumber(String claimNumber) {
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claimNumber = claimNumber.trim();
        }
        this.claimNumber = claimNumber;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public BigDecimal getIndemnityAmount() {
        return indemnityAmount;
    }

    public BigDecimal getPercentageLiabilityAccepted() {
        return percentageLiabilityAccepted;
    }

    public String getEngineerClaimReviewNotes() {
        return engineerClaimReviewNotes;
    }

    public Integer getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    public BigDecimal getPercentageLiabilityCho() {
        return percentageLiabilityCho;
    }

    public Date getLiabilityAgreedDate() {
        return liabilityAgreedDate;
    }

    public LiabilityStatus getLiabilityStatus() {
        return liabilityStatus;
    }

    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (liabilityStatus != null && liabilityStatus.equals(LiabilityStatus.LIABILITY_ACCEPTED)
                && (percentageLiabilityAccepted.compareTo(new BigDecimal(100.0)) != 0
                || percentageLiabilityCho.compareTo(BigDecimal.ZERO) != 0)) {
            LOG.error("Full Liability accepted but % not correct: ins={}, cho={}", percentageLiabilityAccepted, percentageLiabilityCho);
            throw new AccessDeniedException("Liability % not correct");
        } else if (liabilityStatus != null && liabilityStatus.equals(LiabilityStatus.LIABILITY_SPLIT)
                && (percentageLiabilityCho.add(percentageLiabilityAccepted).compareTo(new BigDecimal(100.0)) > 0
                || percentageLiabilityCho.add(percentageLiabilityAccepted).compareTo(BigDecimal.ZERO) <= 0)) {
            LOG.error("Liability total must be > 0 and <= 100%: ins={}, cho={}", percentageLiabilityAccepted, percentageLiabilityCho);
            throw new AccessDeniedException("Total liability is > 100% or <= 0%");
        }
    }

    @Override
    protected void beforeProcess(Claim claim) {
        liabilityUpdated = claimService.setLiability(claim, liabilityStatus);

        if (!claim.getClaimNumber().equals(claimNumber)) {
            claim.setClaimNumber(claimNumber);
            claimNumberUpdated = true;
        }
        claim.setIndemnityAmount(indemnityAmount);
        claim.setLiabilityPercentages(percentageLiabilityAccepted, percentageLiabilityCho);
        claim.setIsInvoiceReviewRequired(isIsInvoiceReviewRequired());
        claim.setReasonOfRejection(getReasonOfRejection());
        claim.setLiabilityAgreedDate(liabilityAgreedDate);
    }

    @Override
    protected void doProcess(Claim claim) {

        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.newComment(0, engineerClaimReviewNotes));
        }
        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.newComment(0, "Supporting Liability Notes: " + supportingLiabilityNotes));
        }
        if (indemnityStance != null && !indemnityStance.isEmpty() && (claim.getIndemnityStance() == null || !claim.getIndemnityStance().equals(indemnityStance))) {
            claim.setIndemnityStance(indemnityStance);
            // Add Note
            claim.addComment(Comment.newComment(0, "Insurer Indemnity Stance: " + indemnityStance));
        }

        claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
    }

    protected ReasonOfRejection getReasonOfRejection() {
        ReasonOfRejection reasonOfRejection = null;
        if (reasonOfRejectionId != null && reasonOfRejectionId > 0) {
            reasonOfRejection = (ReasonOfRejection) this.getDataService().get(ReasonOfRejection.class, reasonOfRejectionId);
        }
        return reasonOfRejection;
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
    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
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
