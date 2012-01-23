package idas.chox.web.scheduler;

import java.util.Arrays;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReferenceUpdateJob.class);

	public List<String> parseStringToList(String sendersAsString, String delimiter) {
		String[] sendersArray = sendersAsString.split(delimiter);
		return Arrays.asList(sendersArray);
	}

	public String getSender(Message message) {
		String sender = null;
		Address[] addresses;
		try {
			addresses = message.getFrom();
			if (addresses.length >= 1) {
				InternetAddress address = (InternetAddress) addresses[0];
				sender = address.getAddress();
			}
		} catch (MessagingException e) {
			LOG.error("Cannot retrieve sender from given message. {}" ,e.getMessage());
		}
		return sender;
	}
	
}
