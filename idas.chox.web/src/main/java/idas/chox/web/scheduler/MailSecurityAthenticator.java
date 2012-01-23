package idas.chox.web.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

public class MailSecurityAthenticator {

	private static final Logger LOG = LoggerFactory
			.getLogger(MailSecurityAthenticator.class);

	private AuthenticationManager authenticationManager;

	public boolean isPrivilegedSender(List<String> listOfPrivilegedSenders,
			String sender) {
		for (String priviligedSender : listOfPrivilegedSenders) {
			if (priviligedSender.trim().equalsIgnoreCase(sender.trim())) {
				return true;
			}
		}
		LOG.info("User with email {} is not authenticated.");
		return false;
	}

	public void authenticateSender(String userName, String password) {
		try {
			Authentication authentication = new UsernamePasswordAuthenticationToken(userName, password);
			authentication = authenticationManager.authenticate(authentication);
			if (!authentication.isAuthenticated()) {
				LOG.error("This user is not authenticated. ");
			}
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
		} catch (SecurityException se) {
			LOG.info("User {} is not authenticated: {}", userName, se.getMessage());
		}
	}
	
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}
