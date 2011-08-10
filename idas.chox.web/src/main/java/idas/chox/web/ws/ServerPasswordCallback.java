package idas.chox.web.ws;

import idas.chox.web.security.WebUserService;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

/**
 *
 * @author John
 */
public class ServerPasswordCallback implements CallbackHandler {
    static final Logger LOG = LoggerFactory.getLogger(ServerPasswordCallback.class);

//    private UserDetailsService service;
    private WebUserService userDetailsService;
    
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        LOG.debug("PasswordCallbackHandler: identifier is '{}'", pc.getIdentifier());
        LOG.debug("PasswordCallbackHandler: password is '{}'", pc.getPassword());

        LOG.info("Service is :{}", userDetailsService);
        UserDetails userDetails = userDetailsService.loadUserByUsername(pc.getIdentifier());
        LOG.debug("Password retrieved for user '{}': {}", pc.getIdentifier(), userDetails.getPassword());
        // this seems ridiculous, but is necessary for passing authentication on
        // to Spring-Security. We're essentially bypassing CXF's WSS4JInterceptor
        // by ensuring that the password callback always matches the client password.
//        pc.setPassword(pc.getPassword());
        pc.setPassword(userDetails.getPassword());

    }

//    public void setService(UserDetailsService service) {
//        this.service = service;
//    }

    public void setUserDetailsService(WebUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}
