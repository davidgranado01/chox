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

        if (c.getHireMonitoringDetail() != null && c.getHireMonitoringDetail().getRepairBookInDate() != null
                && c.getCustomer() != null && c.getCustomer().getIsUsable() != null && c.getCustomer().getIsUsable()) {

            int day = DateHelper.getDay(c.getHireMonitoringDetail().getRepairBookInDate());
            switch (day) {
                case Calendar.FRIDAY:
                    dayString = "Friday";
                    bFlag = true;
                    break;
                case Calendar.SATURDAY:
                    dayString = "Saturday";
                    bFlag = true;
                    break;
                case Calendar.SUNDAY:
                    dayString = "Sunday";
                    bFlag = true;
                    break;
                default:
                    break;
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
