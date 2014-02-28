package idas.chox.web.scheduler;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.quartz.JobExecutionException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.core.model.SchedulerJob;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.SchedulerJobService;
import idas.chox.core.util.EmailHelper;

/**
 *
 * @author John
 */
public abstract class SchedulerJobBase implements Scheduler, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerJobBase.class);
    protected static final String email_date_format = "dd MMMM yyyy";
    protected MailSecurityAthenticator mailSecurityAthenticator;
    protected MailUtil mailUtil;
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;
    private SecurityInfoProvider securityInfoProvider;
    private Session session;
    private SessionFactory sessionFactory;
    private SchedulerJobService schedulerJobService;
    private String hostName;
    private ServerConfig serverConfig;
    private ApplicationContext applicationContext;
    
    protected abstract List<SchedulerJob> getSchedulerJobs();
    protected abstract void process(String emailSubject, SchedulerJob schedulerJob) throws MessagingException;

    @Override
    public void execute() throws JobExecutionException {
        LOG.info("Calling Scheduler Job : '{}'.", getClass().getSimpleName());
        String loginUsername = null;
        String loginPassword = null;
        String emailSubject;
        
        try {
            handleHibernateTransactionIntricacies();
                        
            LOG.info("{} has {} subjects.", getClass().getSimpleName(), getSchedulerJobs().size());
            for (SchedulerJob schedulerJob : getSchedulerJobs()) {

                // ADD PREFIX TO THE EMAIL SUBJECT IF THE APPLICATION DO NOT RUN ON PRODUCTION SERVER.
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
                loginUsername = schedulerJob.getLoginUserName();
                loginPassword = schedulerJob.getLoginPassword();
                mailSecurityAthenticator.authenticateSender(loginUsername, loginPassword);
                LOG.debug("Mapped login user {} is authenticated.", loginUsername);

                process(emailSubject, schedulerJob);
                LOG.info("{} with subject '{}' job finished.", getClass().getSimpleName(), emailSubject);
            }

        } catch (AccessDeniedException e) {
            LOG.error("The user is not authorized to update {} for given user name {} and password {} \n", new Object[]{ getClass().getSimpleName(), loginUsername, loginPassword, e});
        } catch (Exception e) {
            LOG.error("An exception was thrown during {} update:  \n", getClass().getSimpleName(), e);
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
            if (bccReceivers != null && bccReceivers.length > 0) {
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
    
    public void setMailSecurityAthenticator(
            MailSecurityAthenticator mailSecurityAthenticator) {
        this.mailSecurityAthenticator = mailSecurityAthenticator;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
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


    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    public SchedulerJobService getSchedulerJobService() {
        return schedulerJobService;
    }

    public void setSchedulerJobService(SchedulerJobService schedulerJobService) {
        this.schedulerJobService = schedulerJobService;
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

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.applicationContext = ac;
    }
   
}
