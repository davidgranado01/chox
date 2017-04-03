package idas.chox.service.workflow.activities;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.services.NotificationService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.notifications.HireVehicleUpdatedNotification;
import idas.chox.events.BaseActivityEvent;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;


public class HireUpdate extends BaseActivity {
    static final Logger LOG = LoggerFactory.getLogger(HireUpdate.class);
    private VehicleClass vehicleClass;
    private NotificationService notificationService;
    private Date hireStartDate;
    private Date hireStartDateTime;
    private String hireStartTime;
    private boolean updateInsurer;
    private boolean isHireStartUpdate = false;

    
    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }
    
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    
    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public Date getHireStartDate() {
        return hireStartDate;
    }

    public void setHireStartDate(Date hireStartDate) {
        this.hireStartDate = hireStartDate;
    }

    public String getHireStartTime() {
        return hireStartTime;
    }

    public void setHireStartTime(String hireStartTime) {
        this.hireStartTime = hireStartTime;
    }

    public boolean isUpdateInsurer() {
        return updateInsurer;
    }

    public Date getHireStartDateTime() {
        return hireStartDateTime;
    }

    public void setUpdateInsurer(boolean updateInsurer) {
        this.updateInsurer = updateInsurer;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (hireStartDate == null) {
            LOG.warn("Hire Start Date is null. Can not update Hire Start.");
            throw new Exception("Hire Start Date is null. Can not update Hire Start.");
        }
        if (hireStartTime != null) {
            String hireStartTimeClean = Jsoup.clean(hireStartTime, Whitelist.basic());
            if (!hireStartTimeClean.equals(hireStartTime)) {
                LOG.warn("Hire Start Time contains forbidden content - possible XSS attack: {}", hireStartTime);
                throw new Exception("Hire Start Time contains forbidden content");
            }
        } 

        // Merge date and time
        if (hireStartTime != null) {
            try {
                Date time = DateHelper.getTimeFormat().parse(hireStartTime);
                hireStartDateTime = DateHelper.mergeTimeToDate(hireStartDate, time);
            } catch (Exception ex) {
                LOG.error("Exception thrown merging time into date: {}", hireStartDate, hireStartTime);
                throw new Exception("Error setting Hire Start date/time");
            }
        } else {
            hireStartDateTime = hireStartDate;
        }

    }

    @Override
    protected void doProcess(Claim claim) {

        VehicleHire vh = claim.getVehicleHire();
        if (vh == null) {
            vh = new VehicleHire();
            claim.setVehicleHire(vh);
            isHireStartUpdate = true;
        }else {
            if (vh.getRentalStart() == null) {
                isHireStartUpdate = true;
            }
            // If no original values then save current ones
            if (vh.getHireStartOriginal() == null && vh.getHireStart() != null) {
                vh.setHireStartOriginal(vh.getHireStart());
            }
            if (vh.getVehicleClassOriginal() == null && vh.getVehicleClass() != null) {
                vh.setVehicleClassOriginal(vh.getVehicleClass());
            }
        }

        vh.setHireStart(hireStartDateTime);
        if (vehicleClass != null) {
            vh.setVehicleClass(vehicleClass);
        }
        
    }

    @Override
    protected void afterProcess(Claim claim) {
        getDataService().save(claim);
//        activityEventGenerator.generate(claim, this);
        for (BaseActivityEvent event : activityEventGenerator.getEvents(claim, this)) {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        }
        if (isHireStartUpdate) {
            claimService.addOnHireTask(claim);
        }
        if (updateInsurer || claim.getInsurer().isAllowDefaultHMUpdates()) {
                notificationService.addNotification(claim, new HireVehicleUpdatedNotification());
        }
    }
}