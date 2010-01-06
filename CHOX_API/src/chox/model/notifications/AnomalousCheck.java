package chox.model.notifications;

import chox.model.Claim;
import chox.model.Notification;

public interface AnomalousCheck {
    boolean check(Claim c);
    Notification BuildNotification();
    boolean isRefreshRequired();
}
