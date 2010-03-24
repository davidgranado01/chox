package idas.chox.core.model;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum NotificationType {
	
	
	EcdAnomalousNotification("EcdAnomalousNotification"),
	EcdUpdatedNotification("EcdUpdatedNotification"),
	HireUpdatedNotification("HireUpdatedNotification"),
	RepairBookedInOnFridayNotification("RepairBookedInOnFridayNotification"),
	RepairBookedInOnSaturdayNotification("RepairBookedInOnSaturdayNotification"),
	RepairBookedInOnSundayNotification("RepairBookedInOnSundayNotification"),
	LiabilityStatusUpdatedNotification(""){
		@Override
		public boolean isInsurerType() {
			
			return false;
		}
	};
	
	private static final Logger LOG = LoggerFactory.getLogger(NotificationType.class);
	
	private static Set insurerNotifications;
	private static Set choNotifications;
	NotificationType(String type){
		
	}
	
	public boolean isInsurerType(){
		return true;
	}
	
	public static Set getInsurerNotificationTypes(){
		if ( insurerNotifications == null){
			insurerNotifications = new HashSet();
			for (int i = 0; i < values().length; i++) {
				NotificationType array_element = values()[i];
				if ( array_element.isInsurerType()){
					LOG.debug("Insurer Notification "+array_element.toString());
					insurerNotifications.add(array_element.toString());
				}
			}
			
		}
		return insurerNotifications;
	}
	
	public static Set getChoNotificationTypes(){
		if ( choNotifications == null){
			choNotifications = new HashSet();
			for (int i = 0; i < values().length; i++) {
				NotificationType array_element = values()[i];
				if ( ! array_element.isInsurerType()){
					LOG.debug("Cho Notification "+array_element.toString());
					choNotifications.add(array_element.toString());
				}
			}
			
		}
		return choNotifications;
	}
}
