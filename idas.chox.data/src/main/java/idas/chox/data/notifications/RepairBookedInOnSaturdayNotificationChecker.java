package idas.chox.data.notifications;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;
import idas.chox.core.notifications.AnomalousCheck;
import idas.chox.core.util.DateHelper;
import java.util.Calendar;

public class RepairBookedInOnSaturdayNotificationChecker implements AnomalousCheck {

    public boolean check(Claim c) {

        //Check reapair book in date is Saturday.
        boolean bFlag = false;

        if (c.getHireMonitoringDetail() != null) {

            if (!DateHelper.DateCompare(c.getHireMonitoringDetail().getRepairBookInDate(), c.getHireMonitoringDetail().getNotificationRepairBookInDate()) && c.getCustomer().getIsUsable()) {
                if (c.getHireMonitoringDetail().getRepairBookInDate() != null) {
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
