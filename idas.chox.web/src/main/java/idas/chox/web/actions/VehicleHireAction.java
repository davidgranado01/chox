package idas.chox.web.actions;

import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.LookupService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.web.VehicleClassComparator;
import java.util.Collections;

public class VehicleHireAction extends ClaimModelAction<VehicleHire> {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleHireAction.class);
    private LookupService lookupService;
    private String oldVRN;

    public String getOldVRN() {
        return oldVRN;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public VehicleHire loadModel() {

        LOG.debug("VehicleHire loadModel is called");
        VehicleHire vehicleHire = claim.getVehicleHire();
        if (vehicleHire != null) {
            LOG.debug("VehicleHire loadModel is not null ");
            oldVRN = vehicleHire.getVehicleRegistration();
            LOG.debug("VehicleHire loadModel is not null and old vrn is set up");
            return vehicleHire;
        }

        oldVRN = "";
        LOG.debug("VehicleHire loadModel is null ");
        return new VehicleHire();

    }

    public boolean isTpiClaim() {
        return claim.isTpiClaim();
    }

//    @Override
    public String updateModel(Claim claim) {

        if (!oldVRN.equalsIgnoreCase(model.getVehicleRegistration())) {
            try {
                LOG.debug("VRN has changed - performing HPI check/retrieval");
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
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
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
        claim.setVehicleHire(model);
        LOG.debug("vehicleHire set in the claim ");
        return SUCCESS;
    }

    public void setVehicleClassId(int vehicleClassId) {
        setVehicleClassId_original(getVehicleClassId());

        if (model.getVehicleClass().getId() != vehicleClassId) {
            List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
            for (VehicleClass vClass : vehicleClasses) {
                if (vClass.getId() == vehicleClassId) {
                    model.setVehicleClass(vClass);
                    break;
                }
            }

        }
    }

    public int getVehicleClassId() {
        return this.model.getVehicleClass() != null ? this.model.getVehicleClass().getId() : 0;
    }

    public List<VehicleClass> getVehicleClasses() {
        List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
        Collections.sort(vehicleClasses, new VehicleClassComparator());

        return vehicleClasses;
    }

    public int getVehicleClassId_original() {
        return this.model.getVehicleClass_original() != null ? this.model.getVehicleClass_original().getId() : 0;
    }

    public String getVehicleClassName_original() {
        if (getVehicleClassId_original() != 0) {
            return lookupService.getVehicleClassName(getVehicleClassId_original());
        } else {
            return null;
        }

    }

    public String getVehicleClassName() {
        if (getVehicleClassId() != 0) {
            return lookupService.getVehicleClassName(getVehicleClassId());
        } else {
            return null;
        }

    }

    public void setVehicleClassId_original(int vehicleClassId) {

        if (vehicleClassId != getVehicleClassId_original() && getVehicleClassId_original() == 0) {

            List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
            for (VehicleClass vClass : vehicleClasses) {
                if (vClass.getId() == vehicleClassId) {
                    model.setVehicleClass_original(vClass);
                    break;
                }
            }
        }

    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }

    public String getRentalStartTime() {
        return DateHelper.TimeFormat.format(model.getHireStart());
    }

    public void setRentalStartTime(String time) {
        if (model != null) {
            try {
                Date a = model.getHireStart();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setHireStart(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getRentalEndTime() {
        return DateHelper.TimeFormat.format(model.getHireEnd());
    }

    public void setRentalEndTime(String time) {
        if (model != null) {
            try {
                Date a = model.getHireEnd();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setHireEnd(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public String getRentalStartTime_original() {
        return DateHelper.TimeFormat.format(model.getHireStart_original());
    }

    public void setRentalStartTime_original(String time) {
        if (model != null) {
            try {
                Date a = model.getHireStart_original();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setHireStart_original(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getRentalEndTime_original() {
        return DateHelper.TimeFormat.format(model.getHireEnd_original());
        
    }

    public void setRentalEndTime_original(String time) {
        if (model != null) {
            try {
                Date a = model.getHireEnd_original();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setHireEnd_original(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
