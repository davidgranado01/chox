package idas.chox.data.notifications;

import java.util.HashSet;
import java.util.Set;

import idas.chox.core.notifications.AnomalousCheck;

public enum NotificationType {

    EcdAnomalousNotification("EcdAnomalousNotification"),
    RepairBookedInDateAnomalousNotification("RepairBookedInDateAnomalousNotification"),
    EcdUpdatedNotification("EcdUpdatedNotification"),
    HireUpdatedNotification("HireUpdatedNotification"),
    LiabilityStatusUpdatedNotification("") {
        @Override
        public boolean isInsurerType() {

            return false;
        }
    },
    TotalLossAnomalousNotification("TotalLossAnomalousNotification");

    private String type;
    private static final Set insurerNotifications;
    private static final Set choNotifications;


    static {
        insurerNotifications = new HashSet();
        for (int i = 0; i < values().length; i++) {
            NotificationType type = values()[i];
            if (type.isInsurerType()) {
                insurerNotifications.add(type.toString());
            }
        }

        choNotifications = new HashSet();
        for (int i = 0; i < values().length; i++) {
            NotificationType array_element = values()[i];
            if (!array_element.isInsurerType()) {
                choNotifications.add(array_element.toString());
            }
        }
    }


    NotificationType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }


    public boolean isInsurerType() {
        return true;
    }


    public static Set getInsurerNotificationTypes() {
        return insurerNotifications;
    }


    public static Set getChoNotificationTypes() {
        return choNotifications;
    }


    public static NotificationType getNotificationType(String type) {
        return NotificationType.valueOf(type);
    }


    public static AnomalousCheck getAnomalyCheckerForNotification(String type) {
        NotificationType  notificationType;
        notificationType = NotificationType.valueOf(type);
        switch(notificationType) {
            case EcdAnomalousNotification:
                return new EcdAnomalousNotificationChecker();
            case RepairBookedInDateAnomalousNotification:
                return new RepairBookedInDateAnomalousNotificationChecker();
            case TotalLossAnomalousNotification:
                return new TotalLossAnomalousNotificationChecker();
            default:
                return null;
        }
    }
}
