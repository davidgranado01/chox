/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.notifications;

import chox.model.Notification;

/**
 *
 * @author emmanuel
 */
public class EcdAnomalousNotification extends Notification {
  
    public EcdAnomalousNotification()
    {
        super();
        setMessage("Significant Delay To Hire Duration Due To New ECD");
    }

}
