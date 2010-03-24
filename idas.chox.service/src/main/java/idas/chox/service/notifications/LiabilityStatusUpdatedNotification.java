/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.notifications;

import java.text.SimpleDateFormat;
import java.util.Date;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;

/**
 *
 * @author emmanuel
 */
public class LiabilityStatusUpdatedNotification extends Notification {
	
	public static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
    public LiabilityStatusUpdatedNotification(Claim obj) {
        super();        
        String message = getLiabilityStatusUpdateNotificationMessage(obj); 
        setMessage(message);
    }
    
    public static String getLiabilityStatusUpdateNotificationMessage(Claim obj){
    	return "Liability Status Updated To '"+obj.getLiabilityStatus()+"' On " + format.format(new Date());
    }
    
}
