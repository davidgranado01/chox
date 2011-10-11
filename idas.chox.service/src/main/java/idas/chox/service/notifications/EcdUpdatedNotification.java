package idas.chox.service.notifications;

import idas.chox.core.model.Notification;

/**
 *
 * @author emmanuel
 */
public class EcdUpdatedNotification extends Notification {

    public EcdUpdatedNotification() {
        super();
        setMessage("ECD Update");
    }
}
