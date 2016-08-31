package idas.chox.web.actions;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.hpi.Hpi;
import idas.chox.core.hpi.HpiException;
import idas.chox.core.hpi.HpiResponse;
import idas.chox.core.model.Customer;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.security.TabAccessibility;

public class CustomerAction extends ClaimModelAction<Customer> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerAction.class);

    private LookupService lookupService;
    private VehicleClassService vehicleClassService;
    private int vehicleClassId;
    private String oldVRN;
    private Boolean isUsableOriginal;
    @Override
    protected Customer loadModel() {

        if (claim.getCustomer() == null) {
            oldVRN = "";
            return new Customer();
        } else {
            oldVRN = claim.getCustomer().getVehicleRegistration();
            isUsableOriginal = claim.getCustomer().getIsUsable();
            return claim.getCustomer();
        }
    }

    @Override
    public String updateModel() {
        LOG.debug("Updating customer model: oldvrn={}, newvrn={}", oldVRN, model.getVehicleRegistration());
//        if (hpiCheck == null)
//            LOG.debug("hpiCheck is null");
        if (!oldVRN.equalsIgnoreCase(model.getVehicleRegistration())) {
            LOG.debug("VRN has changed - performing HPI check/retrieval");
            try {
                HpiResponse response = Hpi.getHpiInfo(model.getVehicleRegistration());
                model.setHpiVehicleManufacturer(response.getManufacturer());
                model.setHpiVehicleModel(response.getModel());
                model.setHpiVehicleYear(response.getYear());
                model.setHpiVehicleCapacity(response.getCapacity());
                model.setHpiVehicleDoorplan(response.getDoorPlan());
                model.setHpiVehicleTransmission(response.getTransmission());
                model.setHpiFirstRegistration(response.getFirstRegistration());
                model.setHpiError(null);
            } catch (HpiException ex) {
                LOG.warn("Error getting HPI info for vrn '{}': {}",  claim.getCustomer().getVehicleRegistration(), ex.getMessage());
                model.setHpiError(ex.getMessage());
                model.setHpiVehicleManufacturer(null);
                model.setHpiVehicleModel(null);
                model.setHpiVehicleYear(null);
                model.setHpiVehicleCapacity(null);
                model.setHpiVehicleDoorplan(null);
                model.setHpiVehicleTransmission(null);
                model.setHpiFirstRegistration(null);
            }
        }
        // If cusomer car is now usable, we need to check for hire anomolies
        if (!((isUsableOriginal != null && isUsableOriginal) || isUsableOriginal == null) && model.getIsUsable()!=null && model.getIsUsable()) {
                claimService.checkRepairBookedInDateAnomaly(claim);
                // update model in session before calling super.updateModel as model version
                // may have been increased when anomalous added or removed from claim.
//                updateModelInSession(Arrays.asList(claim, claim.getHireMonitoringDetail(), model));
            
        }
        claim.setCustomer(model);
        if (vehicleClassId >= 0) {
            claim.getCustomer().setVehicleClass(this.vehicleClassService.getVehicleClass(vehicleClassId));
        }
        return super.updateModel();
    }
    
    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("CustomerAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("CustomerAction validate success");
        }
        else {
            LOG.debug(" CustomerAction validation is not done as claim is null");
        }
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public int getVehicleClassId() {
        Customer customer = getModel();
        return customer.getVehicleClass() != null ? vehicleClassId = customer.getVehicleClass().getId() : 0;
    }

    public List<VehicleClass> getVehicleClasses() {
        return this.lookupService.getVehicleClasses();
    }

    public List<Insurer> getInsurer() {
        return this.lookupService.getInsurers();
    }
}
