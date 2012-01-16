package idas.chox.web.scheduler;

import idas.chox.web.security.WebUserService;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.security.AuthenticationManager;

public class ReferenceUpdateJob extends QuartzJobBean {

	private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateJob.class);

	private ImapMailReceiver imapMailReceiver;
	private XlsFileParser xlsFileParser;
	private WebUserService userDetailsService;
	private AuthenticationManager authenticationManager;
	private InternetAddress internetAddress;
	private Properties props;

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");

			//XXX this will be removed once we will read this properties from web.xml
			internetAddress = new InternetAddress();
			internetAddress.setPersonal("erac.test123");
			internetAddress.setAddress("erac.test@gmail.com");

			imapMailReceiver = new ImapMailReceiver();
			imapMailReceiver.setProps(props);
			imapMailReceiver.setFrom(internetAddress);
			imapMailReceiver.setHost("imap.gmail.com");
			imapMailReceiver.setAuthenticationManager(authenticationManager);
			imapMailReceiver.setUserDetailsService(userDetailsService);

			List<InputStream> listOfAttachments = imapMailReceiver
					.receiveMailAttachments(true);
			if (listOfAttachments.size() != 0)
				readAndUpdateTheXlsDate(listOfAttachments);

			imapMailReceiver.clean();

		} catch (UnsupportedEncodingException e) {
			LOG.error("Mail password cannot be encoded. " + e);
		}
	}

	private void readAndUpdateTheXlsDate(List<InputStream> attachmets) {
		for (InputStream attachemt : attachmets) {
			//XXX for now update is done inside of xlsFileParser bean
			Map<String, String> xlsDataMap = xlsFileParser.readExcelFile(attachemt);
		}
	}

	public void setImapMailReciever(ImapMailReceiver imapMailReceiver) {
		this.imapMailReceiver = imapMailReceiver;
	}

	public WebUserService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(WebUserService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public XlsFileParser getXlsFileParser() {
		return xlsFileParser;
	}

	public void setXlsFileParser(XlsFileParser xlsFileParser) {
		this.xlsFileParser = xlsFileParser;
	}
}