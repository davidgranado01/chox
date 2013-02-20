package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.xml.util.NodeHelper;

public class WorkgroupRouting extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(WorkgroupRouting.class);
    private VehicleClassPriceService vehicleClassPriceService;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
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
        boolean isClaimOwnerCheckedRequired = true;

        if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().isAutoRoutingEnablePrice()) {

            if (autoWorkgroupRoutingByPrice(claim)) {
                LOG.debug("Claim has been auto-routed based on price - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
            }
        }

        if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().isAutoRoutingEnable()) {
            LOG.debug("Trying to rout claim...");
            if (autoWorkgroupRouting(claim)) {
                LOG.debug("Claim has been auto-routed - sets status to CLAIM_UNACKNOWLEDGED_ROUTED");
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            } else {
                LOG.debug("No auto-routing for claim {}.", claim.getChoReference());
                isClaimOwnerCheckedRequired = false;
            }
        } else if (!claim.getInsurer().isWorkgroupEnable()) {
            LOG.debug("Workgroups are  disabled - set status to CLAIM_UNACKNOWLEDGED_ROUTED");
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            if (!claim.getInsurer().isClaimOwnershipEnable()) {
                isClaimOwnerCheckedRequired = false;
            }
        } else if (claim.getInsurer().isWorkgroupEnable() && !claim.getInsurer().isAutoRoutingEnable()) {
            LOG.debug("Workgroups are enabled, auto-routing disabled", claim.getChoReference());
            isClaimOwnerCheckedRequired = false;
        }

        if (isClaimOwnerCheckedRequired && claim.getInsurer().isClaimOwnershipEnable()) {
            LOG.debug("Claim ownership is enabled - set status to CLAIM_UNACKNOWLEDGED_UNASSIGNED");
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }

        LOG.debug("Claim '{}' new status is {}", claim.getChoReference(), claim.getStatus());

    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        getDataService().save(claim);
        logTransaction(claim);

        if (getChainActivity() != null) {
            getChainActivity().processInBatch(claim);
        }

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
                    }
                }
            }
        } else {
            LOG.error("Automatic Routing Mapping is Not Defined for claim '{}'", claim.getChoReference());
            throw new Exception("Automatic Routing Mapping is Not Defined, Please contact CHOX Admin");
        }

        return false;
    }

    protected boolean autoWorkgroupRoutingByPrice(Claim claim) throws Exception {
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
        }
        else {
            LOG.debug("No vehicle hire available for claim {} - using customer's vehicle price for routing", claim.getChoReference());
            vehicleClass = claim.getCustomer().getVehicleClass();
            firstRegistration = claim.getCustomer().getHpiFirstRegistration();
        }

        
        if (hireStart == null) {
            LOG.debug("Hire Start is null - using todays date");
            hireStart = new Date();
        }

        if (firstRegistration != null && hireStart != null) {
            age = new BigDecimal(DateHelper.DifferenceInYears(hireStart, firstRegistration));
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

            if (vehicleClassPrice != null ) {

                for (AutomaticRoutingPrice automaticRouting : automaticRoutingMappingPrice) {

                    if (vehicleClassPrice.compareTo(automaticRouting.getPrice())==-1) {
                        LOG.debug("Found price match: {} -> {}", automaticRouting.getPrice(), automaticRouting.getWorkgroup());
                        claim.setWorkgroup(automaticRouting.getWorkgroup());
                        return true;
                    }
                }
            }
        } else {
            LOG.error("Automatic Routing Mapping based on price is Not Defined for claim '{}'", claim.getChoReference());
            throw new Exception("Automatic Routing Mapping  based on is Not Defined, Please contact CHOX Admin");
        }

        return false;

    }
}
