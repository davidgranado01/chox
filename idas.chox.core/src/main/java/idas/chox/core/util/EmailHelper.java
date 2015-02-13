package idas.chox.core.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailHelper {
    private static final Logger LOG = LoggerFactory.getLogger(EmailHelper.class);

    private static final String emailSubjectPrefix = "CHOX Support Email: ";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final boolean SMTP_authetication = true;
    private String SmtpHostName;
    String SmtpPort;
    String SmtpEmailUser;
    String SmtpEmailUserPassword;

    public EmailHelper(String smtpHostName, String smtpPort, String smtpEmailUser, String smtpEmailUserPassword) {
        this.SmtpHostName = smtpHostName;
        this.SmtpPort = smtpPort;
        this.SmtpEmailUser = smtpEmailUser;
        this.SmtpEmailUserPassword = smtpEmailUserPassword;
    }

    private Authenticator getAuthenticator(final String userName, final String password) {

        Authenticator authenticator = new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        return authenticator;
    }
    
    public void postMail(String subject, String message, String[] recipients) throws MessagingException, UnsupportedEncodingException {
        postMail(subject, message, recipients, new String[]{});
    }
    
    public void postMail(String subject, String message, String[] recipients, String[] bccRecipients) throws MessagingException, UnsupportedEncodingException {

        if (recipients == null) {
            LOG.debug("No recipients - not sending email.");
            if (bccRecipients != null) {
                LOG.warn("Email recipients empty but bcc recipients not: first bcc recipient is '{}'", bccRecipients[0]);
            }
            return;
        }
        try {

            Properties props = new Properties();
            props.put("mail.smtp.host", SmtpHostName);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "false");
            props.put("mail.smtp.port", SmtpPort);
            props.put("mail.smtp.socketFactory.port", SmtpPort);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");

            Session session;

            if (SMTP_authetication) {
                Authenticator authenticator = getAuthenticator(SmtpEmailUser, SmtpEmailUserPassword);
                session = Session.getInstance(props, authenticator);
            } else {
                session = Session.getInstance(props);
            }

            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(SmtpEmailUser);
            addressFrom.setPersonal("CHOX Support");
            msg.setFrom(addressFrom);

            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            
            for (String bccRecipient : bccRecipients) {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccRecipient));
            }
   
            msg.setSubject(emailSubjectPrefix + subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);

        } catch (Exception ex) {
            LOG.error("Error posting email with subject '{}': \n", subject, ex);
        }
    }

    public void setSmtpEmailUser(String SmtpEmailUser) {
        this.SmtpEmailUser = SmtpEmailUser;
    }

    public void setSmtpEmailUserPassword(String SmtpEmailUserPassword) {
        this.SmtpEmailUserPassword = SmtpEmailUserPassword;
    }

    public void setSmtpHostName(String SmtpHostName) {
        this.SmtpHostName = SmtpHostName;
    }

    public void setSmtpPort(String SmtpPort) {
        this.SmtpPort = SmtpPort;
    }
}