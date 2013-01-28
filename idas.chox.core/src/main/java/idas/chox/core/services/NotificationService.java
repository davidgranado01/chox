package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;

public interface NotificationService {
    public void addNotification(Claim claim, Notification notification);
    public void checkForAnomalies(Claim claim, String type)  throws Exception;

    public void removeAllNotifications(Integer claimId);
    public void removeAllInsurerNotifications(Integer claimId);
    public void removeAllCHONotifications(Integer claimId);
    public void removeNotificationById(Integer notificationId);

    public List<Notification> getNotifications(Integer claimId);

    public void acknowledgeAllInsurerNotifications(Integer claimId);
    public void acknowledgeAllCHONotifications(Integer claimId);
    public void acknowledgeNotificationById(Integer notificationId);

}
