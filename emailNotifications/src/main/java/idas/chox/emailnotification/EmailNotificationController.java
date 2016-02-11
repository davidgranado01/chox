package idas.chox.emailnotification;

import idas.chox.emailnotification.config.NotificationSettingsBean;
import idas.chox.emailnotification.config.NotificationType;
import idas.chox.emailnotification.notifier.ClaimAcknowledgedNotifier;
import idas.chox.emailnotification.notifier.ClaimClosedNotifier;
import idas.chox.emailnotification.notifier.InvoiceContestedNotifier;
import idas.chox.emailnotification.notifier.InvoicePaidNotifier;
import idas.chox.emailnotification.notifier.LiabilityUpdatedNotifier;
import idas.chox.emailnotification.notifier.Notifier;
import idas.chox.emailnotification.notifier.QuantumAgreedNotifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("notificationController")
public class EmailNotificationController {

    private static final String COMMENT = "#";
    protected static final String DATE_FORMAT = "yyyyMMdd";
    private Date dateFrom;
    private Date dateTo;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private @Autowired ClaimClosedNotifier claimClosedNotifier;
    private @Autowired ClaimAcknowledgedNotifier claimAcknowledgedNotifier;
    private @Autowired InvoiceContestedNotifier invoiceContestedNotifier;
    private @Autowired InvoicePaidNotifier invoicePaidNotifier;
    private @Autowired LiabilityUpdatedNotifier liabilityUpdatedNotifier;
    private @Autowired QuantumAgreedNotifier quantumAgreedNotifier;

    private Map<NotificationType, Notifier> notifiers = new HashMap<NotificationType, Notifier>();

    public void start(String settingsLocation, String lastrunDate, boolean enableEmails, boolean limit1) {
        logger.info("Starting manual email notifier.");
        List<NotificationSettingsBean> settings = this.loadSettingsFile(settingsLocation);
        this.registerNotifiers(limit1);
        dateFrom = this.obtainDateFrom(lastrunDate);
        dateTo = this.obtainDateTo();
        for (NotificationSettingsBean setting : settings) {
            this.process(setting, enableEmails);
        }
        logger.info("Finishing manual email notifier.");
    }

    public List<NotificationSettingsBean> loadSettingsFile(String settingsLocation) {
        List<NotificationSettingsBean> settings = new ArrayList<NotificationSettingsBean>();

        File settingsFile = new File(settingsLocation);

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(settingsFile)));

            String settingsString = null;

            while ((settingsString = in.readLine()) != null) {
                if (!settingsString.startsWith(COMMENT)) {
                    settings.add(new NotificationSettingsBean(settingsString));
                }
            }

        } catch (IOException e) {
            String message = String.format("Unable to read the settings file at %s", settingsLocation);
            throw new RuntimeException(message, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    String message = String.format("Unable to close input stream for %s", settingsLocation);
                    throw new RuntimeException(message, e);
                }
            }
        }
        return settings;
    }

    public void process(NotificationSettingsBean setting, boolean enableEmails) {
        Notifier notifier = notifiers.get(setting.getType());
        if (notifier == null) {
            logger.error(String.format("Unable to find Notifier for email type %s.", setting.getType()));
        } else {
            notifier.getAndProcessNotificationData(setting, dateFrom, dateTo, enableEmails);
        }
    }


    public Date obtainDateFrom(String dateFromString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        // Default to yesterday if no date provided
        if (dateFromString == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            return cal.getTime();
        }
        try {
            return dateFormat.parse(dateFromString);
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Unable to extract date (format yyyyMMdd) from %s", dateFromString), e);
        }
    }

    public Date obtainDateTo() {
        return new Date();
    }


    public void registerNotifiers(boolean limit1) {
        notifiers.put(NotificationType.CLOSED, claimClosedNotifier);
        notifiers.put(NotificationType.ACKNOWLEDGED, claimAcknowledgedNotifier);
        notifiers.put(NotificationType.CONTESTED, invoiceContestedNotifier);
        notifiers.put(NotificationType.PAID, invoicePaidNotifier);
        notifiers.put(NotificationType.LIABILITIED, liabilityUpdatedNotifier);
        notifiers.put(NotificationType.QUANTUMED, quantumAgreedNotifier);
        if (limit1) {
            Set<NotificationType> notifierKeys = notifiers.keySet();
            for (NotificationType key : notifierKeys) {
                Notifier notifier = notifiers.get(key);
                notifier.setLimit1(true);
            }
        }
    }

    public Map<NotificationType, Notifier> getNotifiers() {
        return notifiers;
    }

    protected void setNotifiers(Map<NotificationType, Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    protected void setDateFrom(Date from) {
        dateFrom = from;
    }

    protected void setDateTo(Date to) {
        dateTo = to;
    }

}
