package idas.chox.web.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MailSecurityAthenticator {

	private static final Logger LOG = LoggerFactory
			.getLogger(MailSecurityAthenticator.class);

	private AuthenticationManager authenticationManager;

	/**
	 * Returns true if sender is in the list of privileged users.
	 * @param List<String> listOfPrivilegedSenders
	 * @param String sender 
	 * @return
	 */
	public boolean isPrivilegedSender(List<String> listOfPrivilegedSenders,
			String sender) {
		for (String priviligedSender : listOfPrivilegedSenders) {
			if (priviligedSender.trim().equalsIgnoreCase(sender.trim())) {
                LOG.debug("Sender '{}' is authenticated.", sender);
				return true;
			}
		}
		LOG.debug("Email sender with address '{}' is not authenticated.", sender);
		return false;
	}

	/**
	 * Authenticates given user with spring security authentication.
	 * @param userName
	 * @param password
	 */
	public void authenticateSender(String userName, String password) {
		try {
			Authentication authentication = new UsernamePasswordAuthenticationToken(userName, password);
			authentication = authenticationManager.authenticate(authentication);
			if (!authentication.isAuthenticated()) {
				LOG.error("User '{}' with password '{}' is not authenticated. ", userName, password);
			}
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
		} catch (SecurityException se) {
			LOG.error("Exception authenticating sender '{}': ", userName, se);
		}
	}
	
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}
