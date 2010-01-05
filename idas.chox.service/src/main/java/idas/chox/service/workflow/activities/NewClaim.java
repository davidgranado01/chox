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

/**
 *
 * @author emmanuel
 */
public class NewClaim extends BaseActivity {

    @Override
    protected void prepare(Claim claim) {
        if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {

            claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
        }
        //Normalize caim number
        String claimNumber = claim.getClaimNumber();
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claim.setClaimNumber(claimNumber.trim());
        }
    }

    @Override
    protected void validate(Claim claim) throws Exception {

        if (!claim.isTransient()) {
            throw new Exception("An process new claim attempt failed due to claim is already exist.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimRouting(claim);
    }

    @Override
    protected void onProcessCompleted(Claim claim) {
        //super.onProcessCompleted(claim);
        getDataService().save(claim);
        logTransaction(claim, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, "");
        if (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)) {
            logTransaction(claim, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, 1);
        } else if (claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
            logTransaction(claim, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, 1);
            logTransaction(claim, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, 2);
        }
    }

    @Override
    protected String getCurrentStatus() {
        return "";
    }

    @Override
    protected String getNextStatus() {
        return "";
    }

    public void claimRouting(Claim claim) throws Exception {

        if (claim != null) {

            // CHECK AUTOMATIC ROUTING AND OWNERSHIP
            if (claim.getThirdParty() != null) {
                if (claim.getThirdParty().getInsurer() != null) {

                    // CHECK WORKGROUP ENABLE
                    if (claim.getThirdParty().getInsurer().isWorkgroupEnable()) {
                        // >> WORKGROUP ENABLED

                        // CHECK AUTOMATIC ENABLE
                        if (claim.getThirdParty().getInsurer().isAutoRoutingEnable()) {
                            doAutomaticClaimRoutingEnable(claim);
                        }

                    } else {
                        // >> NOT WORKGROUP ENABLED
                        doWorkgroupDisable(claim);

                    }
                    doClaimOwnership(claim);
                }
            }
        }
    }

    public void doAutomaticClaimRoutingEnable(Claim claim) throws Exception {

        AutomaticRoutingService automaticRoutingService = getProcessContext().getAutomaticRoutingService();

        int insurerId = claim.getThirdParty().getInsurer().getId();
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

    private void doWorkgroupDisable(Claim claim) {
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
    }

    private void doClaimOwnership(Claim claim) {

        if (claim.getThirdParty().getInsurer().isClaimOwnershipEnable() && claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }
    }
}
