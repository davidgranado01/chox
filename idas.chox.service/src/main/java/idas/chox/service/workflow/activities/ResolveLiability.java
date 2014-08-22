package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;


public class ResolveLiability extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(ResolveLiability.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    private BigDecimal percentageLiabilityAccepted;
    private Integer reasonOfRejectionId;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    private String engineerClaimReviewNotes;

    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    // </editor-fold>
    public String getEngineerClaimReviewNotes() {
        return engineerClaimReviewNotes;
    }
    
    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (liabilityStatus != null && liabilityStatus.equals(LiabilityStatus.LIABILITY_ACCEPTED)
                && ((new BigDecimal(100.0)).compareTo(percentageLiabilityAccepted) != 0
                || BigDecimal.ZERO.compareTo(percentageLiabilityCho) != 0)) {
            LOG.error("Full Liability accepted but % not correct: ins={}, cho={}", percentageLiabilityAccepted, percentageLiabilityCho);
            throw new AccessDeniedException("Liability % not correct");
        }
        else if (liabilityStatus != null && liabilityStatus.equals(LiabilityStatus.LIABILITY_SPLIT)
                && (percentageLiabilityCho.add(percentageLiabilityAccepted).compareTo(new BigDecimal(100.0)) > 0
                    || percentageLiabilityCho.add(percentageLiabilityAccepted).compareTo(BigDecimal.ZERO) <= 0)) {
            LOG.error("Liability total must be > 0 and <= 100%: ins={}, cho={}", percentageLiabilityAccepted, percentageLiabilityCho);
            throw new AccessDeniedException("Total liability is > 100% or <= 0%");
        }
    }


    @Override
    protected void beforeProcess(Claim claim) throws Exception {
        LOG.debug("liabilityStatus " + liabilityStatus);
        LOG.debug("claim liab " + claim.getLiabilityStatus());
        claimService.setLiability(claim, liabilityStatus);
        claim.setLiabilityPercentages(percentageLiabilityAccepted, percentageLiabilityCho);
        claim.setLiabilityAgreedDate(liabilityAgreedDate);        
        getWorkflowContext().getClaimService().updateLiabilityPayment(claim);
    }


    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("claim status " + claim.getLiabilityStatus());
        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.newComment(0, "Supporting Liability Notes: " + engineerClaimReviewNotes));
        }
        if ( !ClaimType.isInsurerVsInsurer(claim.getClaimType())
                && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType()) &&
            ( claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
             || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
             || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
             || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED)) {
            claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        } else {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        }
    }

    /**
     * @return the claimNumber
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * @param claimNumber the claimNumber to set
     */
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
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
     * @return the reasonOfRejectionId
     */
    public Integer getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    /**
     * @param reasonOfRejectionId the reasonOfRejectionId to set
     */
    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
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
