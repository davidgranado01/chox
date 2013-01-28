package idas.chox.data.notifications;

import idas.chox.core.model.Notification;

/**
 *
 * @author John
 */
public class RepairBookedInDateAnomalousNotification extends Notification {
    public RepairBookedInDateAnomalousNotification(String day) {
        super();

        setMessage("Repair booked in on " + day + " and the CHO's Customer's vehicle was driveable.");
        
    }
}
