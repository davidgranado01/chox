package idas.chox.web.actions;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.LookupService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.TabAccessibility;

/**
 *
 * @author John
 */
public class VehicleMonitoringHireAction extends ClaimModelAction<VehicleHire> {
    private static final Logger LOG = LoggerFactory.getLogger(VehicleMonitoringHireAction.class);

    private LookupService lookupService;
    private int vehicleClassMonitoringId;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public VehicleHire loadModel(){

        VehicleHire vehicleHire = claim.getVehicleHire();
        if (vehicleHire != null) {
            return vehicleHire;
        }
        return new VehicleHire();

    }

    @Override
    public String updateModel() {
        VehicleClass vehicleClass = this.model.getVehicleClass();
        if (vehicleClassMonitoringId > 0 && (vehicleClass == null || vehicleClass.getId() != vehicleClassMonitoringId)) {
            List<VehicleClass> vehicleClasses = this.lookupService.getVehicleClasses();
            for (VehicleClass vClass : vehicleClasses) {
                if (vClass.getId() == vehicleClassMonitoringId) {
                    vehicleClass = vClass;
                    break;
                }
            }
            model.setVehicleClass(vehicleClass);
        }
        claim.setVehicleHire(model);
        return super.updateModel();
    }

    public void setVehicleClassMonitoringId(int vehicleClassMonitoringId) {
        this.vehicleClassMonitoringId = vehicleClassMonitoringId;
    }

    public int getVehicleClassMonitoringId() {
        return this.model.getVehicleClass() != null ? vehicleClassMonitoringId = this.model.getVehicleClass().getId() : 0;
    }

    public List<VehicleClass> getVehicleClasses() {
        return this.lookupService.getVehicleClasses();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_HIRE_MONITORING;
    }

    public String getRentalStartTime() {
        return DateHelper.getTimeFormat().format(model.getHireStart());
    }

    public void setRentalStartTime(String time) {
        if (model != null && time.length() > 0) {
            try {
                Date a = model.getHireStart();
                Date b = DateHelper.getTimeFormat().parse(time);
                model.setHireStart(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                LOG.warn("Error setting rental start time to '{}'", time);
            }
        }
    }

}
