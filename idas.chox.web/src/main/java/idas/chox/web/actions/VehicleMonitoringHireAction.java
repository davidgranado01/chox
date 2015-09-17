package idas.chox.web.actions;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.NotificationService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.notifications.HireVehicleUpdatedNotification;
import idas.chox.service.security.TabAccessibility;
import idas.chox.service.workflow.activities.ActivityEvent;

/**
 *
 * @author John
 */
public class VehicleMonitoringHireAction extends ClaimModelAction<VehicleHire> {
    private static final Logger LOG = LoggerFactory.getLogger(VehicleMonitoringHireAction.class);

    private LookupService lookupService;
    private NotificationService notificationService;
    private int vehicleClassMonitoringId;
    boolean updateInsurer;
    private Date currentHireStart;
    
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setUpdateInsurer(boolean updateInsurer) {
        this.updateInsurer = updateInsurer;
    }

    @Override
    public VehicleHire loadModel(){

        VehicleHire vehicleHire = claim.getVehicleHire();
        if (vehicleHire != null) {
            currentHireStart = vehicleHire.getHireStart();
            return vehicleHire;
        }
        return new VehicleHire();

    }

    @Override
    public String updateModel() {
        boolean isHireStartUpdate = false;
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
            if ((claim.getVehicleHire() == null && model.getRentalStart() != null)
                || (claim.getVehicleHire() != null && currentHireStart == null && model.getRentalStart() != null)) {
                isHireStartUpdate = true;
            }
            
            claim.setVehicleHire(model);
            String result = super.updateModel();

            activityEventGenerator.generate(claim, ActivityEvent.HIRE_VEHICLE_UPDATED_EVENT);

            if (updateInsurer) {
                notificationService.addNotification(claim, new HireVehicleUpdatedNotification());
            }
            if (isHireStartUpdate) {
                claimService.addOnHireTask(claim);
            }

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
