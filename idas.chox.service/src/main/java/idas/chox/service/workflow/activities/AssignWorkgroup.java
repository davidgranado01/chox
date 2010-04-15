package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Workgroup;
import java.util.List;

public class AssignWorkgroup extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(AssignWorkgroup.class);

    private int workgroupId;
    private Workgroup workgroup;

    @Override
    protected void validate(Claim claim) throws Exception {

        try {
            super.validate(claim);
            LOG.debug("AssignWorkgroup Activity validation: workgroupId='{}'", workgroupId);
            if (workgroupId <= 0) {
                throw new Exception("Invalid workgroup id.");
            } else {
                workgroup = (Workgroup) this.getWorkflowContext().getDataService().get(Workgroup.class, workgroupId);
                LOG.debug("validate assign workgroup " + workgroup.getName());
                if (workgroup == null) {
                    throw new Exception("An attempt to assign work group failed due to invalid workgroup provided");
                }
            }
        } catch (Exception e) {
            LOG.error("Exception thrown: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    protected void beforeProcess(Claim claim) throws Exception {
        try {
            claim.setWorkgroup(workgroup);
        } catch (Exception e) {
            LOG.error("Exception thrown: {}", e.getMessage());
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
            LOG.error("Exception thrown: {}", e.getMessage());
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
