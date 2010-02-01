/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.service.xml.util.NodeHelper;
import java.util.List;

public class ClaimOwnerAssigning extends BaseActivity {

    @Override
    public boolean isRequired(Claim claim) {
        //process only in claim status is CLAIM_UNACKNOWLEDGED_ROUTED
        return (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)
                && claim != null && claim.getInsurer() != null
                && claim.getInsurer().isClaimOwnershipEnable());
    }

    @Override
    protected void beforeProcess(Claim claim) throws Exception {
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);

    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
    }
}
