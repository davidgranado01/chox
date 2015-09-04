package idas.chox.web.scheduler;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.opencsv.CSVReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author John
 */
public abstract class CsvEmailSchedulerJob extends EmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(CsvEmailSchedulerJob.class);

    protected abstract List<String[]> doJob(List<String[]> jobInput, String sender);
    protected abstract String buildMessage(String email, String subject, List<String[]> xlsDataMap);


    @Override
    public void processEmail(Message message, String emailSubject, String sender, String bccReceivers, boolean replyToSender) throws MessagingException {
        CSVReader reader;
        LOG.debug("Getting attachment streams....");
        List<EmailAttachment> attachmentStreams;
        try {
            attachmentStreams = imapMailReceiver.fetchAttachments(message.getContent(), "csv");
        } catch (IOException ex) {
            LOG.warn("Error fetching attachment - cannot retrive attachment: {} ", ex.getMessage(), ex);
            return;
        }
        LOG.debug("Found {} csv attachment streams", attachmentStreams.size());
        Map<Integer, List<String>> xlsDataMap;
        if (attachmentStreams.size() > 0) {
            for (EmailAttachment attachment : attachmentStreams) {
                try {
                    LOG.info("Processing attachment stream");
                    reader = new CSVReader(new InputStreamReader(attachment.getIs(), "UTF-8"));
                    List<String[]> entries = reader.readAll();
                    LOG.info("We have {} entries - processing in doJob", entries.size());
                    List<String[]> resultMap = doJob(entries, sender);
                    LOG.info("resultMap has {} entries", resultMap.size());
                    String emailMessage = buildMessage(sender, emailSubject, resultMap);
                    LOG.debug("Bcc receiver size is {}", Arrays.asList(bccReceivers.split(",")).size());
                    if (replyToSender) {
                        LOG.info("Replying to sender");
                        sendMail(sender, bccReceivers, "RE: " + emailSubject, emailMessage);
                    } else {
                        LOG.info("Replying to bcc receivers");
                        sendMail(bccReceivers, null, "RE: " + emailSubject, emailMessage);
                    }
                } catch (Exception ex) {
                    LOG.error("Error reading CVS attachment file: {}", ex.getMessage());
                }
            }
        } else {
            String emailMessage = buildMessage(sender, emailSubject, null);
            LOG.info("Mail ({}) with sender ({}) has no attachments", emailSubject, sender);
            sendMail(sender, bccReceivers, "RE: " + emailSubject, emailMessage);
        }
    }
}
