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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component("notificationController")
public class EmailNotificationController {

    private static final String COMMENT = "#";
    private static final String SETTINGS_LOCATION = "settings.txt";
    private static final String LAST_RUN_DATE_LOCATION = "lastrun.txt";
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

    public void start(boolean limit1) {
        List<NotificationSettingsBean> settings = this.loadSettingsFile();
        this.registerNotifiers(limit1);
        dateFrom = this.obtainDateFrom();
        dateTo = this.obtainDateTo();
        for (NotificationSettingsBean setting : settings) {
            this.process(setting);
        }
        this.updateDateFrom();
    }

    public List<NotificationSettingsBean> loadSettingsFile() {
        List<NotificationSettingsBean> settings = new ArrayList<NotificationSettingsBean>();

        ClassPathResource resource = new ClassPathResource(SETTINGS_LOCATION);

        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String settingsString = null;

            while ((settingsString = in.readLine()) != null) {
                if (!settingsString.startsWith(COMMENT)) {
                    settings.add(new NotificationSettingsBean(settingsString));
                }
            }

        } catch (IOException e) {
            String message = String.format("Unable to read the settings file at %s", SETTINGS_LOCATION);
            throw new RuntimeException(message, e);
        }
        return settings;
    }

    public void process(NotificationSettingsBean setting) {
        Notifier notifier = notifiers.get(setting.getType());
        if (notifier == null) {
            logger.error(String.format("Unable to find Notifier for email type %s.", setting.getType()));
        } else {
            notifier.getAndProcessNotificationData(setting, dateFrom, dateTo);
        }
    }

    public Date obtainDateFrom() {
        ClassPathResource resource = new ClassPathResource(LAST_RUN_DATE_LOCATION);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date dateInput = null;
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String dateString = null;

            while ((dateString = in.readLine()) != null) {
                dateInput = dateFormat.parse(dateString);
            }

        } catch (IOException | ParseException e) {
            String message = String.format("Unable to read the last_run settings file at %s", LAST_RUN_DATE_LOCATION);
            throw new RuntimeException(message, e);
        }

        return dateInput;

    }

    public Date obtainDateTo() {
        return new Date();
    }

    public void updateDateFrom() {
        String lastRunPath = new ClassPathResource(LAST_RUN_DATE_LOCATION).getPath();
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(lastRunPath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            printWriter.print(dateFormat.format(dateTo));
            printWriter.close();
        } catch (IOException e) {
            String message = String.format("Problem updating the %s file.", LAST_RUN_DATE_LOCATION);
            throw new RuntimeException(message, e);
        }
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
