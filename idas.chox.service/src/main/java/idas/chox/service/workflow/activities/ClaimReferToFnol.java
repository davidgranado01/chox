package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.service.notifications.LiabilityStatusUpdatedNotification;


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
    private int reasonOfRejectionId;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;

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

            if (oasWorkgroupId <= 0) {
                throw new Exception("Invalid workgroup id");
            } else {
                workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
                if (workgroup == null) {
                    throw new Exception("Invalid workgroup id");
                }
            }

            if (claimOwnerId >= 0) {
                claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            }
        }
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
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_CH") && !securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                    && !securityInfoProvider.isInRoleOf("ROLE_INS_COM") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to refer claim to FNOL.");
        }
    }

    @Override
    protected void beforeProcess(Claim claim) {

        if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {

            claim.setClaimOwner(claimOwner);
            claim.setWorkgroup(workgroup);

        } else {
            if (claim.getLiabilityStatus() == null || !claim.getLiabilityStatus().equals(liabilityStatus)) {

                String note;
                if ( claim.getLiabilityStatus()==null ){
                    note = "Liability status changed to '" + liabilityStatus+"'";
                }else{
                    note = "Liability status changed from '" + claim.getLiabilityStatus() + "' to '" + liabilityStatus+"'";
                }
                claim.setLiabilityStatus(liabilityStatus);
                Comment comment = Comment.New(0, note);
                comment.setClaim(claim);
                claim.getComments().add(comment);
                claim.AddNotification(new LiabilityStatusUpdatedNotification(liabilityStatus));
            }
            claim.setClaimNumber(claimNumber);
            claim.setIndemnityAmount(indemnityAmount);
            claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
            claim.setIsInvoiceReviewRequired(isInvoiceReviewRequired);
            claim.setIsQuantumDispute(isQuantumDispute);
            claim.setReasonOfRejection(getReasonOfRejection());
            claim.setPercentageLiabilityCho(percentageLiabilityCho);
            claim.setLiabilityAgreedDate(liabilityAgreedDate);
            

        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.New(0, engineerClaimReviewNotes));
        }

//        if (getReasonOfRejection() != null) {
//            claim.addComment(Comment.New(0, "Reason For Rejection: " + getReasonOfRejection().getName()));
//        }

        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setIsFnolReviewed(false);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
    }

    protected ReasonOfRejection getReasonOfRejection() {
        ReasonOfRejection reasonOfRejection = null;
        if (reasonOfRejectionId > 0) {
            reasonOfRejection = (ReasonOfRejection) this.getDataService().get(ReasonOfRejection.class, reasonOfRejectionId);
        }
        return reasonOfRejection;
    }
}
