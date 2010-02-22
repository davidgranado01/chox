package idas.chox.service.workflow.activities;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.service.xml.util.NodeHelper;
import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class WorkgroupRouting extends BaseActivity {
//    static final Logger logger = LoggerFactory.getLogger(WorkgroupRouting.class);

    @Override
    public boolean isRequired(Claim claim) {
        boolean isRequired = true;

        if(claim != null && claim.getInsurer() != null){
            if((!claim.getInsurer().isWorkgroupEnable() && !claim.getInsurer().isClaimOwnershipEnable()) || (claim.getInsurer().isWorkgroupEnable() && !claim.getInsurer().isAutoRoutingEnable())){
                isRequired = false;
            }
        }
        
        return isRequired;
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

//        logger.debug("{} :: THIS STATUS {}", claim.getChoReference(), claim.getStatus());

        boolean isClaimOwnerCheckedRequired = true;
        if(claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().isAutoRoutingEnable()){
            if(autoWorkgroupRouting(claim)){
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            }else{
                isClaimOwnerCheckedRequired = false;
            }
        }
        else if (!claim.getInsurer().isWorkgroupEnable()) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            isClaimOwnerCheckedRequired = false;
        }

        if(isClaimOwnerCheckedRequired && claim.getInsurer().isClaimOwnershipEnable()){
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }

//        logger.debug("{} :: THIS NEW {}", claim.getChoReference(), claim.getStatus());

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
                        return true;
                        // claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        // break;
                    }
                }
            }
        } else {
            throw new Exception("Automatic Routing Mapping is Not Defined, Please contact CHOX Admin");
        }

        return false;
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
       expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }
}
