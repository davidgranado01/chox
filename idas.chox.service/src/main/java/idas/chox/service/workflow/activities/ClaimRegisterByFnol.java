package idas.chox.service.workflow.activities;

import java.util.List;

import org.hibernate.util.StringHelper;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;

public class ClaimRegisterByFnol extends BaseActivity {

    private String claimNumber;
    private String reasonForRejection;

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                    && !securityInfoProvider.getIsCHOXAdmin() && !securityInfoProvider.isInRoleOf("ROLE_INS_FNOL")) {
            throw new AccessDeniedException("Not in correct role to return from FNOL.");
        }
    }

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

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
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