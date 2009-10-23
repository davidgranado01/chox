/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model.notifications;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.Notification;
import java.util.Calendar;

/**
 *
 * @author emmanuel
 */
public class RepairBookedInOnFridayNotification extends Notification implements AnomalousCheck {

    public RepairBookedInOnFridayNotification() {
        super();
        setMessage("Repair Booked In On A Friday");
    }

    public boolean check(Claim c) {

        //Check reapair book in date is Firday.
        boolean bFlag = false;

        if(c.getHireMonitoringDetail() != null){
            if(c.getHireMonitoringDetail().getRepairBookInDate() != null){
                bFlag = DateHelper.getDay(c.getHireMonitoringDetail().getRepairBookInDate()) == Calendar.FRIDAY;
            }
        }

        return bFlag;

    }
}
