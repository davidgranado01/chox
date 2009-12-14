package idas.chox.service.notifications;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;
import idas.chox.core.notifications.AnomalousCheck;
import idas.chox.core.util.DateHelper;
import java.util.Calendar;

public class RepairBookedInOnSundayNotificationChecker implements AnomalousCheck {

    public boolean check(Claim c) {

        //Check reapair book in date is Sunday.
        boolean bFlag = false;

        if (c.getHireMonitoringDetail() != null) {

            if (!DateHelper.DateCompare(c.getHireMonitoringDetail().getRepairBookInDate(), c.getHireMonitoringDetail().getNotificationRepairBookInDate())) {
                if (c.getHireMonitoringDetail().getRepairBookInDate() != null) {
                    bFlag = DateHelper.getDay(c.getHireMonitoringDetail().getRepairBookInDate()) == Calendar.SUNDAY;
                }
            }
        }

        return bFlag;

    }

    public Notification BuildNotification() {
        return new RepairBookedInOnSundayNotification();
    }

    public boolean isRefreshRequired() {
        return true;
    }
}
