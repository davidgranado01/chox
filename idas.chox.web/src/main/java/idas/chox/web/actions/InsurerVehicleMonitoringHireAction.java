package idas.chox.web.actions;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.InsurerVehicleHire;
import idas.chox.core.services.LookupService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.TabAccessibility;
import idas.chox.service.workflow.activities.ActivityEvent;

/**
 *
 * @author John
 */
public class InsurerVehicleMonitoringHireAction extends ClaimModelAction<InsurerVehicleHire> {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerVehicleMonitoringHireAction.class);

    private LookupService lookupService;
    private int vehicleClassMonitoringId;
    
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public InsurerVehicleHire loadModel(){
        return claim.getInsurerVehicleHire() != null ? claim.getInsurerVehicleHire() : new InsurerVehicleHire();
    }

    @Override
    public String updateModel() {
        try {
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
            
            claim.setInsurerVehicleHire(model);
            String result = super.updateModel();

            activityEventGenerator.generate(claim, ActivityEvent.INSURER_HIRE_VEHICLE_UPDATED_EVENT);


            return result;
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
    }

    public void setVehicleClassMonitoringId(int vehicleClassMonitoringId) {
        this.vehicleClassMonitoringId = vehicleClassMonitoringId;
        LOG.debug("Vehicle Class Monitoring Id set as integer: {}", vehicleClassMonitoringId);
    }

    /*
     * String setter added as we are getting an occasional OGNL error.
     * Note that adding this means that the integer setter will not be called (from OGNL).
     * This is a temporary fix - the problem is that LookupItem is used to populate the dropdown
     * which returns (name, value) as (string, string).
     */
    public void setVehicleClassMonitoringId(String vehicleClassMonitoringIdString) throws Exception {
        if (vehicleClassMonitoringIdString != null && !vehicleClassMonitoringIdString.isEmpty()) {
            try {
                this.vehicleClassMonitoringId = new Integer(vehicleClassMonitoringIdString);
            } catch (NumberFormatException ex) {
                LOG.error("Cannot conver '{}' to integer", vehicleClassMonitoringIdString);
                throw new Exception("An Internal error occurred - please try agin. If this problem persists, please contact CHOX support.");
            }
        }
        LOG.debug("Vehicle Class Monitoring Id set from string: {}", vehicleClassMonitoringIdString);
    }

    public int getVehicleClassMonitoringId() {
        return model.getVehicleClass() != null ? model.getVehicleClass().getId() : 0;
    }

    public List<VehicleClass> getVehicleClasses() {
        return this.lookupService.getVehicleClasses();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_INSURER_HIRE_MONITORING;
    }

    public String getRentalStartTime() {
        return DateHelper.getTimeFormat().format(model.getRentalStart());
    }

    public void setRentalStartTime(String time) {
        if (model != null && time.length() > 0) {
            try {
                Date a = model.getRentalStart();
                Date b = DateHelper.getTimeFormat().parse(time);
                model.setRentalStart(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                LOG.warn("Error setting rental start time to '{}'", time);
            }
        }
    }

}
