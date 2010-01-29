/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;

/**
 *
 * @author emmanuel
 */
public class WorkgroupRouting extends BaseActivity {

    @Override
    public boolean isRequired(Claim claim) {
        return (claim != null && claim.getInsurer() != null);
    }    

    @Override
    protected void doProcess(Claim claim) throws Exception {

        /*
        if (claim.getInsurer().isWorkgroupEnable()) {
            if (claim.getInsurer().isAutoRoutingEnable()) {
                autoWorkgroupRouting(claim);
            }
        } else {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        }
        */

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);        
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        getDataService().save(claim);
        logTransaction(claim, 1);

        if (chainActivity != null) {
            chainActivity.processInBatch(claim);
        }
    }

    /*
    protected void autoWorkgroupRouting(Claim claim) throws Exception {
        AutomaticRoutingService automaticRoutingService = getWorkflowContext().getAutomaticRoutingService();

        int insurerId = claim.getInsurer().getId();
        List<AutomaticRouting> automaticRoutingMapping = automaticRoutingService.getAutomaticRoutings(insurerId);

        if (automaticRoutingMapping.size() > 0) {

            String policyNumber = claim.getThirdParty().getPolicyNumber().trim();

            if (policyNumber != null && !policyNumber.equalsIgnoreCase("")) {

                for (AutomaticRouting automaticRouting : automaticRoutingMapping) {

                    NodeHelper nodeHelper = new NodeHelper();
                    if (nodeHelper.isRegularExpressionCheckPass(automaticRouting.getExpression(), policyNumber.toUpperCase())) {

                        claim.setWorkgroup(automaticRouting.getWorkgroup());
                        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        break;
                    }
                }
            }
        } else {
            throw new Exception("Automatic Routing Mapping is Not Defined, Please contact CHOX Admin");
        }
    }
    */
    
    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
       expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }
}
