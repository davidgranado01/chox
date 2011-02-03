package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.service.xml.util.NodeHelper;
import java.util.List;

public class WorkgroupRouting extends BaseActivity {
    static final Logger LOG = LoggerFactory.getLogger(WorkgroupRouting.class);
//    private static Log logger = LogFactory.getLog(WorkgroupRouting.class);

    @Override
    public boolean isRequired(Claim claim) {
        boolean isRequired = true;

//        if(claim != null && claim.getInsurer() != null){
//            if (!claim.getInsurer().isWorkgroupEnable()) { // && !claim.getInsurer().isClaimOwnershipEnable()
//                isRequired = false;
//                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
//            }
//            else if (claim.getInsurer().isWorkgroupEnable() && !claim.getInsurer().isAutoRoutingEnable()) {
//                isRequired = false;
//            }
//        }
        
        return isRequired;
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        LOG.debug("Claim '{}' status is {}", claim.getChoReference(), claim.getStatus());
//        logger.debug(claim.getChoReference() + ": CURRENT STATUS = " + claim.getStatus());
//        System.out.println(claim.getChoReference() + " :: THIS STATUS = " + claim.getStatus());
        boolean isClaimOwnerCheckedRequired = true;
        if(claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().isAutoRoutingEnable()){
            LOG.debug("Trying to rout claim...");
            if(autoWorkgroupRouting(claim)){
                LOG.debug("Claim has been auto-routed - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            }else{
                LOG.debug("No auto-routing for claim {}.", claim.getChoReference());
                isClaimOwnerCheckedRequired = false;
            }
        }
        else if (!claim.getInsurer().isWorkgroupEnable()) {
            LOG.debug("Workgroups are  disabled - set status to CLAIM_UNACKNOWLEDGED_ROUTED");
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            if (!claim.getInsurer().isClaimOwnershipEnable())
                isClaimOwnerCheckedRequired = false;
        }
        else if (claim.getInsurer().isWorkgroupEnable() && !claim.getInsurer().isAutoRoutingEnable()) {
            LOG.debug("Workgroups are enabled, auto-routing disabled", claim.getChoReference());
            isClaimOwnerCheckedRequired = false;
        }

        if(isClaimOwnerCheckedRequired && claim.getInsurer().isClaimOwnershipEnable()){
            LOG.debug("Claim ownership is enabled - set status to CLAIM_UNACKNOWLEDGED_UNASSIGNED");
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }

        LOG.debug("Claim '{}' new status is {}", claim.getChoReference(), claim.getStatus());
//        logger.debug(claim.getChoReference() + ": NEW STATUS = " + claim.getStatus());
//        System.out.println(claim.getChoReference() + " :: THIS NEW = " + claim.getStatus());

    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        //if(!claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)){

            getDataService().save(claim);
            logTransaction(claim);

            if (chainActivity != null) {
                chainActivity.processInBatch(claim);
            }
            
        //}
    }
    
    protected boolean autoWorkgroupRouting(Claim claim) throws Exception {
        LOG.debug("Auto-routing claim: {}", claim.getChoReference());
        
        AutomaticRoutingService automaticRoutingService = getWorkflowContext().getAutomaticRoutingService();

        int insurerId = claim.getInsurer().getId();
        List<AutomaticRouting> automaticRoutingMapping = automaticRoutingService.getAutomaticRoutings(insurerId);

        if (automaticRoutingMapping.size() > 0) {

            String policyNumber = claim.getThirdParty().getPolicyNumber().trim();

            if (policyNumber != null && !policyNumber.equalsIgnoreCase("")) {

                for (AutomaticRouting automaticRouting : automaticRoutingMapping) {

                    NodeHelper nodeHelper = new NodeHelper();
                    if (nodeHelper.isRegularExpressionCheckPass(automaticRouting.getExpression(), policyNumber.toUpperCase())) {
                        LOG.debug("Found regex match: {} -> {}", automaticRouting.getExpression(), automaticRouting.getWorkgroup());
                        claim.setWorkgroup(automaticRouting.getWorkgroup());
                        return true;
                        // claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        // break;
                    }
                }
            }
        } else {
            LOG.error("Automatic Routing Mapping is Not Defined for claim '{}'", claim.getChoReference());
            throw new Exception("Automatic Routing Mapping is Not Defined, Please contact CHOX Admin");
        }

        return false;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
       expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }
}
