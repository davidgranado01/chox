
package chox.model.notifications;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.Notification;
import java.util.Calendar;

public class RepairBookedInOnSaturdayNotificationChecker implements AnomalousCheck {
    
    public boolean check(Claim c) {

        //Check reapair book in date is Saturday.
        boolean bFlag = false;

        if(c.getHireMonitoringDetail() != null){

            if(!DateHelper.DateCompare(c.getHireMonitoringDetail().getRepairBookInDate(), c.getHireMonitoringDetail().getNotificationRepairBookInDate())){
                if(c.getHireMonitoringDetail().getRepairBookInDate() != null){
                    bFlag = DateHelper.getDay(c.getHireMonitoringDetail().getRepairBookInDate()) == Calendar.SATURDAY;
                }
            }
        }

        return bFlag;

    }

    public Notification BuildNotification() {
        return new RepairBookedInOnSaturdayNotification();
    }

    public boolean isRefreshRequired() {
        return true;
    }
}
