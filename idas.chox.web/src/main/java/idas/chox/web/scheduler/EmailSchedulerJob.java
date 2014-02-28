package idas.chox.web.scheduler;

import java.util.List;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.SchedulerJob;

/**
 *
 * @author John
 */
public abstract class EmailSchedulerJob extends SchedulerJobBase {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSchedulerJob.class);
    protected ImapMailReceiver imapMailReceiver;

    protected abstract void processEmail(Message message, String emailSubject, String sender, String bccReceivers) throws MessagingException;

    public void setImapMailReceiver(ImapMailReceiver imapMailReceiver) {
        this.imapMailReceiver = imapMailReceiver;
    }

    @Override
    protected void process(String emailSubject, SchedulerJob schedulerJob) throws MessagingException {
        String sender = null;
                List<Message> listOfmails = imapMailReceiver.receiveMailsWithSubject(emailSubject);
                LOG.debug("Total no of mails are {}.", listOfmails.size());
                try {
                for (Message message : listOfmails) {

                    sender = mailUtil.getSender(message);
                    if (mailSecurityAthenticator.isPrivilegedSender(schedulerJob.getPrivilegedUsers(), sender)) {
                        LOG.debug("Sender '{}' is in privileged user list.", sender);
                        try {
                            processEmail(message, emailSubject, sender, schedulerJob.getBccReceivers());
                        } catch (Exception ex) {
                            LOG.error("Exception thrown while processing {} from sender {} with subject '{}'\n",
                                    new Object[]{getClass().getSimpleName(), sender, emailSubject, ex});
                            sendMail(schedulerJob.getErrorMessageReceivers(), null, "Error parsing email '" + emailSubject + "'", ex.getMessage());
                        } finally {
                             message.setFlag(Flags.Flag.SEEN, true);
                        }
                    } else {
                        LOG.info("{} request received from unauthorised user {}.", getClass().getSimpleName(), sender);
                        sendMail(schedulerJob.getErrorMessageReceivers(), schedulerJob.getBccReceivers(),
                                "Email with subject '" + emailSubject + "' request received from unauthorised user",
                                emailSubject + " request received from unauthorised user '" + sender + "'. Allowed users are " + schedulerJob.getPrivilegedUsers());
                    }
                }
                } catch (MessagingException ex) {
                    throw ex;
                } finally {
                    imapMailReceiver.clean();
                }
     }
}
