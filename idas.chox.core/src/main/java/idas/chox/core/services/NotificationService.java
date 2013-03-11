package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;

public interface NotificationService {
    void addNotification(Claim claim, Notification notification);
    void checkForAnomalies(Claim claim, String type)  throws Exception;

    void removeAllNotifications(Integer claimId);
    void removeAllInsurerNotifications(Integer claimId);
    void removeAllCHONotifications(Integer claimId);
    void removeNotificationById(Integer notificationId);

    List<Notification> getNotifications(Integer claimId);

    void acknowledgeAllInsurerNotifications(Integer claimId);
    void acknowledgeAllCHONotifications(Integer claimId);
    void acknowledgeNotificationById(Integer notificationId);

}
