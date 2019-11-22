package idas.chox.web.scheduler;

import java.util.Arrays;
import java.util.List;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

import org.quartz.JobExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import idas.chox.core.services.GmailSchedulerJobService;
import idas.chox.core.util.GmailUtils;
import idas.chox.core.workflow.ScheduleActivity;
import idas.chox.service.workflow.ScheduleActivityFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 *
 * @author john
 */
@DisallowConcurrentExecution
public class GmailSchedulerJob implements Scheduler { // , ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(GmailSchedulerJob.class);
    private GmailUtils gmailUtils;
    private GmailSchedulerJobService gmailSchedulerJobService;
    private MailSecurityAthenticator mailSecurityAthenticator;
    private String hostName;
    private ServerConfig serverConfig;
    private boolean active;
    private Session session;
    private SessionFactory sessionFactory;
    private ScheduleActivityFactory scheduleActivityFactory;
    private Transaction hibernateTransaction;

    public void setScheduleActivityFactory(ScheduleActivityFactory scheduleActivityFactory) {
        this.scheduleActivityFactory = scheduleActivityFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setGmailUtils(GmailUtils gmailUtils) {
        this.gmailUtils = gmailUtils;
    }

    public void setGmailSchedulerJobService(GmailSchedulerJobService gmailSchedulerJobService) {
        this.gmailSchedulerJobService = gmailSchedulerJobService;
    }

    public void setMailSecurityAthenticator(MailSecurityAthenticator mailSecurityAthenticator) {
        this.mailSecurityAthenticator = mailSecurityAthenticator;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void execute() throws JobExecutionException {
        int count = 0;
        if (!active) {
            LOG.info("GMAIL not active");
            return;
        }

        // Determine email subject prefix, depending upon system and contect
        String emailSubjectPrefix = null;
        if (!hostName.equalsIgnoreCase("prod")) {
            emailSubjectPrefix = hostName + "-";
            if (!serverConfig.getServletContext().getContextPath().isEmpty()) {
                emailSubjectPrefix = emailSubjectPrefix + serverConfig.getServletContext().getContextPath().replace("/", "") + ":";
            }
        }
        LOG.debug("Using prefix '{}'", emailSubjectPrefix);

        List<Message> listOfmails = gmailUtils.getEmails(emailSubjectPrefix);
        LOG.info("Total no of unread mails{}: {}", emailSubjectPrefix == null ? "" : " with prefix '" + emailSubjectPrefix + "'", listOfmails.size());
        for (Message message : listOfmails) {
            LOG.debug("Processing message {}", ++count);
            try {
                String from = null, subject = null, fullsubject = null;
                // Get subject and sender from message headers
                List<MessagePartHeader> headers = message.getPayload().getHeaders();
                if (!headers.isEmpty()) {
                    for (MessagePartHeader header : headers) {
                        String name = header.getName();
                        switch (name) {
                            case "From":
                                from = header.getValue();
                                if (from.contains("<")) { // strip out actual email address
                                    from = from.substring(from.indexOf("<") + 1, from.indexOf(">", from.indexOf("<")));
                                }
                                break;
                            case "Subject":
                                fullsubject = header.getValue().trim();
                                break;
                            default:
                                break;
                        }
                    }
                }
                LOG.debug("Subject='{}', from='{}'", fullsubject, from);
                if (fullsubject == null || fullsubject.isEmpty()) {
                    LOG.error("Email from sender '{}' contains no subject.", from);
                    continue;
                }
                // Get details of Gmail Scheduler job from database on the email subject
                if (emailSubjectPrefix != null) {
                    subject = fullsubject.substring(emailSubjectPrefix.length()).trim();
                } else {
                    subject = fullsubject;
                }

                LOG.debug("Found unread message with subject '{}' from '{}' with prefix '{}'", new Object[]{subject, from, emailSubjectPrefix});

                // To get the matching job, we need to remove trailing characters from the subject.
                // All characters after the following strings (when present) should be removed: Request, Pack, Notification, Task
                String matchSubject;
                if (subject.toLowerCase().contains("attachment upload:")) {
                    matchSubject = subject.substring(0, subject.toLowerCase().indexOf("attachment upload:") + 17);
                } else if (subject.toLowerCase().contains("request")) {
                    matchSubject = subject.substring(0, subject.toLowerCase().indexOf("request") + 7);
                } else if (subject.toLowerCase().contains("pack")) {
                    matchSubject = subject.substring(0, subject.toLowerCase().indexOf("pack") + 4);
                } else if (subject.toLowerCase().contains("notification")) {
                    matchSubject = subject.substring(0, subject.toLowerCase().indexOf("notification") + 12);
                } else if (subject.toLowerCase().contains("task")) {
                    matchSubject = subject.substring(0, subject.toLowerCase().indexOf("task") + 4);
                } else {
                    matchSubject = subject;
                }

                idas.chox.core.model.GmailSchedulerJob job = gmailSchedulerJobService.getSchedulerJobs(matchSubject);

                if (job != null && job.isActive()) {
                    // Check sender is authorised
                    if (!mailSecurityAthenticator.isPrivilegedSender(job.getPrivilegedUsers(), from)) {
                        LOG.error("Sender '{}' is not authorised for email subject '{}'", from, matchSubject);
                        // Mark message as read - leave in INBOX
                        GmailUtils.modifyThread("me", message.getThreadId(), null, Arrays.asList("UNREAD"));
                        continue;
                    }
                    LOG.info("Found unread message with subject '{}' from approved sender '{}'{}", new Object[]{subject, from, emailSubjectPrefix == null ? "" : " (with prefix '" + emailSubjectPrefix + "')"});

                    //Ok, sender is authorised, so lets authenticate the user
                    try {
                        mailSecurityAthenticator.authenticateSender(job.getLoginUserName(), job.getLoginPassword());
                        LOG.debug("Mapped login user {} is authenticated.", job.getLoginUserName());

                    } catch (AccessDeniedException | AuthenticationException e) {
                        LOG.error("The user for scheduler job {} is not authenticated: username='{}', password='{}' \n", new Object[]{getClass().getSimpleName(), job.getLoginUserName(), job.getLoginPassword(), e});
                        continue;
                    } catch (Exception e) {
                        LOG.error("An exception was thrown during {} update: \n", getClass().getSimpleName(), e);
                        continue;
                    }

                    // Get details of the message: the content + any attachments
                    String emailContent = null;
                    List<MessagePart> messageParts = message.getPayload().getParts();
                    if (messageParts != null && !messageParts.isEmpty()) {
                        byte[] bytes = Base64.decodeBase64(messageParts.get(0).getBody().getData());
                        if (bytes != null) {
                            emailContent = new String(bytes);
                        }
                    }

                    List<idas.chox.core.model.EmailAttachment> attachments = gmailUtils.fetchAttachments(message);

                    /*
                     * Process message depending upon type (job_name)
                     * The following types are not email jobs: 
                     *   DB_REFERENCE_UPDATE, KEOGHS, CLAIM_MATCHING
                     *   TOTALLOSS_CHASE_TASK, PAID_INVOICES
                     */
                    ScheduleActivity activity = scheduleActivityFactory.getActivity(job.getJobName());
                    handleHibernateTransactionIntricacies(false);
                    LOG.info("Processing scheduler activity '{}'", activity.getClass().toGenericString());
                    boolean processed = activity.process(emailContent, attachments, from, fullsubject);
                    LOG.debug("Done processing scheduler activity '{}'", activity.getClass().toGenericString());
                    if (TransactionSynchronizationManager.isActualTransactionActive()) {
                        session.flush();
                        LOG.debug("Session flushed.");
                    }

                    // Mark message as read, remove from inbox and add correct label for processed message
                    // Note all emails with a prefix (i.e. not production) are given the label 'test-emails'
                    if (processed) {
                        List<String> labelsToAdd = null;
                        if (emailSubjectPrefix != null) {
                            labelsToAdd = Arrays.asList(GmailUtils.getLabelId("Test Emails"));
                        } else if (job.getProcessedLabel() != null) {
                            labelsToAdd = Arrays.asList(GmailUtils.getLabelId(job.getProcessedLabel()));
                        }
                        GmailUtils.modifyThread("me", message.getThreadId(), labelsToAdd, Arrays.asList("INBOX", "UNREAD"));

                        if (job.isReplyToSender()) {
                            GmailUtils.sendMessage(from, job.getBccReceivers(), "RE: " + fullsubject, activity.getResponse(fullsubject, from));
                        }
                    }
                } else {
                    if (job == null) {
                        LOG.warn("No matching active job found for subject '{}'", matchSubject);
                    } else {
                        LOG.warn("Job for subject '{}' is currently inactive.", matchSubject);
                    }
                }

            } catch (Exception ex) {
                LOG.error("Exception thrown processing gmail: {}", ex.getMessage(), ex);
            } finally {
                LOG.debug("Finished processing email- releasing session.");
                releaseHibernateSessionConditionally();
            }
        }

        if (listOfmails.size() > 0) {
            LOG.info("Finished processing emails.");
        }
    }

    private void handleHibernateTransactionIntricacies(boolean startTransaction) {
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException ex) {
            LOG.debug("Exception thrown getting current session: {}", ex.getMessage());
            session = sessionFactory.openSession();
            LOG.debug("Session created.");
        }
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));

        if (startTransaction && !TransactionSynchronizationManager.isActualTransactionActive()) {
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

    private void releaseHibernateSessionConditionally() {
        if (hibernateTransaction != null && hibernateTransaction.getStatus() == TransactionStatus.ACTIVE) {
            hibernateTransaction.commit();
            LOG.debug("Hibernate Transaction committed: {}", hibernateTransaction);
        } else if (hibernateTransaction != null) {
            LOG.debug("Hibernate Transaction status={}, ", hibernateTransaction.getStatus());
        } else {
            LOG.debug("Hibernate Transaction is null");
        }

        if (session != null) {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            session.clear();
            session.close();
            session = null;
        }
    }

}
