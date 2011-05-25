package idas.chox.web.security;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import idas.chox.service.security.PermissionedUser;
import java.security.SecureRandom;
import javax.servlet.http.HttpSession;
import org.hibernate.StaleObjectStateException;
import org.postgresql.util.Base64;

/**
 *
 * @author emmanuel
 */
public class CustomAuthenticationProcessingFilter extends AuthenticationProcessingFilter {
    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationProcessingFilter.class);

    protected static final String MEDIA_TYPE_PLAIN_TEXT = "text/plain";
    protected String passwordExpiredUrl;
    private UserService userService;

    /* A place to put authentication so it will be available to
     * sendRedirect
     */
    private Authentication currentAuthentication;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, Authentication authResult)
            throws IOException {
        LOG.debug("In onSuccessfulAuthentication...");

        super.onSuccessfulAuthentication(request, response, authResult);
        currentAuthentication = authResult;

        // Add nonce
        HttpSession session = request.getSession();
        byte[] nonce = new byte[16];
        SecureRandom rand;
        try {
            SecureRandom.getInstance("SHA1PRNG").nextBytes(nonce);
        } catch (NoSuchAlgorithmException ex) {
            LOG.error("Could not get algorithm SHA1PRNG");
        }
        String nonceStr = Base64.encodeBytes(nonce);

        session.setAttribute("SessionNonce", nonceStr);
        LOG.debug("Nonce added to session: {}", nonceStr);

        // Update users last login time
        try {
            userService.updateLastLogin(((PermissionedUser) currentAuthentication.getPrincipal()).getUser().getId());
        } catch (Exception ex) {
            LOG.warn("Error updating users last login time: {}", ex.getMessage());
            WebUser user = ((PermissionedUser) currentAuthentication.getPrincipal()).getUser();
            LOG.warn("UserID: {}, lastlogin='{}' version=" + user.getVersion(), user.getId(), user.getLastLoginDate());
        }
    }

    @Override
    protected void sendRedirect(HttpServletRequest request,
            HttpServletResponse response,
            String targetUrl) throws IOException {
        LOG.debug("In sendRedirect...");

        if (currentAuthentication != null) {
            PermissionedUser user = (PermissionedUser) currentAuthentication.getPrincipal();
            if (user.getUser().getIsExpired()) {
                sendResponse(request, response, getRelativeUrl(request, getPasswordExpiredUrl()));
                return;
            }
        }

        super.sendRedirect(request, response, targetUrl);
    }

    private void sendResponse(HttpServletRequest req,
            HttpServletResponse resp, String redirectUrl) throws IOException {
        LOG.debug("In sendResponse...");
        resp.sendRedirect(redirectUrl);
    }

    private String getRelativeUrl(HttpServletRequest request, String path) {
        LOG.debug("In getRelativeUrl...");
        if (path != null) {
            return request.getContextPath() + path;
        } else {
            return null;
        }
    }

    /**
     * @return the passwordExpiredUrl
     */
    public String getPasswordExpiredUrl() {
        return passwordExpiredUrl;
    }

    /**
     * @param passwordExpiredUrl the passwordExpiredUrl to set
     */
    public void setPasswordExpiredUrl(String passwordExpiredUrl) {
        this.passwordExpiredUrl = passwordExpiredUrl;
    }
}
