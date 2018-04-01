package idas.chox.web.actions;

import java.util.Arrays;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
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
            originalModel = (Customer) BeanUtils.cloneBean(returnCustomer);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            LOG.error("Exception cloning customer: {}", ex.getMessage(), ex);
        }
       if (claim.isHashed()) {
            if (returnCustomer.getTitle() != null && !returnCustomer.getTitle().isEmpty()) returnCustomer.setTitle(GDPR_REMOVED_STRING);
            if (returnCustomer.getFirstName() != null && !returnCustomer.getFirstName().isEmpty()) returnCustomer.setFirstName(GDPR_REMOVED_STRING);
            if (returnCustomer.getLastName() != null && !returnCustomer.getLastName().isEmpty()) returnCustomer.setLastName(GDPR_REMOVED_STRING);
            if (returnCustomer.getAddress1() != null && !returnCustomer.getAddress1().isEmpty()) returnCustomer.setAddress1(GDPR_REMOVED_STRING);
            if (returnCustomer.getAddress2() != null && !returnCustomer.getAddress2().isEmpty()) returnCustomer.setAddress2(GDPR_REMOVED_STRING);
            if (returnCustomer.getAddress3() != null && !returnCustomer.getAddress3().isEmpty()) returnCustomer.setAddress3(GDPR_REMOVED_STRING);
            if (returnCustomer.getAddress4() != null && !returnCustomer.getAddress4().isEmpty()) returnCustomer.setAddress4(GDPR_REMOVED_STRING);
            if (returnCustomer.getAddress5() != null && !returnCustomer.getAddress5().isEmpty()) returnCustomer.setAddress5(GDPR_REMOVED_STRING);
            if (returnCustomer.getTelephoneDay() != null && !returnCustomer.getTelephoneDay().isEmpty()) returnCustomer.setTelephoneDay(GDPR_REMOVED_STRING);
            if (returnCustomer.getTelephoneEvening() != null && !returnCustomer.getTelephoneEvening().isEmpty()) returnCustomer.setTelephoneEvening(GDPR_REMOVED_STRING);
            if (returnCustomer.getEmail() != null && !returnCustomer.getEmail().isEmpty()) returnCustomer.setEmail(GDPR_REMOVED_STRING);
            if (returnCustomer.getOccupation() != null && !returnCustomer.getOccupation().isEmpty()) returnCustomer.setOccupation(GDPR_REMOVED_STRING);
        }
       
        // If claim has been re-opened after being hashed..
        if (returnCustomer.getTitle() != null && returnCustomer.getTitle().startsWith("~~")) returnCustomer.setTitle(GDPR_REMOVED_STRING);
        if (returnCustomer.getFirstName() != null && returnCustomer.getFirstName().startsWith("~~")) returnCustomer.setFirstName(GDPR_REMOVED_STRING);
        if (returnCustomer.getLastName() != null && returnCustomer.getLastName().startsWith("~~")) returnCustomer.setLastName(GDPR_REMOVED_STRING);
        if (returnCustomer.getAddress1() != null && returnCustomer.getAddress1().startsWith("~~")) returnCustomer.setAddress1(GDPR_REMOVED_STRING);
        if (returnCustomer.getAddress2() != null && returnCustomer.getAddress2().startsWith("~~")) returnCustomer.setAddress2(GDPR_REMOVED_STRING);
        if (returnCustomer.getAddress3() != null && returnCustomer.getAddress3().startsWith("~~")) returnCustomer.setAddress3(GDPR_REMOVED_STRING);
        if (returnCustomer.getAddress4() != null && returnCustomer.getAddress4().startsWith("~~")) returnCustomer.setAddress4(GDPR_REMOVED_STRING);
        if (returnCustomer.getAddress5() != null && returnCustomer.getAddress5().startsWith("~~")) returnCustomer.setAddress5(GDPR_REMOVED_STRING);
        if (returnCustomer.getTelephoneDay() != null && returnCustomer.getTelephoneDay().startsWith("~~")) returnCustomer.setTelephoneDay(GDPR_REMOVED_STRING);
        if (returnCustomer.getTelephoneEvening() != null && returnCustomer.getTelephoneEvening().startsWith("~~")) returnCustomer.setTelephoneEvening(GDPR_REMOVED_STRING);
        if (returnCustomer.getEmail() != null && returnCustomer.getEmail().startsWith("~~")) returnCustomer.setEmail(GDPR_REMOVED_STRING);
        if (returnCustomer.getOccupation() != null && returnCustomer.getOccupation().startsWith("~~")) returnCustomer.setOccupation(GDPR_REMOVED_STRING);

        return returnCustomer;
    }

    @Override
    public String updateModel() {
        LOG.debug("Updating customer model: oldvrn={}, newvrn={}", oldVRN, model.getVehicleRegistration());

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
        if (claim.isHashed()) {
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
