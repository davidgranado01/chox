package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;
import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.service.xml.util.NodeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoRouting extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(AutoRouting.class);

    @Override
    public boolean isRequired(Claim claim) {
        LOG.debug("Auto-routing required:", (claim != null && claim.getInsurer() != null));
        return (claim != null && claim.getInsurer() != null);
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Auto-routing - processing");
        if (claim.getInsurer().isWorkgroupEnable()) {
            LOG.debug("Workgroups are enabled");
            if (claim.getInsurer().isAutoRoutingEnable()) {
                LOG.debug("Auto-routing is enabled");
                autoWorkgroupRouting(claim);
            }
        }
        
        /*
        if (claim.getInsurer().isWorkgroupEnable()) {

            if (claim.getInsurer().isAutoRoutingEnable()) {
                autoWorkgroupRouting(claim);
            }

        } else {

            if (claim.getInsurer().isClaimOwnershipEnable()) {
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
            }

        }
        */
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        getDataService().save(claim);
        logTransaction(claim, 0);
        LOG.debug("Auto-routing - after processing");
        if(claim.getInsurer().isWorkgroupEnable() && !claim.getInsurer().isAutoRoutingEnable()){
            LOG.debug("Nothing to do");
        }else{
            if (chainActivity != null) {
                chainActivity.processInBatch(claim);
                LOG.debug("Processing chain activity");
            }
            else
                LOG.debug("No chain activity to process.");
        }
        
    }
    
    protected void autoWorkgroupRouting(Claim claim) throws Exception {
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

                        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);

                        /*
                        if (claim.getInsurer().isClaimOwnershipEnable()) {
                            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
                        } else {
                            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        }
                        */

                        break;
                    }
                }
            }

        } else {
            LOG.error("Automatic Routing Mapping is Not Defined for claim '{}'", claim.getChoReference());
            throw new Exception("Automatic Routing Mapping is Not Defined, Please contact CHOX Admin");
        }

    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }
}
