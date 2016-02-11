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
import java.io.IOException;
import java.io.InputStream;
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

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private @Autowired ClaimClosedNotifier claimClosedNotifier;
    private @Autowired ClaimAcknowledgedNotifier claimAcknowledgedNotifier;
    private @Autowired InvoiceContestedNotifier invoiceContestedNotifier;
    private @Autowired InvoicePaidNotifier invoicePaidNotifier;
    private @Autowired LiabilityUpdatedNotifier liabilityUpdatedNotifier;
    private @Autowired QuantumAgreedNotifier quantumAgreedNotifier;

    private Map<NotificationType, Notifier> notifiers = new HashMap<>();

    public void start(String startDate, String endDate, boolean enableEmails, boolean limit1) {
        logger.info("Starting email notifier with parameters: startDate='{}', endDate='{}', enableEmails={}", new Object[]{startDate, endDate, enableEmails});
        List<NotificationSettingsBean> settings = this.loadSettingsFileFromClasspath();
        this.registerNotifiers(limit1);
        for (NotificationSettingsBean setting : settings) {
            this.process(setting, startDate, endDate, enableEmails);
        }
        logger.info("Finishing email notifier.");
    }

    public List<NotificationSettingsBean> loadSettingsFileFromClasspath() {
        List<NotificationSettingsBean> settings = new ArrayList<>();
        
        BufferedReader in = null;
        try {
            InputStream settingsStream = getClass().getResourceAsStream("/settings.txt");
            if (settingsStream == null) {
                logger.error("Cannot load resouce file from classpath");
                throw new RuntimeException("Cannot load resouce file from classpath");
            }
            in = new BufferedReader(new InputStreamReader(settingsStream));

            String settingsString;

            while ((settingsString = in.readLine()) != null) {
                if (!settingsString.startsWith(COMMENT)) {
                    settings.add(new NotificationSettingsBean(settingsString));
                }
            }

        } catch (IOException ex) {
            String message = String.format("Unable to read the settings file: {}", ex.getMessage());
            throw new RuntimeException(message, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    String message = String.format("Unable to close input stream: {}", ex.getMessage());
                    throw new RuntimeException(message, ex);
                }
            }
        }
        return settings;
    }

    public void process(NotificationSettingsBean setting, String startDate, String endDate, boolean enableEmails) {
        Notifier notifier = notifiers.get(setting.getType());
        if (notifier == null) {
            logger.error("Unable to find Notifier for email type {}.", setting.getType());
        } else {
            notifier.getAndProcessNotificationData(setting, startDate, endDate, enableEmails);
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

}
