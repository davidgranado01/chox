package idas.chox.emailnotification.notifier;

import idas.chox.emailnotification.config.NotificationSettingsBean;
import idas.chox.emailnotification.util.EmailHelper;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.Transport;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.velocity.VelocityEngineUtils;

public abstract class AbstractNotifier implements Notifier {

    private @Value("${db.url}") String dbUrl;
    private @Value("${db.user}") String dbUser;
    private @Value("${db.password}") String dbPassword;

    private static final String ENCODING = "UTF-8";

    private @Autowired EmailHelper emailHelper;
    private @Autowired VelocityEngine velocityEngine;

    protected static final String DATE_FORMAT = "yyyy-MM-dd";
    protected boolean limit1 = false;
    protected Transport transport;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract void processRecord(ResultSet rs, List<String> recipients, Boolean enableEmails) throws SQLException;

    protected abstract String getQueryString();
    
    @Override
    public void getAndProcessNotificationData(NotificationSettingsBean settings, String dateFrom, String dateTo, Boolean enableEmails) {
        transport = emailHelper.getTransport();
        this.runReport(settings, getQueryString(), dateFrom, dateTo, enableEmails);
        emailHelper.closeTransport(transport);
        transport = null;
    }

    protected void generateAndSendEmail(String subject, String emailTemplate, Map<String, Object> data, String[] recipients) {
        String message = this.generateMessageText(emailTemplate, data);
        try {
            emailHelper.postMail(transport, subject, message, recipients);
        } catch (UnsupportedEncodingException | MessagingException e) {
            logger.error("Unable to send email with subject {}.", subject, e);
        }
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Unable to connect to CHOX database.", e);
        }
        return connection;
    }

    protected void runReport(NotificationSettingsBean settings, String reportQuery, String startDate, String endDate, boolean enableEmails) {

        // Restrict to records produced (if limit1 is set to TRUE)
        boolean maxRecordsShown = false;

        try {
            logger.debug("Extracting data for Manual Email Notifications, using this SQL:\n{}", reportQuery);
            Connection conn = this.getConnection();
            NamedParameterStatement stmt = new NamedParameterStatement(conn, reportQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt("insId", settings.getInsurerId());
            stmt.setInt("choId", settings.getChoId());
            stmt.setTimestamp("startDate", Timestamp.valueOf(startDate + " 00:00:00"));
            stmt.setTimestamp("endDate", Timestamp.valueOf(endDate + " 00:00:00"));
            ResultSet rs = stmt.executeQuery();
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            logger.info("The query for '{}' has recovered {} records.", this.getClass().getSimpleName(), rowcount);

            while (rs.next()) {
                if (!maxRecordsShown) {
                    processRecord(rs, settings.getEmailAddressses(), enableEmails);

                    if (limit1) {
                        // It limit records is switched on then set max records to true
                        maxRecordsShown = true;
                    }
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get Email Notification data from CHOX database.", e);
        }
    }

    private String generateMessageText(String template, Map<String, Object> model) {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, ENCODING, model);
    }


    protected void setEmailHelper(EmailHelper emailHelper) {
        this.emailHelper = emailHelper;
    }

    @Override
    public void setLimit1(boolean limit1) {
        this.limit1 = limit1;
    }

    protected void logEmail(String subject, String[] emailTo) {
        logger.info("SendingTo= {}, with subject = '{}'", Arrays.toString(emailTo), subject);
    }
}
