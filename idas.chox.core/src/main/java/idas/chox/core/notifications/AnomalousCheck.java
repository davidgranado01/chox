package idas.chox.core.notifications;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;

public interface AnomalousCheck {
    boolean check(Claim c);
    Notification BuildNotification();
    boolean isRefreshRequired();
}
