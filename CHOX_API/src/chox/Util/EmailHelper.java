package chox.Util;

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

public class EmailHelper {
    
    /*
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";
    private static final boolean SMTP_authetication = true;
    private static final String SMTP_authetication_user = "info@greenfinch.ie";
    private static final String SMTP_authetication_password="Passwurd99.";
    private static final String emailSubjectPrefix = "CHOX Support Email: ";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String EMAIL_FROM = "info@greenfinch.ie";
    */
    
    private static final String SMTP_HOST_NAME = "relay.blacknight.com";
    private static final String SMTP_PORT = "25";
    private static final boolean SMTP_authetication = false;
    private static final String SMTP_authetication_user = "";
    private static final String SMTP_authetication_password="";
    private static final String emailSubjectPrefix = "CHOX Support Email: ";
    private static final String SSL_FACTORY = "";
    private static final String EMAIL_FROM = "choxsupport@sherwoodcompliance.co.uk";
    
    private Authenticator getAuthenticator(final String userName, final String password){
        
        Authenticator authenticator = new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        return authenticator;
    }
    

    public boolean postMail(String subject, String message, String[] recipients) 
        throws MessagingException, UnsupportedEncodingException {
        
        boolean debug = false;
        
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", SMTP_authetication);
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        // props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        // props.put("mail.smtp.socketFactory.fallback", "false");
        
        Session session = null;
        
        if(SMTP_authetication){
            Authenticator authenticator = getAuthenticator(SMTP_authetication_user, SMTP_authetication_password);
            session = Session.getDefaultInstance(props, authenticator);
        }else{
            session = Session.getDefaultInstance(props);
        }
        
        session.setDebug(debug);
        
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(EMAIL_FROM);
        addressFrom.setPersonal("CHOX Support");
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(emailSubjectPrefix+subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
        
        return true;
        
    }
    
    /*
    public boolean postMail(String subject, String message, String[] recipients) 
        throws MessagingException, UnsupportedEncodingException {
        
        boolean debug = false;
        
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");
        
        Session session = null;
        
        if(SMTP_authetication){
            Authenticator authenticator = getAuthenticator(SMTP_authetication_user, SMTP_authetication_password);
            session = Session.getDefaultInstance(props, authenticator);
        }else{
            session = Session.getDefaultInstance(props);
        }
        
        session.setDebug(debug);
        
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(EMAIL_FROM);
        addressFrom.setPersonal("CHOX Support");
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(emailSubjectPrefix+subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
        
        return true;
        
    }
    */
}
