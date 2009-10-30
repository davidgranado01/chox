
package chox.model.notifications;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.Notification;
import java.util.Calendar;

public class RepairBookedInOnFridayNotificationChecker implements AnomalousCheck {

    public boolean check(Claim c) {

        //Check reapair book in date is Firday.
        boolean bFlag = false;

        if(c.getHireMonitoringDetail() != null){

            // System.out.println("getRepairBookInDate:"+c.getHireMonitoringDetail().getRepairBookInDate());
            // System.out.println("getNotificationRepairBookInDate:"+c.getHireMonitoringDetail().getNotificationRepairBookInDate());
            // System.out.println(DateHelper.DateCompare(c.getHireMonitoringDetail().getRepairBookInDate(), c.getHireMonitoringDetail().getNotificationRepairBookInDate()));
            
            if(!DateHelper.DateCompare(c.getHireMonitoringDetail().getRepairBookInDate(), c.getHireMonitoringDetail().getNotificationRepairBookInDate())){
                if(c.getHireMonitoringDetail().getRepairBookInDate() != null){
                    bFlag = DateHelper.getDay(c.getHireMonitoringDetail().getRepairBookInDate()) == Calendar.FRIDAY;
                }
            }
        }

        return bFlag;

    }

    public Notification BuildNotification() {
        return new RepairBookedInOnFridayNotification();
    }

    public boolean isRefreshRequired() {
        return true;
    }
}
