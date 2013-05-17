package idas.chox.web.scheduler;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.SchedulerJob;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.SchedulerJobService;
import idas.chox.core.util.EmailHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author Seeni
 */
public abstract class DbSchedulerJob implements Scheduler, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(DbSchedulerJob.class);
    protected static final String email_date_format = "dd MMMM yyyy";
    private MailUtil mailUtil;
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;
    private ClaimService claimService;
    private SecurityInfoProvider securityInfoProvider;
    private MailSecurityAthenticator mailSecurityAthenticator;
    private SchedulerJobService schedulerJobService;
    private String hostName;
    private ServerConfig serverConfig;
    private Session session;
    private SessionFactory sessionFactory;
    private ApplicationContext applicationContext;


    public abstract Map<Integer, List<String>> doJob();
    
    protected abstract List<SchedulerJob> getDBSchedulerJobs();
    
    protected abstract String buildMessage(String subject, Map<Integer, List<String>> xlsDataMap);
        
    @Override
    public void execute() throws JobExecutionException {
        
        String loginUsername = null;
        String loginPassword = null;
        String emailSubject;
        
        try {
            handleHibernateTransactionIntricacies();
            LOG.info("Calling DB Scheduler Job : '{}'.", getClass().getSimpleName());
            // TODO: investigate why we cannot access properties directly - if we do this we get null values
            LOG.debug("Properties accessed directly : {}, {}, {}, {}, {}",
                new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword});
            LOG.debug("Properties accessed using getters :{}, {}, {}, {}, {}, {}",
                new Object[]{getSmtpHostName(), getSmtpPort(), getSmtpEmailUser(), getSmtpEmailPassword()});
            LOG.info("{} has '{}' subjects.", getClass().getSimpleName(), getDBSchedulerJobs().size());
            for (SchedulerJob schedulerJob : getDBSchedulerJobs()) {
                
                if (!hostName.equalsIgnoreCase("PRODUCTION")) {
                    String emailSubjectPrefix = hostName + "-";
                    if (!serverConfig.getServletContext().getContextPath().isEmpty()) {
                        emailSubjectPrefix = emailSubjectPrefix + serverConfig.getServletContext().getContextPath().replace("/", "") + ":";
                    }
                    emailSubject = emailSubjectPrefix + schedulerJob.getEmailSubject();
                } else {
                    emailSubject = schedulerJob.getEmailSubject();
                }
                
                LOG.info("{} with subject '{}' job started.", getClass().getSimpleName(), emailSubject);
                LOG.debug("login user name is : {} for {} job.", schedulerJob.getLoginUserName(), getClass().getSimpleName());
                loginUsername = schedulerJob.getLoginUserName();
                loginPassword = schedulerJob.getLoginPassword();
                getMailSecurityAthenticator().authenticateSender(loginUsername, loginPassword);
                Map<Integer, List<String>> resultMap = doJob();
                String emailMessage = buildMessage(emailSubject, resultMap);
                sendMail(schedulerJob.getPrivilegedUsers(), schedulerJob.getBccReceivers(), emailSubject, emailMessage);
                LOG.info("{} with subject '{}' job finished.", getClass().getSimpleName(), emailSubject);
                
            }
        } catch (AccessDeniedException e) {
            LOG.error("The user is not authorized to update {} for given user name {} and password {} \n", new Object[]{ getClass().getSimpleName(), loginUsername, loginPassword, e});
        } catch (Exception e) {
            LOG.error("An exception was thrown during a {} update: ", getClass().getSimpleName(), e);
        } finally {
            releaseHibernateSessionConditionally();
        }
    }


    protected final void sendMail(String receiver, String bccReceiver, String subject, String emailMessage) {
        String[] receivers = receiver != null ? receiver.split(",") : null;
        String[] bccReceivers = bccReceiver != null ? bccReceiver.split(",") : null;
        LOG.debug("sending mails to receivers {} and bccreceivers {} ", receivers, bccReceivers);
        try {
            EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword);
            if (bccReceiver != null && bccReceivers.length > 0) {
                emailHelper.postMail(subject, emailMessage, receivers, bccReceivers);
            } else {
                emailHelper.postMail(subject, emailMessage, receivers);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        } catch (MessagingException e) {
            LOG.error("Messaging Exception thrown sending email with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        }
    }


    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
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

    public SchedulerJobService getSchedulerJobService() {
        return schedulerJobService;
    }

    public void setSchedulerJobService(SchedulerJobService schedulerJobService) {
        this.schedulerJobService = schedulerJobService;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
    public void handleHibernateTransactionIntricacies() {
        session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    public void releaseHibernateSessionConditionally() {
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.releaseSession(session, sessionFactory);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.applicationContext = ac;
    }

}
