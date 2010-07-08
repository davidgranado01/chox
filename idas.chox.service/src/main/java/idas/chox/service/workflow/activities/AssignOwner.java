package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class AssignOwner extends BaseActivity {

    private int oasWorkgroupId;
    private int claimOwnerId;
    private WebUser claimOwner;
    private Workgroup workgroup;

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        if (oasWorkgroupId <= 0) {
            throw new Exception("Invalid workgroup id.");
        } else {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
            if (workgroup == null) {
                throw new Exception("Invalid workgroup id.");
            }
        }

        if (claimOwnerId <= 0) {
            throw new Exception("Invalid user id.");
        } else {
            claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            if (claimOwner == null) {
                throw new Exception("Invalid user id.");
            }
        }

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                    && !securityInfoProvider.getIsCHOXAdmin() && !securityInfoProvider.isInRoleOf("ROLE_INS_COM")) {
            throw new AccessDeniedException("Not in correct role to assign owner.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        claim.setClaimOwner(claimOwner);
        claim.setWorkgroup(workgroup);
        claim.setIsFnolReviewed(false);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        if (claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
            Comment comment = Comment.New(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() +").");
            claim.addComment(comment);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
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
}
