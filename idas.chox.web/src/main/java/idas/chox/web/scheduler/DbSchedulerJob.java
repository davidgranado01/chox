package idas.chox.web.scheduler;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.QueuedTicket;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.EmailHelper;

/**
 *
 * @author Seeni
 */
public abstract class DbSchedulerJob implements SchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(DbSchedulerJob.class);
    protected static final String email_date_format = "dd MMMM yyyy";
    private MailUtil mailUtil;
    private String bccReceivers;
    private String emailSubject;
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;
    private ClaimService claimService;
    private SecurityInfoProvider securityInfoProvider;
    private String updateUserName;
    private String updatePassword;
    private MailSecurityAthenticator mailSecurityAthenticator;
    

    public abstract Map<Integer, List<String>> doJob(List<QueuedTicket> queuedTickets);
        
    @Override
    public void execute() throws JobExecutionException {
        LOG.info("Calling DB Scheduler Job : '{}'.", getClass().getSimpleName());
// TODO: investigate why we cannot access properties directly - if we do this we get null values
        LOG.debug("Properties accessed directly : {}, {}, {}, {}, {}, {}, {}, {}", 
                    new Object[]{bccReceivers, emailSubject, smtpHostName, smtpPort, smtpEmailUser,
                            smtpEmailPassword, updateUserName, updatePassword});
        LOG.debug("Properties accessed using getters :{}, {}, {}, {}, {}, {}, {}, {}", 
                    new Object[]{getBccReceivers(), getEmailSubject(), getSmtpHostName(), getSmtpPort(),
                            getSmtpEmailUser(), getSmtpEmailPassword(), 
                            getUpdateUserName(),getUpdatePassword()});
        getMailSecurityAthenticator().authenticateSender(getUpdateUserName(), getUpdatePassword()); 
    }


    protected final void sendMail(String receiver, String bccReceiver, String subject, String emailMessage) {
        try {
            EmailHelper emailHelper = new EmailHelper(getSmtpHostName(), getSmtpPort(), getSmtpEmailUser(), getSmtpEmailPassword());
            if (!bccReceiver.isEmpty()) {
                emailHelper.postMail(subject, emailMessage, (String[]) getMailUtil().parseStringToList(receiver, ",").toArray(), (String[]) getMailUtil().parseStringToList(bccReceiver, ",").toArray());
            } else {
                emailHelper.postMail(subject, emailMessage, (String[]) getMailUtil().parseStringToList(receiver, ",").toArray());
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{getSmtpHostName(), getSmtpPort(), getSmtpEmailUser(), getSmtpEmailPassword(), e});
        } catch (MessagingException e) {
            LOG.error("Messaging Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{getSmtpHostName(), getSmtpPort(), getSmtpEmailUser(), getSmtpEmailPassword(), e});
        }
    }


    public String getBccReceivers() {
        return bccReceivers;
    }


    public void setBccReceivers(String bccReceivers) {
        this.bccReceivers = bccReceivers;
    }
    

    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public MailSecurityAthenticator getMailSecurityAthenticator() {
        return mailSecurityAthenticator;
    }

    public void setMailSecurityAthenticator(MailSecurityAthenticator mailSecurityAthenticator) {
        this.mailSecurityAthenticator = mailSecurityAthenticator;
    }

    public MailUtil getMailUtil() {
        return mailUtil;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    public String getSmtpEmailPassword() {
        return smtpEmailPassword;
    }

    public void setSmtpEmailPassword(String smtpEmailPassword) {
        this.smtpEmailPassword = smtpEmailPassword;
    }

    public String getSmtpEmailUser() {
        return smtpEmailUser;
    }

    public void setSmtpEmailUser(String smtpEmailUser) {
        this.smtpEmailUser = smtpEmailUser;
    }

    public String getSmtpHostName() {
        return smtpHostName;
    }

    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(String updatePassword) {
        this.updatePassword = updatePassword;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

}
