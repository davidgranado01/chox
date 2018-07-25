package idas.chox.web.ws;

import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.UsernameTokenValidator;
import org.apache.wss4j.dom.message.token.UsernameToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author John
 */
public class WSTokenValidator extends UsernameTokenValidator {

    static final Logger LOG = LoggerFactory.getLogger(WSTokenValidator.class);

    private AuthenticationManager authenticationManager;

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    protected void verifyPlaintextPassword(UsernameToken usernameToken,
            RequestData data) throws WSSecurityException {
        LOG.debug("Verifying plain text password......");
        verifyDigestPassword(usernameToken, data);
    }

    @Override
    protected void verifyDigestPassword(UsernameToken usernameToken,
            RequestData data) throws WSSecurityException {
        LOG.debug("Verifying digest password......");

        String user = usernameToken.getName();
        String password = usernameToken.getPassword();
        LOG.debug("Validating user '{}' with password '{}'", new Object[]{user, password});
        // Add authentication token to security context here?
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, password);
            authentication = authenticationManager.authenticate(authentication);
            if (!authentication.isAuthenticated()) {
                LOG.error("User '{}' with password '{}' is not authenticated.", user, password);
                throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
            } else if (LOG.isDebugEnabled()){
                LOG.debug("User '{}' with password '{}' has been authenticated.", user, password);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (WSSecurityException | AuthenticationException ex) {
            LOG.error("Exception thrown authenticating: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
        } catch (RuntimeException ex) {
            LOG.error("RuntimeException thrown authenticating: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
        } 

    }
}
