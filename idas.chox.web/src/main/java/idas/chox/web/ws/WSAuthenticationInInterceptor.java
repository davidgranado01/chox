package idas.chox.web.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class WSAuthenticationInInterceptor extends WSS4JInInterceptor implements InitializingBean {

    static final Logger LOG = LoggerFactory.getLogger(WSAuthenticationInInterceptor.class);
    private AuthenticationManager authenticationManager;

    public WSAuthenticationInInterceptor() {
        super();
    }

    public WSAuthenticationInInterceptor(Map<String, Object> properties) {
        super(properties);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getAuthenticationManager(), "Authentication manager must be set");
        Assert.notNull(getProperties(), "Interceptor properties must be set, even if empty");
    }

    /**
     * @return the authenticationManager
     */
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    /**
     * @param authenticationManager the authenticationManager to set
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        LOG.debug("WSAthenticationInInterceptor in handleMessage()...");
        try {
            super.handleMessage(message);
            LOG.debug("WSAthenticationInInterceptor: returned from super...");
            List<WSHandlerResult> result = (ArrayList<WSHandlerResult>) message.getContextualProperty(WSHandlerConstants.RECV_RESULTS);
            if (result != null && !result.isEmpty()) {
                for (WSHandlerResult res : result) {
                    // loop through security engine results
                    for (WSSecurityEngineResult securityResult : (List<WSSecurityEngineResult>) res.getResults()) {
                        int action = (Integer) securityResult.get(WSSecurityEngineResult.TAG_ACTION);
                        // determine if the action was a username token
                        if ((action & WSConstants.UT) > 0) {
                            // get the principal object
                            WSUsernameTokenPrincipal principal = (WSUsernameTokenPrincipal) securityResult.get(WSSecurityEngineResult.TAG_PRINCIPAL);
                            if (principal.getPassword() == null) {
                                principal.setPassword("");
                            }
                            LOG.info("Authenticating with user='{}', password='{}'", principal.getName(), principal.getPassword());
                            Authentication authentication = new UsernamePasswordAuthenticationToken(principal.getName(), principal.getPassword());
                            authentication = authenticationManager.authenticate(authentication);
                            if (!authentication.isAuthenticated()) {
                                LOG.error("This user is not authenticated.");
//                                throw new AuthenticationException( "This user is not authentic." );
                            }
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("Authentication Credentials Not Found.");
            }
        } catch (RuntimeException ex) {
            LOG.error("Exception thrown in WS Authentication: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }
    }
}
