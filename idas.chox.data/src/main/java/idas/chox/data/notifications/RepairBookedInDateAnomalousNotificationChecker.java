package idas.chox.data.notifications;

import java.util.Calendar;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;
import idas.chox.core.notifications.AnomalousCheck;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author John
 */
public class RepairBookedInDateAnomalousNotificationChecker implements AnomalousCheck {
    private String dayString;
    
    @Override
    public boolean check(Claim c) {
        //Check repair book in date is either a Firday, Saturday or Sunday.
        boolean bFlag = false;

        if (c.getHireMonitoringDetail() != null) {

            if (c.getCustomer().getIsUsable()) {
                if (c.getHireMonitoringDetail().getRepairBookInDate() != null) {
                    int day = DateHelper.getDay(c.getHireMonitoringDetail().getRepairBookInDate());
                    if (day == Calendar.FRIDAY) {
                        dayString = "Friday";
                        bFlag = true;
                    } else if (day == Calendar.SATURDAY) {
                        dayString = "Saturday";
                        bFlag = true;
                    } else if (day == Calendar.SUNDAY) {
                        dayString = "Sunday";
                        bFlag = true;
                    }
                }
            }
        }

        return bFlag;

    }

    @Override
    public Notification buildNotification() {
        return new RepairBookedInDateAnomalousNotification(dayString);
    }

    @Override
    public boolean isRefreshRequired() {
        return true;
    }
   
}
