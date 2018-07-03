package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;

public class ClaimReferToFnol extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimReferToEng.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    // FROM CLAIM UNASSIGNED
    private int oasWorkgroupId;
    private int claimOwnerId;
    private WebUser claimOwner;
    private Workgroup workgroup;
    // FROM CLAIM ROUTED
    private String claimNumber;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private String supportingLiabilityNotes;
    private Integer reasonOfRejectionId;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    private boolean liabilityUpdated = false;
    private boolean claimRouted = false;
    private boolean ownerAssigned = false;
    private boolean claimNumberUpdated = false;
    private String indemnityStance;

    public boolean isLiabilityUpdated() {
        return liabilityUpdated;
    }

    public boolean isClaimRouted() {
        return claimRouted;
    }

    public boolean isOwnerAssigned() {
        return ownerAssigned;
    }

    public boolean isClaimNumberUpdated() {
        return claimNumberUpdated;
    }

    public String getIndemnityStance() {
        return indemnityStance;
    }

    public void setIndemnityStance(String indemnityStance) {
        this.indemnityStance = indemnityStance;
    }

    public WebUser getClaimOwner() {
        return claimOwner;
    }

    public Workgroup getWorkgroup() {
        return workgroup;
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

    public boolean isIsQuantumDispute() {
        return isQuantumDispute;
    }

    public boolean isIsInvoiceReviewRequired() {
        return isInvoiceReviewRequired;
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

    public void setLiabilityAgreedDate(Date liabilityAgreedDate) {
        this.liabilityAgreedDate = liabilityAgreedDate;
    }

    public void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }

    public void setPercentageLiabilityCho(BigDecimal percentageLiabilityCho) {
        this.percentageLiabilityCho = percentageLiabilityCho;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">
    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

    public void setClaimNumber(String claimNumber) {
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claimNumber = claimNumber.trim();
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

    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

    public int getOasWorkgroupId() {
        return oasWorkgroupId;
    }

    public void setOasWorkgroupId(int oasWorkgroupId) {
        this.oasWorkgroupId = oasWorkgroupId;
    }

    public int getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(int claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }
    // </editor-fold>

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {

            if (oasWorkgroupId > 0) {
                workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
                if (workgroup == null) {
                    throw new Exception("Invalid workgroup id");
                }
                // Check workgroup belongs to the Insurer
                if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                    throw new AccessDeniedException("Workgroup does not belong to Insurer");
                }
            }

            if (claimOwnerId > 0) {
                claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
                // Check user belongs to the Insurer
                if (claimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
                }

            }
        }
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

        if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {

            if (workgroup != null && (claim.getWorkgroup() == null || claim.getWorkgroup().getId().compareTo(workgroup.getId())!=0)) {
                claim.setWorkgroup(workgroup);
                claimRouted = true;
            }
            if (claimOwner != null && (claim.getClaimOwner()== null || claim.getClaimOwner().getId().compareTo(claimOwner.getId())!=0)) {
                claim.setClaimOwner(claimOwner);
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                getDataService().save(claim);
                getDataService().flush();
                logTransaction(claim);
                setCurrentStatus(claim.getStatus());
                ownerAssigned = true;
            }

        } else {
            liabilityUpdated = claimService.setLiability(claim, liabilityStatus);

            if (!claim.getClaimNumber().equals(claimNumber)) {
                claim.setClaimNumber(claimNumber);
                claimNumberUpdated = true;
            }
            claim.setIndemnityAmount(indemnityAmount);
            claimService.updateLiabilityPercentages(claim, percentageLiabilityAccepted, percentageLiabilityCho);
            claim.setIsInvoiceReviewRequired(isInvoiceReviewRequired);
            claim.setIsQuantumDispute(isQuantumDispute);
            claim.setReasonOfRejection(getReasonOfRejection());
            claim.setLiabilityAgreedDate(liabilityAgreedDate);


        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.newComment(0, engineerClaimReviewNotes, true));
        }
        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.newComment(0, new StringBuilder().append("Supporting Liability Notes: ").append(supportingLiabilityNotes).toString(), true));
        }
        if (indemnityStance != null && !indemnityStance.isEmpty() && (claim.getIndemnityStance() == null || !claim.getIndemnityStance().equals(indemnityStance))) {
            claim.setIndemnityStance(indemnityStance);
            // Add Note
            claim.addComment(Comment.newComment(0, "Insurer Indemnity Stance: " + indemnityStance));
        }

        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setIsFnolReviewed(false);
    }

    protected ReasonOfRejection getReasonOfRejection() {
        ReasonOfRejection reasonOfRejection = null;
        if (reasonOfRejectionId != null && reasonOfRejectionId > 0) {
            reasonOfRejection = (ReasonOfRejection) this.getDataService().get(ReasonOfRejection.class, reasonOfRejectionId);
        }
        return reasonOfRejection;
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
