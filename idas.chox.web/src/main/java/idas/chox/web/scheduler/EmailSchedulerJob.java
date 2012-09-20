package idas.chox.web.scheduler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.core.model.EmailUpdateUser;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.util.EmailHelper;

/**
 *
 * @author John
 */
public abstract class EmailSchedulerJob implements SchedulerJob{

    private static final Logger LOG = LoggerFactory.getLogger(EmailSchedulerJob.class);
    private ImapMailReceiver imapMailReceiver;
    private XlsFileParser xlsFileParser;
    private String emailAccount;
    private String emailAccountPassword;
    private MailSecurityAthenticator mailSecurityAthenticator;
    private MailUtil mailUtil;
    private List<EmailUpdateUser> privilegedUsers;
    private String bccReceivers;
    private String emailSubject;
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;
    private String errorMessageReceivers;
    private ClaimService claimService;
    private InvoiceService invoiceService;
    private SecurityInfoProvider securityInfoProvider;
    protected static final String email_date_format = "dd MMMM yyyy";
    private boolean existingTransaction;
    private Session session;
    private SessionFactory sessionFactory;

    protected abstract Map<Integer, List<String>> doJob(Map<Integer, List<String>> jobInput, String sender);
    
    protected abstract String buildMessage(String email, String subject, Map<Integer, List<String>> xlsDataMap);

    @Override
    public void execute() throws JobExecutionException {
        String sender = null;
        try {
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(emailAccount);
            internetAddress.setPersonal(emailAccountPassword);

            imapMailReceiver.setFrom(internetAddress);

            List<Message> listOfmails = imapMailReceiver.receiveMailsWithAttacment(emailSubject);

            for (Message message : listOfmails) {
                sender = mailUtil.getSender(message);
                EmailUpdateUser schedulerPrivilegedUser = mailSecurityAthenticator.isPrivilegedSender(privilegedUsers, sender);
                if (schedulerPrivilegedUser != null) {
                    mailSecurityAthenticator.authenticateSender(schedulerPrivilegedUser.getUserName(), schedulerPrivilegedUser.getPassword());
                    List<InputStream> attachmentStreams = imapMailReceiver.fetchAttachements(message, "xls");
                    Map<Integer, List<String>> xlsDataMap = null;
                    try {
                        for (InputStream attachemt : attachmentStreams) {
                            xlsDataMap = xlsFileParser.readExcelFile(attachemt);
                            Map<Integer, List<String>> resultMap = doJob(xlsDataMap,sender);
                            String emailMessage = buildMessage(sender, emailSubject, resultMap);
                            sendMail(sender, bccReceivers, "RE: " + emailSubject, emailMessage);
                        }
                    } catch (Exception ex) {
                        LOG.error("Exception thrown processing scheduler job from sender {} with subject '{}'\n",
                                new Object[]{sender, emailSubject, ex});
                        sendMail(errorMessageReceivers,null, "Error parsing email '" + emailSubject + "'", ex.getMessage());
                    }
                } else {
                    sendMail(errorMessageReceivers, bccReceivers,
                            "Update request received from unauthorised user",
                            "Supplier reference update request received from unauthorised user '" + sender + "'");
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
    
    protected final void sendMail(String receiver, String bccReceiver, String subject, String emailMessage) {
        try {
            EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword);
            if (!bccReceiver.isEmpty()) {
                emailHelper.postMail(subject, emailMessage, (String[]) mailUtil.parseStringToList(receiver, ",").toArray(), (String[]) mailUtil.parseStringToList(bccReceiver, ",").toArray());
            } else {
                emailHelper.postMail(subject, emailMessage, (String[]) mailUtil.parseStringToList(receiver, ",").toArray());
            }
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

    public void setMailSecurityAthenticator(
            MailSecurityAthenticator mailSecurityAthenticator) {
        this.mailSecurityAthenticator = mailSecurityAthenticator;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setPrivilegedUsers(List<EmailUpdateUser> privilegedUsers) {
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

    public void setErrorMessageReceivers(String errorMessageReceivers) {
        this.errorMessageReceivers = errorMessageReceivers;
    }

    public String getBccReceivers() {
        return bccReceivers;
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

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }
    
    public void handleHibernateTransactionIntricacies() {
        session = SessionFactoryUtils.getSession(sessionFactory, true);
        existingTransaction = SessionFactoryUtils.isSessionTransactional(session, sessionFactory);
        if (existingTransaction) {
            LOG.info("Found thread-bound Session for Quartz job");
        } else {
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        }
    }

    public void releaseHibernateSessionConditionally() {
        if (existingTransaction) {
            LOG.info("Not closing pre-bound Hibernate Session after TransactionalQuartzTask");
        } else {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            SessionFactoryUtils.releaseSession(session, sessionFactory);
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
