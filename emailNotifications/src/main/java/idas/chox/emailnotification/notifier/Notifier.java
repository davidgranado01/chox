package idas.chox.emailnotification.notifier;

import idas.chox.emailnotification.config.NotificationSettingsBean;

public interface Notifier {
    public abstract int getAndProcessNotificationData(NotificationSettingsBean settings, String dateFrom, String dateTo, Boolean enableEmails);

    public abstract void setLimit1(boolean limit1);
}
