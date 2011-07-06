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
import idas.chox.web.security.CustomAuthenticationProcessingFilter.BrowserUtil.BrowserType;
import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
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
        LOG.debug("In sendRedirect...with request: {}", request);

        if (currentAuthentication != null) {
            PermissionedUser user = (PermissionedUser) currentAuthentication.getPrincipal();
            if (user.getUser().getIsExpired()) {
                sendResponse(request, response, getRelativeUrl(request, getPasswordExpiredUrl()));
                return;
            }
        }

        LOG.debug("In sendRedirect...with targetUrl: {}", targetUrl);
        if (checkBrowserType(request) == BrowserType.INTERNET_EXPLORER_PRE7) {
            // display a warning 
            targetUrl += "?showSplash=true";
        }

//        Map<String, String[]> extraParams = new TreeMap<String, String[]>();
//        extraParams.put("showSplash", new String[]{"true"});
//        HttpServletRequest wrappedRequest = new WrappedRequest(request, extraParams);
//        super.sendRedirect(wrappedRequest, response, targetUrl);
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

    private BrowserType checkBrowserType(HttpServletRequest req) {
        String userAgent = req.getHeader("user-agent");
        BrowserType type = BrowserType.UNKNOWN;

        if (userAgent != null) {
            if (userAgent.indexOf("MSIE") != -1) {
                if (userAgent.indexOf("MSIE 6") != -1 || userAgent.indexOf("MSIE 5") != -1 || userAgent.indexOf("MSIE 4") != -1) {
                    type = BrowserType.INTERNET_EXPLORER_PRE7;
                } else {
                    type = BrowserType.INTERNET_EXPLORER;
                }
            } else if (userAgent.indexOf("Netscape") != -1) {
                type = BrowserType.NETSCAPE;
            } else if (userAgent.indexOf("Chrome") != -1) {
                type = BrowserType.GOOGLE_CHROME;
            } else if (userAgent.indexOf("Flock") != -1) {
                type = BrowserType.FLOCK;
            } else if (userAgent.indexOf("Safari") != -1) {
                type = BrowserType.SAFARI;
            } else if (userAgent.indexOf("Firefox") != -1) {
                type = BrowserType.MOZILA_FIREFOX;
            }
        }
        return type;
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

    public static class BrowserUtil {

        public static enum BrowserType {

            INTERNET_EXPLORER, INTERNET_EXPLORER_PRE7, MOZILA_FIREFOX, SAFARI, NETSCAPE, GOOGLE_CHROME, FLOCK, UNKNOWN
        }
    }
}
