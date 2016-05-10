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
    private String smtpHostName;
    String smtpPort;
    String smtpEmailUser;
    String smtpEmailUserPassword;

    public EmailHelper(String smtpHostName, String smtpPort, String smtpEmailUser, String smtpEmailUserPassword) {
        this.smtpHostName = smtpHostName;
        this.smtpPort = smtpPort;
        this.smtpEmailUser = smtpEmailUser;
        this.smtpEmailUserPassword = smtpEmailUserPassword;
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
            props.put("mail.smtp.host", smtpHostName);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "false");
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.smtp.socketFactory.port", smtpPort);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");

            Session session;

            if (SMTP_authetication) {
                Authenticator authenticator = getAuthenticator(smtpEmailUser, smtpEmailUserPassword);
                session = Session.getInstance(props, authenticator);
            } else {
                session = Session.getInstance(props);
            }

            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(smtpEmailUser);
            addressFrom.setPersonal("CHOX Support");
            msg.setFrom(addressFrom);

            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
                LOG.debug("Recipient {} added: '{}'", i, recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            
            for (String bccRecipient : bccRecipients) {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccRecipient));
                LOG.debug("BCC Recipient added: {}", bccRecipient);
            }
   
            msg.setSubject(emailSubjectPrefix + subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);

        } catch (UnsupportedEncodingException | MessagingException ex) {
            LOG.warn("Error posting email with subject '{}': \n{}\n", subject, message, ex);
            throw ex;
        }
    }

    public void setSmtpEmailUser(String SmtpEmailUser) {
        this.smtpEmailUser = SmtpEmailUser;
    }

    public void setSmtpEmailUserPassword(String SmtpEmailUserPassword) {
        this.smtpEmailUserPassword = SmtpEmailUserPassword;
    }

    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }

    public void setSmtpPort(String SmtpPort) {
        this.smtpPort = SmtpPort;
    }
}