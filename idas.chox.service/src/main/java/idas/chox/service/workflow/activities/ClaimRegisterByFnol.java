package idas.chox.service.workflow.activities;

import org.hibernate.util.StringHelper;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;

public class ClaimRegisterByFnol extends BaseActivity {

    private String claimNumber;
    private String reasonForRejection;

    @Override
    protected void doProcess(Claim claim) throws Exception {

        String nextStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;
        if (claim.getPreviousStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
            nextStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED;
        }

        claim.setStatus(nextStatus);
        claim.setClaimNumber(claimNumber);
        claim.setIsFnolReviewed(true);

        if (StringHelper.isNotEmpty(reasonForRejection)) {
            claim.addComment(Comment.New(1, reasonForRejection));
        }
        
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