package idas.chox.web.ws;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import idas.chox.web.security.WebUserService;

/**
 *
 * @author John
 */
public class ServerPasswordCallback implements CallbackHandler {

    static final Logger LOG = LoggerFactory.getLogger(ServerPasswordCallback.class);
    private WebUserService userDetailsService;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = null;
        for (Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback) {
                pc = (WSPasswordCallback) callback;
                break;
            }
        }
        if (pc != null) {
            LOG.debug("PasswordCallbackHandler: identifier is '{}'", pc.getIdentifier());
            LOG.debug("PasswordCallbackHandler: password is '{}'", pc.getPassword());

            LOG.trace("Service is :{}", userDetailsService);
            UserDetails userDetails = userDetailsService.loadUserByUsername(pc.getIdentifier());
            LOG.debug("Password retrieved for user '{}': {}", pc.getIdentifier(), userDetails.getPassword());
        // this seems ridiculous, but is necessary for passing authentication on
            // to Spring-Security. We're essentially bypassing CXF's WSS4JInterceptor
            // by ensuring that the password callback always matches the client password.
            String pass = userDetails.getPassword();
            if (pass != null) {
                LOG.debug("Password set: '{}'", pass);
                pc.setPassword(pass);
                return;
            }

            pc.setPassword(userDetails.getPassword());
        }
    }

    public void setUserDetailsService(WebUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
