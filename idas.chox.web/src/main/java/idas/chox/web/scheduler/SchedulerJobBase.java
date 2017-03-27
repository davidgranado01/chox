package idas.chox.web.scheduler;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.SchedulerJobService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.EmailHelper;
import idas.chox.data.services.SecureDataService;

/**
 *
 * @author John
 */
public abstract class SchedulerJobBase implements Scheduler, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerJobBase.class);
    protected static final String email_date_format = "dd MMMM yyyy";
    protected final String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";
    private final String REG_TIME = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])?$";
//    private final String REG_TIME = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$";
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
    private Transaction hibernateTransaction;
    protected ClaimService claimService;
    
    protected abstract List<SchedulerJob> getSchedulerJobs();
    protected abstract void process(String emailSubject, SchedulerJob schedulerJob) throws MessagingException;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }


    @Override
    public void execute() throws JobExecutionException {
        LOG.info("Calling Scheduler Job : '{}'.", getClass().getSimpleName());
        String loginUsername;
        String loginPassword;
        String emailSubject;
        
        try {
            List<SchedulerJob> schedulerJobs = getSchedulerJobs();
            if (schedulerJobs.isEmpty()) {
                LOG.info("No active scheduler jobs of type '{}'", getClass().getSimpleName());
            } else {
                LOG.info("Scheduler job type '{}' has {} distinct email subjects.", getClass().getSimpleName(), schedulerJobs.size());
            }
            for (SchedulerJob schedulerJob : schedulerJobs) {

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
                try {
                    mailSecurityAthenticator.authenticateSender(loginUsername, loginPassword);
                    LOG.debug("Mapped login user {} is authenticated.", loginUsername);

                    process(emailSubject, schedulerJob);
                } catch (AccessDeniedException | AuthenticationException e) {
                    LOG.error("The user for scheduler job {} is not authenticated: username='{}', password='{}' \n", new Object[]{ getClass().getSimpleName(), loginUsername, loginPassword, e});
                } catch (Exception e) {
                    LOG.error("An exception was thrown during {} update:  \n", getClass().getSimpleName(), e);
                } finally {
                    LOG.info("{} with subject '{}' job finished.", getClass().getSimpleName(), emailSubject);
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown calling Scheduler Job '{}': {}", getClass().getSimpleName(), ex.getMessage(), ex);
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

    protected BigDecimal validateNumeric(String valueString, StringBuilder statusString, String column) {
        BigDecimal value = null;
        
        if (valueString != null && !valueString.isEmpty()) {
            try {
                value = new BigDecimal(valueString);
            } catch (Exception ex) {
                statusString.append(" Invalid Format For '").append(column).append("'.");
            }
        }
        return value;
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
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            try {
                hibernateTransaction = session.beginTransaction();
                LOG.debug("Hibernate Transaction started: {}", hibernateTransaction);
            } catch (HibernateException ex) {
                LOG.error("Exception thrown starting hibernate transaction: {}\n", ex.getMessage(), ex);
            }
        } else {
            LOG.debug("Transaction already active: {}", TransactionSynchronizationManager.getCurrentTransactionName());
        }
    }

    public void releaseHibernateSessionConditionally() {
        if (hibernateTransaction!=null && !hibernateTransaction.wasCommitted() && hibernateTransaction.isActive()) {
            hibernateTransaction.commit();
            LOG.debug("Hibernate Transaction committed: {}", hibernateTransaction);
        } else {
            LOG.debug("Hibernate Transaction not committed: {}", hibernateTransaction);
            if (hibernateTransaction != null) {
                LOG.debug("Hibernate Transaction wasCommitted={}, wasRolledBack={}", hibernateTransaction.wasCommitted(), hibernateTransaction.wasRolledBack());
            }
        }
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        session.clear();
        SessionFactoryUtils.closeSession(session);
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

    protected Claim validateClaimReferenceNumber(String referenceNumber, StringBuilder statusString) {

        Claim claim = null;
        if (!regexExpressionChecker(REG_ALPHANUMERIC, referenceNumber)) {
            statusString.append(" No Claim Reference Provided.");
        } else {
            ((SecureDataService)claimService).setSecurityInfoProvider(((SecureDataService)claimService).getSecurityInfoProvider());
            claim = claimService.getClaimByCHOReferenceNumber(referenceNumber);

            if (claim == null) {
                LOG.debug("No Such Claim Reference {}", referenceNumber);
                statusString.append(" No Such Claim Reference.");
            }
        }
        return claim;
    }

    protected Date validateDate(String dateString, StringBuilder statusString, String columnName) {
        Date date = null;
        SimpleDateFormat sdf = DateHelper.getLocalDateFormat();
        sdf.setLenient(false);
        if (dateString.isEmpty()) {
            statusString.append(" No '").append(columnName).append("' provided.");
        } else if (dateString.length() != sdf.toPattern().length()) {
            statusString.append(" Invalid Format For '").append(columnName).append("'.");
        } else {
            try {
                date = sdf.parse(dateString);
            } catch (ParseException ex) {
                statusString.append(" Invalid Format For '").append(columnName).append("'.");
                LOG.warn("Parse exception thrown for column {}: {}", columnName, dateString);
            }
        }
        return date;
    }
    
    protected String validateTime(String timeString, StringBuilder statusString) {
        if (!regexExpressionChecker(REG_TIME, timeString)) {
            statusString.append("  Invalid Format for Hire Start (Time).");
        } 
        
        return timeString;
    }


    protected boolean regexExpressionChecker(String regex, String dataValue) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(dataValue);

        if (!m.find()) {
            LOG.debug("Invalid data for regex '{}': {}", regex, dataValue);
            return false;
        }
        return true;
    }
  
}
