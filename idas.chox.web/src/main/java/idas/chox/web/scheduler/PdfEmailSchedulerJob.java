package idas.chox.web.scheduler;

import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public abstract class PdfEmailSchedulerJob extends EmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(PdfEmailSchedulerJob.class);

    protected abstract String buildMessage(String sender, String emailSubject, String[] messages);
    protected abstract String doJob(EmailAttachment attachment, String sender);

    @Override
    public void processEmail(Message message, String emailSubject, String sender, String bccReceivers) throws MessagingException {
        List<EmailAttachment> attachmentStreams = imapMailReceiver.fetchAttachments(message, "pdf");
        if (attachmentStreams.size() > 0) {
            String[] statusMessages = new String[attachmentStreams.size()];
            int i = 0;
            for (EmailAttachment attachment : attachmentStreams) {
                statusMessages[i++] = doJob(attachment, sender);
            }
            String emailMessage = buildMessage(sender, emailSubject, statusMessages);
            LOG.debug("Bcc receiver size is {}", Arrays.asList(bccReceivers.split(",")).size());
            sendMail(sender, bccReceivers, "RE: " + emailSubject, emailMessage);
        } else {
            String emailMessage = buildMessage(sender, emailSubject, null);
            LOG.info("Mail ({}) with sender ({}) has no attachments", emailSubject, sender);
            sendMail(sender, bccReceivers, "RE: " + emailSubject, emailMessage);
        }
    }
}
