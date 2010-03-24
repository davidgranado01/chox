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

        try {
            super.validate(claim);
            if (workgroupId <= 0) {
                throw new Exception("Invalid workgroup id.");
            } else {
                workgroup = (Workgroup) this.getWorkflowContext().getDataService().get(Workgroup.class, workgroupId);
                logger.debug("validate assign workgroup " + workgroup.getName());
                if (workgroup == null) {
                    throw new Exception("An attempt to assign work group failed due to invalid workgroup provided");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

    }

    @Override
    protected void beforeProcess(Claim claim) throws Exception {
        try {
            claim.setWorkgroup(workgroup);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        try {
            if (claim.getInsurer().isClaimOwnershipEnable()) {
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
            } else {
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;

        }

    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }
}
