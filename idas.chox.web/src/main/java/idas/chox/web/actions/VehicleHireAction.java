package idas.chox.web.actions;

import idas.chox.core.hpi.*;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.LookupService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.ApplicationAccessibility;

public class VehicleHireAction extends ClaimModelAction<VehicleHire> {
    private static final Logger LOG = LoggerFactory.getLogger(VehicleHireAction.class);

    private LookupService lookupService;
    private int vehicleClassId;
    private String oldVRN;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public VehicleHire loadModel(){

        VehicleHire vehicleHire = claim.getVehicleHire();
        if (vehicleHire != null) {
            oldVRN = vehicleHire.getVehicleRegistration();
            return vehicleHire;
        }

        oldVRN = "";
        return new VehicleHire();

    }

    @Override
    public String updateModel() {
        VehicleClass vehicleClass = this.model.getVehicleClass();
        if (vehicleClass.getId() != vehicleClassId) {
            List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
            for (VehicleClass vClass : vehicleClasses)
                if (vClass.getId() == vehicleClassId) {
                    vehicleClass = vClass;
                    break;
            }
            model.setVehicleClass(vehicleClass);
        }
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
        claim.setVehicleHire(model);
        return super.updateModel();
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public int getVehicleClassId() {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }
  
    public List<VehicleClass> getVehicleClasses() {
        return this.lookupService.getVehicleClasses();
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
                model.setHireStart(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
