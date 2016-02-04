package idas.chox.emailnotification.notifier;

import idas.chox.emailnotification.config.NotificationSettingsBean;

import java.util.Date;

public interface Notifier {
    public abstract void getAndProcessNotificationData(NotificationSettingsBean settings, Date dateFrom, Date dateTo);

}
