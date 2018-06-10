package idas.chox.web.scheduler;

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
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.SchedulerJobService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.services.SecureDataService;

/**
 *
 * @author John
 */
public abstract class SchedulerJobBase implements Scheduler, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerJobBase.class);
    protected static final String EMAIL_DATE_FORMAT = "dd MMMM yyyy";
    protected static final String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";
    private static final String REG_TIME = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])?$";
    protected MailSecurityAthenticator mailSecurityAthenticator;
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
    protected abstract void process(SchedulerJob schedulerJob) throws MessagingException;

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
                LOG.debug("No active scheduler jobs of type '{}'", getClass().getSimpleName());
            } else {
                LOG.debug("Processing {} Scheduler jobs of type  type '{}'.", schedulerJobs.size(), getClass().getSimpleName());
            }
            for (SchedulerJob schedulerJob : schedulerJobs) {
                LOG.info("{} job started.", getClass().getSimpleName());
                loginUsername = schedulerJob.getLoginUserName();
                loginPassword = schedulerJob.getLoginPassword();
                try {
                    mailSecurityAthenticator.authenticateSender(loginUsername, loginPassword);
                    LOG.debug("Mapped login user {} is authenticated.", loginUsername);

                    process(schedulerJob);
                } catch (AccessDeniedException | AuthenticationException e) {
                    LOG.error("The user for scheduler job {} is not authenticated: username='{}', password='{}' \n", new Object[]{ getClass().getSimpleName(), loginUsername, loginPassword, e});
                } catch (Exception e) {
                    LOG.error("An exception was thrown during {} update:  \n", getClass().getSimpleName(), e);
                } finally {
                    LOG.info("{} job finished.", getClass().getSimpleName());
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown calling Scheduler Job '{}': {}", getClass().getSimpleName(), ex.getMessage(), ex);
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
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException ex) {
            LOG.debug("Exception thrown getting current session: {}", ex.getMessage());
            session = sessionFactory.openSession();
        }
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    public void releaseHibernateSessionConditionally() {
        if (hibernateTransaction!=null && !hibernateTransaction.wasCommitted() && hibernateTransaction.isActive()) {
            hibernateTransaction.commit();
            LOG.debug("Hibernate Transaction committed: {}", hibernateTransaction);
        } else if (hibernateTransaction != null) {
            LOG.debug("Hibernate Transaction wasCommitted={}, wasRolledBack={}", hibernateTransaction.wasCommitted(), hibernateTransaction.wasRolledBack());
        } else {
            LOG.debug("Hibernate Transaction is null");
        }
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        session.clear();
        session.close();
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
        sdf.setLenient(true);
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
