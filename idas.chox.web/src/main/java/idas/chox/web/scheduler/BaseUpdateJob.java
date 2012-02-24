package idas.chox.web.scheduler;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.util.EmailHelper;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

/**
 *
 * @author John
 */
public abstract class BaseUpdateJob {

    private static final Logger LOG = LoggerFactory.getLogger(BaseUpdateJob.class);
    private ImapMailReceiver imapMailReceiver;
    private XlsFileParser xlsFileParser;
    private String emailAccount;
    private String emailAccountPassword;
    private String updateUserName;
    private String updatePassword;
    private MailSecurityAthenticator mailSecurityAthenticator;
    private MailUtil mailUtil;
    private String privilegedUsers;
    private String bccReceivers;
    private String emailSubject;
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;

    protected abstract Map<Integer, List<String>> doJob(Map<Integer, List<String>> jobInput);

    protected abstract String buildMessage(String email, String subject, Map<Integer, List<String>> xlsDataMap);

    protected final void execute() throws JobExecutionException {
        String sender = null;
        try {
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(emailAccount);
            internetAddress.setPersonal(emailAccountPassword);

            imapMailReceiver.setFrom(internetAddress);

            List<Message> listOfmails = imapMailReceiver.receiveMailsWithAttacment(emailSubject);

            for (Message message : listOfmails) {
                sender = mailUtil.getSender(message);
                if (mailSecurityAthenticator.isPrivilegedSender(mailUtil.parseStringToList(privilegedUsers, ","), sender)) {
                    mailSecurityAthenticator.authenticateSender(updateUserName, updatePassword);
                    List<InputStream> attachmentStreams = imapMailReceiver.fetchAttachements(message, "xls");
                    Map<Integer, List<String>> xlsDataMap = null;
                    try {
                        for (InputStream attachemt : attachmentStreams) {
                            xlsDataMap = xlsFileParser.readExcelFile(attachemt);
                            Map<Integer, List<String>> resultMap = doJob(xlsDataMap);
                            sendMail(sender, "RE: " + emailSubject, resultMap, true);
                        }
                    } catch (Exception ex) {
                        LOG.error("Exception thrown processing scheduler job from sender {} with subject '{}'\n",
                                new Object[]{sender, emailSubject, ex});
                        sendMail((String)mailUtil.parseStringToList(bccReceivers, ",").toArray()[0],
                                "Error parsing email '" + emailSubject + "' - please see logs for details", null, false);
                        
                    }
                } else {
                    sendMail((String) mailUtil.parseStringToList(bccReceivers, ",").toArray()[0],
                            "Supplier reference update request received from unauthorised user '" + sender + "'",
                            null, false);
                }
            }

        } catch (UnsupportedEncodingException e) {
            LOG.error("Mail password cannot be decoded: {} \n", e.getMessage(), e);
        } catch (AccessDeniedException e) {
            LOG.error("The user is nor authorized to update cho_reference number: {} \n", e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("An exception was thrown during a scheduler reference update: {} \n", e.getMessage(), e);
        } finally {
            imapMailReceiver.clean();
        }
    }

    protected final void sendMail(String sender, String subject, Map<Integer, List<String>> xlsDataMap, boolean bcc) {
        try {
            EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword);
            String emailMessage = buildMessage(sender, emailSubject, xlsDataMap);
            if (bcc)
                emailHelper.postMail(subject, emailMessage, new String[]{sender}, (String[]) mailUtil.parseStringToList(bccReceivers, ",").toArray());
            else
                emailHelper.postMail(subject, emailMessage, new String[]{sender});
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        } catch (MessagingException e) {
            LOG.error("Messaging Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        }
    }

    public void setImapMailReceiver(ImapMailReceiver imapMailReceiver) {
        this.imapMailReceiver = imapMailReceiver;
    }

    public void setXlsFileParser(XlsFileParser xlsFileParser) {
        this.xlsFileParser = xlsFileParser;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public void setEmailAccountPassword(String emailAccountPassword) {
        this.emailAccountPassword = emailAccountPassword;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public void setUpdatePassword(String updatePassword) {
        this.updatePassword = updatePassword;
    }

    public void setMailSecurityAthenticator(
            MailSecurityAthenticator mailSecurityAthenticator) {
        this.mailSecurityAthenticator = mailSecurityAthenticator;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setPrivilegedUsers(String privilegedUsers) {
        this.privilegedUsers = privilegedUsers;
    }

    public void setBccReceivers(String bccReceivers) {
        this.bccReceivers = bccReceivers;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpEmailUser(String smtpEmailUser) {
        this.smtpEmailUser = smtpEmailUser;
    }

    public void setSmtpEmailPassword(String smtpEmailPassword) {
        this.smtpEmailPassword = smtpEmailPassword;
    }

}
