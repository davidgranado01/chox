package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Workgroup;
import java.util.List;

public class AssignWorkgroup extends BaseActivity {

    private int workgroupId;
    private Workgroup workgroup;

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        if (workgroupId <= 0) {
            throw new Exception("Invalid workgroup id.");
        } else {
            workgroup = (Workgroup) this.getWorkflowContext().getDataService().get(Workgroup.class, workgroupId);
            if (workgroup == null) {
                throw new Exception("An attempt to assign work group failed due to invalid workgroup provided");
            }
        }
    }

    @Override
    protected void beforeProcess(Claim claim) throws Exception {
        claim.setWorkgroup(workgroup);
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }
}
