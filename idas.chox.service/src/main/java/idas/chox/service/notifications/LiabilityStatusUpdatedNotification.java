/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.notifications;

import idas.chox.core.model.Notification;

/**
 *
 * @author emmanuel
 */
public class LiabilityStatusUpdatedNotification extends Notification {

    public LiabilityStatusUpdatedNotification() {
        super();
        setMessage("Liability Status Updated");
    }
}
