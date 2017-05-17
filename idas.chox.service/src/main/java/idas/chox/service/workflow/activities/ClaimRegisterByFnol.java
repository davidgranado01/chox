package idas.chox.service.workflow.activities;

import org.hibernate.internal.util.StringHelper;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;

public class ClaimRegisterByFnol extends BaseActivity {

    private String claimNumber;
    private String reasonForRejection;
    private boolean  claimNumberUpdated = false;
    
    @Override
    protected void doProcess(Claim claim) throws Exception {

        String nextStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;
        if (claim.getPreviousStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
            nextStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED;
        }

        claim.setStatus(nextStatus);
        if (!claim.getClaimNumber().equals(claimNumber)) {
            claim.setClaimNumber(claimNumber);
            claimNumberUpdated = true;
        }
        claim.setIsFnolReviewed(true);

        if (StringHelper.isNotEmpty(reasonForRejection)) {
            claim.addComment(Comment.newComment(1, reasonForRejection));
        }
        
    }

    public boolean isClaimNumberUpdated() {
        return claimNumberUpdated;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }
}