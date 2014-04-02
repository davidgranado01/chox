package idas.chox.web.scheduler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public abstract class ExcelEmailSchedulerJob extends EmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelEmailSchedulerJob.class);
    private XlsFileParser xlsFileParser;

    protected abstract Map<Integer, List<String>> doJob(Map<Integer, List<String>> jobInput, String sender);
    protected abstract String buildMessage(String email, String subject, Map<Integer, List<String>> xlsDataMap);

    public void setXlsFileParser(XlsFileParser xlsFileParser) {
        this.xlsFileParser = xlsFileParser;
    }

    @Override
    public void processEmail(Message message, String emailSubject, String sender, String bccReceivers, boolean replyToSender) throws MessagingException {
        List<EmailAttachment> attachmentStreams = imapMailReceiver.fetchAttachments(message, "xls");
        Map<Integer, List<String>> xlsDataMap;
        if (attachmentStreams.size() > 0) {
            for (EmailAttachment attachment : attachmentStreams) {
                xlsDataMap = xlsFileParser.readExcelFile(attachment.getIs());
                Map<Integer, List<String>> resultMap = doJob(xlsDataMap, sender);
                String emailMessage = buildMessage(sender, emailSubject, resultMap);
                LOG.debug("Bcc receiver size is {}", Arrays.asList(bccReceivers.split(",")).size());
                if (replyToSender) {
                    sendMail(sender, bccReceivers, "RE: " + emailSubject, emailMessage);
                } else {
                    sendMail(bccReceivers, null, "RE: " + emailSubject, emailMessage);
                }
            }
        } else {
            String emailMessage = buildMessage(sender, emailSubject, null);
            LOG.info("Mail ({}) with sender ({}) has no attachments", emailSubject, sender);
            sendMail(sender, bccReceivers, "RE: " + emailSubject, emailMessage);
        }
    }
}
