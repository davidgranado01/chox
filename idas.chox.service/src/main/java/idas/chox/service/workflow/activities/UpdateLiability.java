package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;

public class UpdateLiability extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateLiability.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    private BigDecimal percentageLiabilityAccepted;
    private BigDecimal percentageLiabilityCho;
    private BigDecimal indemnityAmount;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    private String claimReviewNotes;
    private String indemnityStance;
    // </editor-fold>


    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
//        if ((liabilityStatus.equals(LiabilityStatus.LIABILITY_ACCEPTED) || liabilityStatus.equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE))
        if (liabilityStatus != null && liabilityStatus.equals(LiabilityStatus.LIABILITY_ACCEPTED)
                && (percentageLiabilityAccepted.compareTo(new BigDecimal(100.0)) != 0
                || percentageLiabilityCho.compareTo(BigDecimal.ZERO) != 0)) {
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
        if (StringHelper.isNotEmpty(claimReviewNotes)) {
            claim.addComment(Comment.newComment(0, "Supporting Liability Notes: " + claimReviewNotes));
        }
        claim.setLiabilityPercentages(percentageLiabilityAccepted, percentageLiabilityCho);
        claim.setLiabilityAgreedDate(liabilityAgreedDate);
        claimService.updateLiabilityPayment(claim);

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("claim status='{}'", claim.getLiabilityStatus());
        if ( !ClaimType.isInsurerVsInsurer(claim.getClaimType())
//                && !ClaimType.isSubscriber(claim.getClaimType())
//                && !ClaimType.isFixedFee(claim.getClaimType())
                && ( claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
                        || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
                        || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
                        || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED)
                && claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
            claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        }
        if (indemnityStance != null && !indemnityStance.isEmpty() && (claim.getIndemnityStance() == null || !claim.getIndemnityStance().equals(indemnityStance))) {
            claim.setIndemnityStance(indemnityStance);
            // Add Note
            claim.addComment(Comment.newComment(0, "Insurer Indemnity Stance: " + indemnityStance));
        } else if ((indemnityStance==null || indemnityStance.isEmpty()) && claim.getIndemnityStance() != null) {
            claim.setIndemnityStance(null);
            claim.addComment(Comment.newComment(0, "Insurer Indemnity Stance has been removed"));
        }
        claim.setIndemnityAmount(indemnityAmount);
    }

    public String getIndemnityStance() {
        return indemnityStance;
    }

    public void setIndemnityStance(String indemnityStance) {
        this.indemnityStance = indemnityStance;
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
     * 
     * @return
     */
    public String getClaimReviewNotes() {
		return claimReviewNotes;
	}

    public void setClaimReviewNotes(String claimReviewNotes) {
	this.claimReviewNotes = claimReviewNotes;
    }

    public BigDecimal getIndemnityAmount() {
        return indemnityAmount;
    }

    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

}
