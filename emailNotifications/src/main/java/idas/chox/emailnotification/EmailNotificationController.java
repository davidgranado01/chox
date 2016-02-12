package idas.chox.emailnotification;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import idas.chox.emailnotification.config.NotificationSettingsBean;
import idas.chox.emailnotification.config.NotificationType;
import idas.chox.emailnotification.notifier.ClaimAcknowledgedNotifier;
import idas.chox.emailnotification.notifier.ClaimClosedNotifier;
import idas.chox.emailnotification.notifier.InvoiceContestedNotifier;
import idas.chox.emailnotification.notifier.InvoicePaidNotifier;
import idas.chox.emailnotification.notifier.LiabilityUpdatedNotifier;
import idas.chox.emailnotification.notifier.Notifier;
import idas.chox.emailnotification.notifier.QuantumAgreedNotifier;

@Component("notificationController")
public class EmailNotificationController {

    private static final String COMMENT = "#";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private @Autowired ClaimClosedNotifier claimClosedNotifier;
    private @Autowired ClaimAcknowledgedNotifier claimAcknowledgedNotifier;
    private @Autowired InvoiceContestedNotifier invoiceContestedNotifier;
    private @Autowired InvoicePaidNotifier invoicePaidNotifier;
    private @Autowired LiabilityUpdatedNotifier liabilityUpdatedNotifier;
    private @Autowired QuantumAgreedNotifier quantumAgreedNotifier;

    private Map<NotificationType, Notifier> notifiers = new HashMap<>();

    public void start(String startDate, String endDate, boolean enableEmails, boolean limit1, String settingsFile) {
        logger.info("Starting email notifier with parameters: startDate='{}', endDate='{}', enableEmails={}", new Object[]{startDate, endDate, enableEmails});
        List<NotificationSettingsBean> settings = this.loadSettingsFile(settingsFile);
        this.registerNotifiers(limit1);
        int noEmailsSent = 0;
        for (NotificationSettingsBean setting : settings) {
            noEmailsSent += process(setting, startDate, endDate, enableEmails);
            if (enableEmails && noEmailsSent % 50 == 0) {
                try {
                    logger.info("Sleeping for {} seconds", noEmailsSent);
                    Thread.sleep(noEmailsSent*1000); // Wait 1 second for each email sent
                } catch (InterruptedException e) {
                    logger.error("Sllep interrupted: %s", e.getMessage());
                }
            }
        }
        logger.info("Finishing email notifier - sent {} email", noEmailsSent);
    }

    private List<NotificationSettingsBean> loadSettingsFile(String settingsFile) {
        List<NotificationSettingsBean> settings = new ArrayList<>();
        
        BufferedReader in = null;
        try {
            if (settingsFile == null) {
                InputStream settingsStream = getClass().getResourceAsStream("/settings.txt");
                if (settingsStream == null) {
                    logger.error("Cannot load resouce file from classpath");
                    throw new RuntimeException("Cannot load resouce file (settings.txt) from classpath");
                }
                in = new BufferedReader(new InputStreamReader(settingsStream));
            } else {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(settingsFile)));
            }
            String settingsString;

            while ((settingsString = in.readLine()) != null) {
                if (!settingsString.startsWith(COMMENT)) {
                    settings.add(new NotificationSettingsBean(settingsString));
                }
            }

        } catch (IOException ex) {
            logger.error("Unable to read settings file '{}'", settingsFile);
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

    public int process(NotificationSettingsBean setting, String startDate, String endDate, boolean enableEmails) {
        Notifier notifier = notifiers.get(setting.getType());
        if (notifier == null) {
            logger.error("Unable to find Notifier for email type {}.", setting.getType());
        } else {
            return notifier.getAndProcessNotificationData(setting, startDate, endDate, enableEmails);
        }
        
        return 0;
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
