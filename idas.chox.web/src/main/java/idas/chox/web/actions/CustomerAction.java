package idas.chox.web.actions;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
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
    private Customer originalModel;

    @Override
    protected Customer loadModel() {
        Customer returnCustomer;
        
        if (claim.getCustomer() == null) {
            oldVRN = "";
            returnCustomer = new Customer();
        } else {
            oldVRN = claim.getCustomer().getVehicleRegistration();
            isUsableOriginal = claim.getCustomer().getIsUsable();
            returnCustomer = claim.getCustomer();
        }
        try {
            originalModel = (Customer) SerializationUtils.clone(returnCustomer);
        } catch (Exception ex) {
            LOG.error("Exception cloning customer: {}", ex.getMessage(), ex);
        }

        replaceHashedStrings(returnCustomer);

        return returnCustomer;
    }

    @Override
    public String updateModel() {

        if (!oldVRN.equalsIgnoreCase(model.getVehicleRegistration())) {
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
        if (claim.isHashed()) { // should never be true as a hashed claim should not be updated
            // Hashed fields should not be changed
            model.setTitle(originalModel.getTitle());
            model.setFirstName(originalModel.getFirstName());
            model.setLastName(originalModel.getLastName());
            model.setAddress1(originalModel.getAddress1());
            model.setAddress2(originalModel.getAddress2());
            model.setAddress3(originalModel.getAddress3());
            model.setAddress4(originalModel.getAddress4());
            model.setAddress5(originalModel.getAddress5());
            model.setPostcode(originalModel.getPostcode());
            model.setTelephoneDay(originalModel.getTelephoneDay());
            model.setTelephoneEvening(originalModel.getTelephoneEvening());
            model.setEmail(originalModel.getEmail());
            model.setOccupation(originalModel.getOccupation());
        }
        
        // If claim has been re-opened after being hashed..
        if (GDPR_REMOVED_STRING.equals(model.getTitle())) model.setTitle(originalModel.getTitle());
        if (GDPR_REMOVED_STRING.equals(model.getFirstName())) model.setFirstName(originalModel.getFirstName());
        if (GDPR_REMOVED_STRING.equals(model.getLastName())) model.setLastName(originalModel.getLastName());
        if (GDPR_REMOVED_STRING.equals(model.getAddress1())) model.setAddress1(originalModel.getAddress1());
        if (GDPR_REMOVED_STRING.equals(model.getAddress2())) model.setAddress2(originalModel.getAddress2());
        if (GDPR_REMOVED_STRING.equals(model.getAddress3())) model.setAddress3(originalModel.getAddress3());
        if (GDPR_REMOVED_STRING.equals(model.getAddress4())) model.setAddress4(originalModel.getAddress4());
        if (GDPR_REMOVED_STRING.equals(model.getAddress5())) model.setAddress5(originalModel.getAddress5());
        if (GDPR_REMOVED_STRING.equals(model.getPostcode())) model.setPostcode(originalModel.getPostcode());
        if (GDPR_REMOVED_STRING.equals(model.getTelephoneDay())) model.setTelephoneDay(originalModel.getTelephoneDay());
        if (GDPR_REMOVED_STRING.equals(model.getTelephoneEvening())) model.setTelephoneEvening(originalModel.getTelephoneEvening());
        if (GDPR_REMOVED_STRING.equals(model.getEmail())) model.setEmail(originalModel.getEmail());
        if (GDPR_REMOVED_STRING.equals(model.getOccupation())) model.setOccupation(originalModel.getOccupation());

        // If cusomer car is now usable, we need to check for hire anomolies
        if (!((isUsableOriginal != null && isUsableOriginal) || isUsableOriginal == null) && model.getIsUsable()!=null && model.getIsUsable()) {
                claimService.checkRepairBookedInDateAnomaly(claim);
                // update model in session before calling super.updateModel as model version
                // may have been increased when anomalous added or removed from claim.
                updateModelInSession(Arrays.asList(claim, claim.getHireMonitoringDetail(), model));
            
        }
        claim.setCustomer(model);
        if (vehicleClassId >= 0) {
            claim.getCustomer().setVehicleClass(this.vehicleClassService.getVehicleClass(vehicleClassId));
        }
        
        super.updateModel();
        replaceHashedStrings(model);
        return SUCCESS;
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
        
    public void replaceHashedStrings(Customer model) {
        // If claim has been re-opened after being hashed..
        if (model.getTitle() != null && model.getTitle().startsWith("~~")) model.setTitle(GDPR_REMOVED_STRING);
        if (model.getFirstName() != null && model.getFirstName().startsWith("~~")) model.setFirstName(GDPR_REMOVED_STRING);
        if (model.getLastName() != null && model.getLastName().startsWith("~~")) model.setLastName(GDPR_REMOVED_STRING);
        if (model.getAddress1() != null && model.getAddress1().startsWith("~~")) model.setAddress1(GDPR_REMOVED_STRING);
        if (model.getAddress2() != null && model.getAddress2().startsWith("~~")) model.setAddress2(GDPR_REMOVED_STRING);
        if (model.getAddress3() != null && model.getAddress3().startsWith("~~")) model.setAddress3(GDPR_REMOVED_STRING);
        if (model.getAddress4() != null && model.getAddress4().startsWith("~~")) model.setAddress4(GDPR_REMOVED_STRING);
        if (model.getAddress5() != null && model.getAddress5().startsWith("~~")) model.setAddress5(GDPR_REMOVED_STRING);
        if (model.getTelephoneDay() != null && model.getTelephoneDay().startsWith("~~")) model.setTelephoneDay(GDPR_REMOVED_STRING);
        if (model.getTelephoneEvening() != null && model.getTelephoneEvening().startsWith("~~")) model.setTelephoneEvening(GDPR_REMOVED_STRING);
        if (model.getEmail() != null && model.getEmail().startsWith("~~")) model.setEmail(GDPR_REMOVED_STRING);
        if (model.getOccupation() != null && model.getOccupation().startsWith("~~")) model.setOccupation(GDPR_REMOVED_STRING);
    }
}
