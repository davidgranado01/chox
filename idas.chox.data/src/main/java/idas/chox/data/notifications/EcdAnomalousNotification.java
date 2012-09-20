package idas.chox.data.notifications;

import idas.chox.core.model.Notification;

/**
 *
 * @author emmanuel
 */
public class EcdAnomalousNotification extends Notification {

    public EcdAnomalousNotification() {
        super();
        setMessage("Significant Delay To Hire Duration Due To New ECD");
    }
}
