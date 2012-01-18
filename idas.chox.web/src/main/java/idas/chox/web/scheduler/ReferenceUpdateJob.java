package idas.chox.web.scheduler;

import idas.chox.core.services.ClaimService;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import org.hibernate.HibernateException;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ReferenceUpdateJob {

	private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateJob.class);

	private ImapMailReceiver imapMailReceiver;
	private XlsFileParser xlsFileParser;
	private ClaimService claimService;

	protected void execute() throws JobExecutionException {
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");

			// XXX this will be removed once we will read this properties from
			// web.xml
			InternetAddress internetAddress = new InternetAddress();
			internetAddress.setPersonal("erac.test123");
			internetAddress.setAddress("erac.test@gmail.com");

			imapMailReceiver.setProps(props);
			imapMailReceiver.setFrom(internetAddress);
			imapMailReceiver.setHost("imap.gmail.com");

			List<InputStream> listOfAttachments = imapMailReceiver
					.receiveMailAttachments(true);
			if (listOfAttachments != null && listOfAttachments.size() != 0) {
				readAndUpdateReferenceNumber(listOfAttachments);
				imapMailReceiver.clean();
			}

		} catch (UnsupportedEncodingException e) {
			LOG.error("Mail password cannot be encoded. " + e);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private void readAndUpdateReferenceNumber(List<InputStream> attachmets) {
		String referenceNumber = null;
		try {
			for (InputStream attachemt : attachmets) {
				Map<Integer, List<String>> xlsDataMap = xlsFileParser.readExcelFile(attachemt);
				Set<Integer> rowNumbers = xlsDataMap.keySet();
				// This is specific for the excel file with two columns and
				// first row is a header.
				// We don't do update on first line and we assume we will always
				// have only two columns.
				for (Integer row : rowNumbers) {
					// first row is header
					if (row.intValue() != 0) {
						List<String> cells = xlsDataMap.get(row);
						// this excel file should have only two columns and we
						// iterate only through those two
						String oldReference = cells.get(0).trim().toUpperCase();
						String newReferenve = cells.get(1).trim().toUpperCase();

						if (oldReference != null && !oldReference.equals("")) {
							referenceNumber = oldReference;
							claimService.updateChoReferenceNumber(oldReference, newReferenve);

						}
					}
				}
			}
		} catch (HibernateException e) {
			LOG.error("Can't update claim with cho_reference number: "
					+ referenceNumber + " " + e);
		}
	}

	public ImapMailReceiver getImapMailReceiver() {
		return imapMailReceiver;
	}

	public void setImapMailReceiver(ImapMailReceiver imapMailReceiver) {
		this.imapMailReceiver = imapMailReceiver;
	}

	public XlsFileParser getXlsFileParser() {
		return xlsFileParser;
	}

	public void setXlsFileParser(XlsFileParser xlsFileParser) {
		this.xlsFileParser = xlsFileParser;
	}

	public ClaimService getClaimService() {
		return claimService;
	}

	public void setClaimService(ClaimService claimService) {
		this.claimService = claimService;
	}

	
}