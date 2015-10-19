package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.AutomaticRoutingCho;
import idas.chox.core.model.AutomaticRoutingPolicy;
import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.core.model.AutomaticRoutingStrategy;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.xml.util.NodeHelper;

public class WorkgroupRouting extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(WorkgroupRouting.class);
    private VehicleClassPriceService vehicleClassPriceService;
    private WorkgroupService workgroupService;
    private static final Map<Integer, Integer> workgoupMap = new ConcurrentHashMap(10);
    
    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    @Override
    public boolean needsOwnershipCheck() {
        return false;
    }

    @Override
    public boolean isRequired(Claim claim) {
        return true;
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        LOG.debug("Claim '{}' status is {}", claim.getChoReference(), claim.getStatus());
        boolean routed = false;
        AutomaticRoutingStrategy routingStrategy = claim.getInsurer().getAutomaticRoutingStrategy();
        if (claim.getInsurer().isWorkgroupEnable()) {
            switch (routingStrategy) {
                case NONE:
                    break;
                case POLICY:
                    if (routeByPolicyNumber(claim)) {
                        LOG.debug("Claim has been auto-routed by Policy Number - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        routed = true;
                    }
                    break;
                case PRICE:
                    if (routeByPrice(claim)) {
                        LOG.debug("Claim has been auto-routed by Vehicle Class Price - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        routed = true;
                    }
                    break;
                case CHO:
                    if (routeByChoAssignment(claim)) {
                        LOG.debug("Claim has been auto-routed by CHO Assignment - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        routed = true;
                    }
                    break;
                case ROUND_ROBIN:
                    if (routeByRoundRobin(claim)) {
                        LOG.debug("Claim has been auto-routed by round-robin- sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        routed = true;
                    }
                    break;
                case FEWEST_CLAIMS:
                    if (routeByFewestClaims(claim)) {
                        LOG.debug("Claim has been auto-routed by fewest claims - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                        routed = true;
                    }
                    break;
            }
        } else { // No Workgroups
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            routed = true;
        }
        
        // If auto-routing stratigy one of POLICY, PRICE or CHO and the claim has not been routed and complete routing
        // is enabled, then route by fewest claims
        if (!routed && (routingStrategy == AutomaticRoutingStrategy.POLICY || routingStrategy == AutomaticRoutingStrategy.PRICE
                || routingStrategy == AutomaticRoutingStrategy.CHO) && claim.getInsurer().isCompleteRoutingEnable()) {
            if (routeByFewestClaims(claim)) {
                LOG.debug("Claim has been auto-routed by fewest claims (complete routing enabled) - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                routed = true;
            }
        }
        
        if (routed && claim.getInsurer().isClaimOwnershipEnable()) {
            LOG.debug("Claim ownership is enabled - set status to CLAIM_UNACKNOWLEDGED_UNASSIGNED");
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }

        LOG.debug("Claim '{}' new status is {}", claim.getChoReference(), claim.getStatus());

    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        getDataService().save(claim);
        logTransaction(claim);
        activityEventGenerator.generate(claim, this);

        if (getChainActivity() != null) {
            getChainActivity().processInBatch(claim);
        }

    }

    private boolean routeByFewestClaims(Claim claim) {
        int insId = claim.getInsurer().getId();
        List<Workgroup> workgroups = workgroupService.getActiveWorkgroupsByInsurerSortByNoClaims(insId);
        
        if (workgroups.size() > 0) {
            claim.setWorkgroup(workgroups.get(0));
            return true;
        }
        
        return false;
    }

    private boolean routeByChoAssignment(Claim claim) {
        AutomaticRoutingService automaticRoutingService = getWorkflowContext().getAutomaticRoutingService();
        int choId = claim.getChorganisation().getId();
        int insurerId = claim.getInsurer().getId();

        List<AutomaticRoutingCho> automaticRoutingMapping = automaticRoutingService.getAutomaticRoutingsByCho(insurerId, choId);
        if (automaticRoutingMapping.size() > 0) {
            if (automaticRoutingMapping.size() > 1) {
                LOG.warn("Multiple mappings found for claim '{}' with CHO '{}'", claim.getChoReference(), claim.getChorganisation().getName());
            }
            claim.setWorkgroup(automaticRoutingMapping.get(0).getWorkgroup());
            return true;
        } else {
            LOG.info("No auto-routing by CHO found for claim '{}' with CHO '{}'", claim.getChoReference(), claim.getChorganisation().getName());
        }
        
        return false;
    }


    private boolean routeByRoundRobin(Claim claim) {
        int insId = claim.getInsurer().getId();
        List<Workgroup> workgroups = workgroupService.getActiveWorkgroupsByInsurer(insId);
        
        // Get index of last workgroup assigned
        int nextToBeAssignedIndex = workgoupMap.get(insId) == null ? 0 : workgoupMap.get(insId) + 1;
        if (nextToBeAssignedIndex >= workgroups.size()) {
            nextToBeAssignedIndex = 0;
        }
        claim.setWorkgroup(workgroups.get(nextToBeAssignedIndex));
        workgoupMap.put(insId, nextToBeAssignedIndex);

        return true;
    }


    private boolean routeByPolicyNumber(Claim claim) throws Exception {
        LOG.debug("Auto-routing claim: {}", claim.getChoReference());

        AutomaticRoutingService automaticRoutingService = getWorkflowContext().getAutomaticRoutingService();

        int insurerId = claim.getInsurer().getId();
        List<AutomaticRoutingPolicy> automaticRoutingMapping = automaticRoutingService.getAutomaticRoutingsByPolicy(insurerId);

        if (automaticRoutingMapping.size() > 0) {

            String policyNumber = claim.getThirdParty().getPolicyNumber().trim();

            if (policyNumber != null && !policyNumber.equalsIgnoreCase("")) {

                for (AutomaticRoutingPolicy automaticRouting : automaticRoutingMapping) {

                    if (NodeHelper.isRegularExpressionCheckPass(automaticRouting.getExpression(), policyNumber.toUpperCase())) {
                        LOG.debug("Found regex match: {} -> {}", automaticRouting.getExpression(), automaticRouting.getWorkgroup());
                        claim.setWorkgroup(automaticRouting.getWorkgroup());
                        return true;
                    }
                }
            }
        } else {
            LOG.warn("Automatic Routing Mapping is Not Defined for claim '{}'", claim.getChoReference());
        }

        return false;
    }

    private boolean routeByPrice(Claim claim) throws Exception {
        LOG.debug("Auto-routing claim based on price : {}", claim.getChoReference());

        AutomaticRoutingService automaticRoutingService = getWorkflowContext().getAutomaticRoutingService();
        int insurerId = claim.getInsurer().getId();
        LOG.debug("insurer  id :{}", insurerId);

        List<AutomaticRoutingPrice> automaticRoutingMappingPrice = automaticRoutingService.getAutomaticRoutingsByPrice(insurerId);


        BigDecimal age = BigDecimal.ZERO;
        Date hireStart = null;
        BigDecimal vehicleClassPrice;
        VehicleClass vehicleClass;
        Date firstRegistration;

        if (claim.getVehicleHire() != null && claim.getVehicleHire().getVehicleClass() != null) {
            hireStart = claim.getVehicleHire().getHireStart();
            vehicleClass = claim.getVehicleHire().getVehicleClass();
            firstRegistration = claim.getVehicleHire().getHpiFirstRegistration();
        } else {
            LOG.debug("No vehicle hire available for claim {} - using customer's vehicle price for routing", claim.getChoReference());
            vehicleClass = claim.getCustomer().getVehicleClass();
            firstRegistration = claim.getCustomer().getHpiFirstRegistration();
        }


        if (hireStart == null) {
            LOG.debug("Hire Start is null - using todays date");
            hireStart = new Date();
        }

        if (firstRegistration != null) {
            age = new BigDecimal(DateHelper.differenceInYears(hireStart, firstRegistration));
        }
        LOG.debug("vehicle class age : {}", age.setScale(2, BigDecimal.ROUND_HALF_UP).toString());


        try {
            vehicleClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), vehicleClass, hireStart, age, claim.getInsurer().getId(), claim.getChorganisation().getId());
        } catch (Exception ex) {
            LOG.warn("No vehicle class price found - using 0.00: {}", ex.getMessage());
            vehicleClassPrice = BigDecimal.ZERO;
        }

        LOG.debug("vehicle class price : {}", vehicleClassPrice);


        if (automaticRoutingMappingPrice.size() > 0) {

            LOG.debug("automaticRoutingMappingPrice found");

            if (vehicleClassPrice != null) {

                for (AutomaticRoutingPrice automaticRouting : automaticRoutingMappingPrice) {

                    if (vehicleClassPrice.compareTo(automaticRouting.getPrice()) == -1) {
                        LOG.debug("Found price match: {} -> {}", automaticRouting.getPrice(), automaticRouting.getWorkgroup());
                        claim.setWorkgroup(automaticRouting.getWorkgroup());
                        return true;
                    }
                }
            }
        } else {
            LOG.warn("Automatic Routing Mapping based on price is Not Defined for claim '{}'", claim.getChoReference());
        }

        return false;

    }
}
