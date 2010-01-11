package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.util.StringHelper;

public class ClaimReferToFnol extends BaseActivity {

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
    }

    @Override
    protected void beforeProcess(Claim claim) {

        if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {

            claim.setClaimOwner(claimOwner);
            claim.setWorkgroup(workgroup);

        } else {

            claim.setClaimNumber(claimNumber);
            claim.setIndemnityAmount(indemnityAmount);
            claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
            claim.setIsInvoiceReviewRequired(isInvoiceReviewRequired);
            claim.setIsQuantumDispute(isQuantumDispute);
            claim.setReasonOfRejection(getReasonOfRejection());

        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.New(1, engineerClaimReviewNotes));
        }

        if (getReasonOfRejection() != null) {
            claim.addComment(Comment.New(0, "Reason For Rejection: " + getReasonOfRejection().getName()));
        }

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
