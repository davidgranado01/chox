package idas.chox.web.scheduler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.core.model.SchedulerJob;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.services.PenaltyChargeService;
import idas.chox.core.services.SchedulerJobService;
import idas.chox.core.util.EmailHelper;

/**
 *
 * @author John
 */
public abstract class EmailSchedulerJob implements Scheduler, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSchedulerJob.class);
    protected static final String email_date_format = "dd MMMM yyyy";
    private MailSecurityAthenticator mailSecurityAthenticator;
    private XlsFileParser xlsFileParser;
    private MailUtil mailUtil;
    private String emailAccount;
    private String emailAccountPassword;
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;
    private SecurityInfoProvider securityInfoProvider;
    private Session session;
    private SessionFactory sessionFactory;
    private SchedulerJobService schedulerJobService;
    private InvoiceService invoiceService;
    private PenaltyChargeService penaltyChargeService;
    private ClaimService claimService;
    private String hostName;
    private ServerConfig serverConfig;
    private ApplicationContext applicationContext;
    
    protected abstract Map<Integer, List<String>> doJob(Map<Integer, List<String>> jobInput, String sender);
    
    protected abstract String buildMessage(String email, String subject, Map<Integer, List<String>> xlsDataMap);
    
    protected abstract List<SchedulerJob> getEmailSchedulerJobs();

    @Override
    public void execute() throws JobExecutionException {
        LOG.info("Calling Email Scheduler Job : '{}'.", getClass().getSimpleName());
        String sender = null;
        String loginUsername = null;
        String loginPassword = null;
        String emailSubject;
        ImapMailReceiver imapMailReceiver = getImapMailReceiver();
        
        try {
            handleHibernateTransactionIntricacies();
            
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(emailAccount);
            internetAddress.setPersonal(emailAccountPassword);
            imapMailReceiver.setFrom(internetAddress);
            
            LOG.info("{} has {} subjects.", getClass().getSimpleName(), getEmailSchedulerJobs().size());
            for (SchedulerJob schedulerJob : getEmailSchedulerJobs()) {

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
                List<Message> listOfmails = imapMailReceiver.receiveMailsWithSubject(emailSubject);
                LOG.debug("Total no of mails are {}.", listOfmails.size());
                
                for (Message message : listOfmails) {

                    sender = mailUtil.getSender(message);
                    if (mailSecurityAthenticator.isPrivilegedSender(schedulerJob.getPrivilegedUsers(), sender)) {
                        LOG.debug("Sender '{}' is in privileged user list.", sender);
                        loginUsername = schedulerJob.getLoginUserName();
                        loginPassword = schedulerJob.getLoginPassword();
                        mailSecurityAthenticator.authenticateSender(loginUsername, loginPassword);
                        LOG.debug("Mapped login user {} is authenticated for sender '{}'.", loginUsername, sender);
                        List<InputStream> attachmentStreams = imapMailReceiver.fetchAttachements(message, "xls");
                        Map<Integer, List<String>> xlsDataMap;
                        try {
                            if (attachmentStreams.size() > 0) {
                                for (InputStream attachemt : attachmentStreams) {
                                    xlsDataMap = xlsFileParser.readExcelFile(attachemt);
                                    Map<Integer, List<String>> resultMap = doJob(xlsDataMap, sender);
                                    String emailMessage = buildMessage(sender, emailSubject, resultMap);
                                    LOG.debug("Bcc receiver size is {}", Arrays.asList(schedulerJob.getBccReceivers().split(",")).size());
                                    sendMail(sender, schedulerJob.getBccReceivers(), "RE: " + emailSubject, emailMessage);
                                }
                            } else {
                                String emailMessage = buildMessage(sender, emailSubject, null);
                                LOG.info("Mail ({}) with sender ({}) has no attachments", emailSubject, sender);
                                sendMail(sender, schedulerJob.getBccReceivers(), "RE: " + emailSubject, emailMessage);
                            }
                        } catch (Exception ex) {
                            LOG.error("Exception thrown while processing {} from sender {} with subject '{}'\n",
                                    new Object[]{getClass().getSimpleName(), sender, emailSubject, ex});
                            sendMail(schedulerJob.getErrorMessageReceivers(), null, "Error parsing email '" + emailSubject + "'", ex.getMessage());
                        }
                    } else {
                        LOG.info("{} request received from unauthorised user {}.", getClass().getSimpleName(), sender);
                        sendMail(schedulerJob.getErrorMessageReceivers(), schedulerJob.getBccReceivers(),
                                hostName + '-' + getClass().getSimpleName() + " request received from unauthorised user",
                                emailSubject + " request received from unauthorised user '" + sender + "'. Allowed users are " + schedulerJob.getPrivilegedUsers());
                    }
                }
                LOG.info("{} with subject '{}' job finished.", getClass().getSimpleName(), emailSubject);
            }

        } catch (UnsupportedEncodingException e) {
            LOG.error("Mail password cannot be decoded: {} \n", e.getMessage(), e);
        } catch (AccessDeniedException e) {
            LOG.error("The user is not authorized to update {} for given user name {} and password {} \n", new Object[]{ getClass().getSimpleName(), loginUsername, loginPassword, e});
        } catch (Exception e) {
            LOG.error("An exception was thrown during {} update:  \n", getClass().getSimpleName(), e);
        } finally {
            imapMailReceiver.clean();
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
    
    public ImapMailReceiver getImapMailReceiver() {
        return this.applicationContext.getBean("imapMailReceiver", ImapMailReceiver.class);
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

    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public PenaltyChargeService getPenaltyChargeService() {
        return penaltyChargeService;
    }

    public void setPenaltyChargeService(PenaltyChargeService penaltyChargeService) {
        this.penaltyChargeService = penaltyChargeService;
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
