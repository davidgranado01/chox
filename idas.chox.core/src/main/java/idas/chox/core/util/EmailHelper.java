package idas.chox.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailHelper {
    private static final Logger LOG = LoggerFactory.getLogger(EmailHelper.class);

    private static final String EMAIL_SUBJECT_PREFIX = "CHOX Support Email: ";
    String smtpEmailUser;

    public EmailHelper(String smtpEmailUser) {
        this.smtpEmailUser = smtpEmailUser;
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
            props.put("mail.smtp.host", "localhost");

            Session session = Session.getInstance(props);

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
   
            if (subject.startsWith("CHOX Fraud Referral")) {
                msg.setSubject(subject);
            } else {
                msg.setSubject(EMAIL_SUBJECT_PREFIX + subject);
            }
            msg.setContent(message, "text/plain");
            Transport.send(msg);

        } catch (UnsupportedEncodingException| MessagingException ex) {
            LOG.warn("Error posting email with subject '{}': \n{}\n", subject, message, ex);
            throw ex;
        } catch (Exception ex) {
            LOG.warn("Exception posting email with subject '{}': \n{}\n", subject, message, ex);
            throw ex;
        }
    }

    public void setSmtpEmailUser(String SmtpEmailUser) {
        this.smtpEmailUser = SmtpEmailUser;
    }

}