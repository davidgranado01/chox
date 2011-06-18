/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.notifications;

import java.text.SimpleDateFormat;
import java.util.Date;

import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Notification;

/**
 *
 * @author emmanuel
 */
public class LiabilityStatusUpdatedNotification extends Notification {
		
    public LiabilityStatusUpdatedNotification(LiabilityStatus obj) {
        super();        
        String message = getLiabilityStatusUpdateNotificationMessage(obj); 
        setMessage(message);
    }
    
    public static String getLiabilityStatusUpdateNotificationMessage(LiabilityStatus obj){
    	return "Liability Status updated to '"+obj+"' On " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }
    
}
