package idas.chox.web.scheduler;

import idas.chox.web.security.WebUserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
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
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.UserDetails;

public class ImapMailReceiver {

	private static final Logger LOG = LoggerFactory
			.getLogger(ImapMailReceiver.class);

	private WebUserService userDetailsService;
	private AuthenticationManager authenticationManager;
	private Properties props;
	private String host;
	private InternetAddress from;
	private Folder folder;
	private Store store;

	public List<InputStream> receiveMailAttachments(
			boolean receiveOnlyUseenMails) {

		List<InputStream> listOfAttachemnts = new ArrayList<InputStream>();

		if (props == null) {
			props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			LOG.info("Properties are not set. Properties will be set by default to 'imaps'");
		}
		try {
			Session session = Session.getDefaultInstance(props, null);
			store = session.getStore(); // .getStore("imaps");
			store.connect(host, from.getAddress(), from.getPersonal());
			folder = store.getFolder("INBOX");

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
					listOfAttachemnts = fetchAtacchements(message);

					if(!isAuthenticateSender(message)){
						message.setFlag(Flags.Flag.RECENT, true);
						return null;
					}

					// TODO Uncoment this before git push
					// message.setFlag(Flags.Flag.DELETED, true);
				}
			}
			// TODO set the log for retrieved mails

		} catch (NoSuchProviderException e) {
			LOG.error("Given mail properties are not corrent. " + e);
		} catch (MessagingException e) {
			LOG.error("Cannot make connecection to the given host. " + e);
		} catch (IOException e) {
			LOG.error("Cannot retrive attachemnts. " + e);
		}
		return listOfAttachemnts;
	}

	public List<InputStream> fetchAtacchements(Message message)
			throws IOException, MessagingException {

		Multipart mp = (Multipart) message.getContent();
		List<InputStream> listOfAttachements = new ArrayList<InputStream>();

		for (int i = 0, n = mp.getCount(); i < n; i++) {
			Part part = mp.getBodyPart(i);

			String fileName = part.getFileName();

			if (fileName != null && fileName.endsWith(".xls")) {
				listOfAttachements.add((InputStream) part.getInputStream());
			}
		}
		return listOfAttachements;
	}

	private boolean isAuthenticateSender(Message message) {
		try {
			Address[] senders = message.getFrom();

			// XXX this is just mock data - which will later be extracted from
			// address
			boolean isAuthorized = false;
			String sender = "admin@driveassist.com";
			String password = "C0mpliance";

			//XXX only for development purposes
			UserDetails webuser = userDetailsService.loadUserByUsername("vicky.sinclair3");
			GrantedAuthority[] grantedAuthorities = webuser.getAuthorities(); 
			for (GrantedAuthority ga : grantedAuthorities) {
				if(ga.getAuthority().equals("ROLE_CHO")){
					isAuthorized = true;
				}
	         }
			if(isAuthorized){
				
				Authentication authentication = new UsernamePasswordAuthenticationToken(
						sender, password);
				authentication = authenticationManager
						.authenticate(authentication);
				if (!authentication.isAuthenticated()) {
					LOG.error("This user is not authenticated.");
					return false;
				}
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				LOG.error("This user is not authenticated.");
				return false;
			}
		} catch (MessagingException e) {
			LOG.error("This user is not authenticated." + e);
		}
		return true;
	}

	public void clean() {
		try {
			folder.close(true);
			store.close();
		} catch (MessagingException e) {
			LOG.error("Cannot close the mail folder: " + e);
		}

	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public InternetAddress getFrom() {
		return from;
	}

	public void setFrom(InternetAddress from) {
		this.from = from;
	}

	public void setHost(String host) {
		this.host = host;
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

}
