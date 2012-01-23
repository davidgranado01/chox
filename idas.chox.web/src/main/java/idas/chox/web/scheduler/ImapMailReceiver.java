package idas.chox.web.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImapMailReceiver {

	private static final Logger LOG = LoggerFactory
			.getLogger(ImapMailReceiver.class);

	private Properties props;
	private String mailHost;
	private String mailStoreProtocol;
	private InternetAddress from;
	private Folder folder;
	private Store store;
	private String mailPort;
	private String mailFolder;
	
	public ImapMailReceiver(){
	}
	
	public ImapMailReceiver(Properties properties, InternetAddress internetAddress){
		this.props = properties;
		this.from = internetAddress;
	}

	public List<Message> receiveMailsWithAttacment() {

		List<Message> listOfMails = new ArrayList<Message>();

		if (props == null) {
			props = System.getProperties();
			props.setProperty("mail.imaps.port" , mailPort);
			props.setProperty("mail.store.protocol", mailStoreProtocol);
			LOG.info("Properties are not set. Properties will be set by default to: {}" , mailStoreProtocol);
		}
		try {
			Session session = Session.getDefaultInstance(props, null);
			store = session.getStore(); 
			store.connect(mailHost, from.getAddress(), from.getPersonal());
			folder = store.getFolder(mailFolder);

			if (folder == null || folder.getName() == null) {
				folder = store.getFolder("INBOX");
				LOG.info("Mail Folder is not set. Folder will be set to 'INBOX'.");
			}

			folder.open(Folder.READ_WRITE);

			Message[] messages = folder.getMessages();

			for (int j = messages.length - 1; j >= 0; j--) {
				Message message = messages[j];
				if (!message.isSet(Flags.Flag.SEEN)
						&& message.getContentType().contains("MIXED")) {
					listOfMails.add(message);
					// TODO Uncoment this before releaseaing into testing
					// message.setFlag(Flags.Flag.DELETED, true);
				}
			}

		} catch (NoSuchProviderException e) {
			LOG.error("Given mail properties are not correct. {} " , e.getMessage());
		} catch (MessagingException e) {
			LOG.error("Cannot make connecection to the given host. {} " , e.getMessage());
		} catch (Exception e) {
			LOG.error("Cannot retrive attachemnts. {} " , e.getMessage());
		}
		return listOfMails;
	}

	public List<InputStream> fetchAtacchements(Message message,
			String fileFormat) {

		List<InputStream> listOfAttachements = new ArrayList<InputStream>();
		try {
			Multipart mp = (Multipart) message.getContent();
			for (int i = 0, n = mp.getCount(); i < n; i++) {
				Part part = mp.getBodyPart(i);

				String fileName = part.getFileName();

				if (fileName != null && fileName.endsWith(fileFormat)) {
					listOfAttachements.add((InputStream) part.getInputStream());
				}
			}
		} catch (MessagingException e) {
			LOG.error("Cannot make connecection to the given host. {} ",
					e.getMessage());
		} catch (IOException e) {
			LOG.error("Cannot retrive attachemnts. {} ", e.getMessage());
		}
		return listOfAttachements;
	}
	
	public void clean() {
		try {
			if(folder.isOpen())
				folder.close(true);
			if(store.isConnected())
				store.close();
		} catch (MessagingException e) {
			LOG.error("Cannot close the mail folder: {} " , e.getMessage());
		}

	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setFrom(InternetAddress from) {
		this.from = from;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public void setMailStoreProtocol(String mailStoreProtocol) {
		this.mailStoreProtocol = mailStoreProtocol;
	}

	public InternetAddress getFrom() {
		return from;
	}

	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

	public void setMailFolder(String mailFolder) {
		this.mailFolder = mailFolder;
	}

}
