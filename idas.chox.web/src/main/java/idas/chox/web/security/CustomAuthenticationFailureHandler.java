package idas.chox.web.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;

/**
 *
 * @author John
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    private String defaultFailureUrl;
    private String defaultBlockedUrl;
    private UserService userService;

    public void setDefaultBlockedUrl(String defaultBlockedUrl) {
        this.defaultBlockedUrl = defaultBlockedUrl;
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        String username = null;
        try {
            username = request.getParameterValues("j_username")[0];
        } catch (Exception ex) {
            Enumeration<String> paramNames = request.getParameterNames();
            StringBuilder pNames = new StringBuilder();
            while (paramNames.hasMoreElements()) {
                String pName = paramNames.nextElement();
                String pValue = null;
                if (request.getParameterValues(pName).length > 0) {
                    pValue = request.getParameterValues(pName)[0];
                }
                pNames.append(pName).append("=").append(pValue);
            }
            LOG.error("No username found in request parameters: {}", pNames.toString());
        }

        LOG.debug("AuthenticationException thrown for login attempt with username='{}'\n", username, exception);

        if (username != null) {
            WebUser user = null;

            try {
                user = userService.findByUserName(username);
            } catch (Exception ex) {
                LOG.warn("No such user: '{}'", username);
            }

            if (user != null && user.getMaxFailedLoginAttempts() > 0) {
                /*
                 * Here we need to increment the failed log-in attempt count
                 * and block if this is now >= the maxLoginAttempts of the organisation
                 */
                if (user.isBlocked() || userService.failedLogin(user.getId())) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    String blockedMessage = null;
                    if (user.isCHO()) {
                        blockedMessage = URLEncoder.encode(user.getChorganisation().getBlockedMessage(), "UTF-8");
                    } else if (user.isAnInsurer()) {
                        blockedMessage = URLEncoder.encode(user.getInsurer().getBlockedMessage(), "UTF-8");
                    }
                    getRedirectStrategy().sendRedirect(request, response, defaultBlockedUrl + "&message=" + blockedMessage);
                    return;
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        getRedirectStrategy().sendRedirect(request, response, defaultFailureUrl);

    }

}
