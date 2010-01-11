package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import java.util.List;

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
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        claim.setClaimOwner(claimOwner);
        claim.setWorkgroup(workgroup);
        claim.setIsFnolReviewed(false);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
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
