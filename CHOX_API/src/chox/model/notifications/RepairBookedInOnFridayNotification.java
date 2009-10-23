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
public class RepairBookedInOnFridayNotification extends Notification{

    public RepairBookedInOnFridayNotification() {
        super();
        setMessage("Repair Booked In On A Friday");
    }
}


