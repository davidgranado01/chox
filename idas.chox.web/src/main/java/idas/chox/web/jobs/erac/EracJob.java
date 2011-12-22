package idas.chox.web.jobs.erac;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import org.apache.poi.ss.usermodel.Cell;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EracJob extends QuartzJobBean {

	private static final Logger LOG = LoggerFactory.getLogger(EracJob.class);

	private ImapMailReceiver imapMailReceiver;
	private ParseXlsFile parseXlsFile;
	private InternetAddress internetAddress;
	private Properties props;

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			
			internetAddress = new InternetAddress();
			internetAddress.setPersonal("erac.test123");
			internetAddress.setAddress("erac.test@gmail.com");
			
			imapMailReceiver.setProps(props);
			imapMailReceiver.setFrom(internetAddress);
			imapMailReceiver.setHost("imap.gmail.com");

			Map<Message, List<InputStream>> mapOfAttachments = imapMailReceiver
					.receiveMailAttachments(true);
			for (Message message : mapOfAttachments.keySet()) {
				readAndUpdateTheXlsDate(message, mapOfAttachments.get(message));
			}

		} catch (UnsupportedEncodingException e) {
			LOG.error("Mail password cannot be encoded. " + e);
		}
	}

	private void readAndUpdateTheXlsDate(Message message,
			List<InputStream> attachmets) {
		for (InputStream attachemt : attachmets) {
			List<List<Cell>> cells = parseXlsFile.readExcelFile(attachemt);
			parseXlsFile.iterateThroughTheXlsFile(cells);

		}
	}

	public void setImapMailReciever(ImapMailReceiver imapMailReceiver) {
		this.imapMailReceiver = imapMailReceiver;
	}

	public void setParseXlsFile(ParseXlsFile parseXlsFile) {
		this.parseXlsFile = parseXlsFile;
	}
}