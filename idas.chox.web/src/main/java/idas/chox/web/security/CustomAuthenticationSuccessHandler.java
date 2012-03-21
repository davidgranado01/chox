package idas.chox.web.security;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import idas.chox.service.security.PermissionedUser;
import idas.chox.web.security.CustomAuthenticationSuccessHandler.BrowserUtil.BrowserType;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.postgresql.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 *
 * @author emmanuel
 */
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private UserService userService;
    private String browserWarningParam;
    private Authentication currentAuthentication;

    public String getBrowserWarningParam() {
        return browserWarningParam;
    }

    public void setBrowserWarningParam(String browserWarningParam) {
        this.browserWarningParam = browserWarningParam;
    }

    public Authentication getCurrentAuthentication() {
        return currentAuthentication;
    }

    public void setCurrentAuthentication(Authentication currentAuthentication) {
        this.currentAuthentication = currentAuthentication;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        LOG.debug("In onSuccessfulAuthentication...");

        currentAuthentication = authentication;

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
        checkBrowserWarning(request, response, getDefaultTargetUrl());
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void checkBrowserWarning(HttpServletRequest request,
            HttpServletResponse response,
            String targetUrl) throws IOException {
        LOG.debug("checking Browser warning...with targetUrl: {}", targetUrl);
        if (checkBrowserType(request) == BrowserType.INTERNET_EXPLORER_PRE7) {
            getRedirectStrategy().sendRedirect(request, response, targetUrl.concat(browserWarningParam));
            return;
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

    public static class BrowserUtil {

        public static enum BrowserType {

            INTERNET_EXPLORER, INTERNET_EXPLORER_PRE7, MOZILA_FIREFOX, SAFARI, NETSCAPE, GOOGLE_CHROME, FLOCK, UNKNOWN
        }
    }
}
