package idas.chox.web.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import org.apache.commons.lang3.time.DateUtils;

import org.postgresql.util.Base64;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.IPWhitelistService;
import idas.chox.core.services.UserService;
import idas.chox.service.security.PermissionedUser;
import idas.chox.web.security.CustomAuthenticationSuccessHandler.BrowserUtil.BrowserType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author emmanuel
 */
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private UserService userService;
    private IPWhitelistService ipWhitelistService;
    private String browserWarningParam;
    private String failureUrl;
    private String blockedUrl;
    private Authentication currentAuthentication;

    public static class BrowserUtil {
        public static enum BrowserType {

            INTERNET_EXPLORER, INTERNET_EXPLORER_PRE7, MOZILA_FIREFOX, SAFARI, NETSCAPE, GOOGLE_CHROME, FLOCK, UNKNOWN
        }
    }

    public void setBrowserWarningParam(String browserWarningParam) {
        this.browserWarningParam = browserWarningParam;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public void setBlockedUrl(String blockedUrl) {
        this.blockedUrl = blockedUrl;
    }

    public void setCurrentAuthentication(Authentication currentAuthentication) {
        this.currentAuthentication = currentAuthentication;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setIpWhitelistService(IPWhitelistService ipWhitelistService) {
        this.ipWhitelistService = ipWhitelistService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws ServletException, IOException {
        int blockMinutes = 0;

        currentAuthentication = authentication;
        WebUser user = ((PermissionedUser)currentAuthentication.getPrincipal()).getUser();

        /*
         * ToDo item: 6.10.3 Enable (optional) IP white-listing for both CHO and Insurers
         * NB: This should be refactored to use a custom Spring decision voter
         */
        int orgId = -1;
        if (user.isAnInsurer()) {
            blockMinutes = user.getInsurer().getBlockTime();
            if (user.getInsurer().isEnableIPWhitelist()) {
                orgId = user.getInsurer().getId();
            }
        }
        else if (user.isCHO()) {
            blockMinutes = user.getChorganisation().getBlockTime();
            if (user.getChorganisation().isEnableIPWhitelist()) {
                orgId = user.getChorganisation().getId();
            }
        }

        if (orgId >= 0) {
            boolean isValid = false;
            
            LOG.trace("IP Whitelist enabled for user '{}' - validating.", user.getFullName());
            // Get client's IP address
            // First try with the clients remote address - this will return an 
            // empty string if not defined. If a proxy server is being used, the
            // address of the proxy server should be returned (if set), which is
            // what we want.
            String ipAddress = request.getRemoteAddr();
            if (!ipAddress.isEmpty()) {
                if (user.isAnInsurer()) {
                    isValid = ipWhitelistService.validateUserIPAddress(orgId, ipAddress, false, true);
                }
                else {
                    isValid = ipWhitelistService.validateUserIPAddress(orgId, ipAddress, true, false);
                }
                LOG.trace("IP address from request.getRemoteAddr() is '{}': isValid={}", ipAddress, isValid);
            }
            
            if (!isValid) {
                LOG.error("User '{}' denied access as IP address {} is not white-listed.", user.getFullName(), ipAddress);
                HttpServletResponse httpResponse = response;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                request.getSession().invalidate();
                getRedirectStrategy().sendRedirect(request, response, failureUrl);
                return;
            }
        }
        
        // Check account has not been blocked due to failed log-in attempts
        if (user.isBlocked()) {
            boolean blocked=true;
            // Check time-limit has not passed
            if (blockMinutes > 0) {
                Date blockTime = user.getBlockedDate();
                Date unblockDate = DateUtils.addMinutes(blockTime, blockMinutes);
                if ((new Date()).after(unblockDate)) {
                    LOG.info("Allowing access for user '{}' as blocked time limit has been exceeded (unblock date was '{}')", user.getFullName(), unblockDate);
                    blocked = false;
                }
            }

            if (blocked) {
                LOG.error("User '{}' denied access as account is currently blocked.", user.getFullName());
                String blockedMessage = null;
                if (user.isCHO()) {
                    blockedMessage = URLEncoder.encode(user.getChorganisation().getBlockedMessage(),  "UTF-8");
                }
                else if (user.isAnInsurer()) {
                    blockedMessage = URLEncoder.encode(user.getInsurer().getBlockedMessage(),  "UTF-8");
                }
                HttpServletResponse httpResponse = response;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                request.getSession().invalidate();
                getRedirectStrategy().sendRedirect(request, response, blockedUrl + "&message=" + blockedMessage);
                return;                
            } else {
                userService.unblock(user.getId());
            }
        }

        
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
        LOG.debug("Nonce added to session for user '{}' (id={}): {}", new Object[]{user.getDisplayName(), user.getId(), nonceStr});

        // Update users last login time
        try {
            userService.updateLastLogin(user.getId());
        } catch (Exception ex) {
            LOG.warn("Error updating users last login time: {}", ex.getMessage());
            LOG.warn("UserID: {}, lastlogin='{}' version=" + user.getVersion(), user.getId(), user.getLastLoginDate());
        }
        LOG.info("User '{}' logged-in successfully from IP address {}.", user.toString(), request.getRemoteAddr());
        checkBrowserWarning(request, response, getDefaultTargetUrl());
        super.onAuthenticationSuccess(request, response, authentication);
    }

    
    private void checkBrowserWarning(HttpServletRequest request,
            HttpServletResponse response,
            String targetUrl) throws IOException {
        LOG.trace("checking Browser warning...with targetUrl: {}", targetUrl);
        if (checkBrowserType(request) == BrowserType.INTERNET_EXPLORER_PRE7) {
            LOG.debug("Browser warning activated with targetUrl='{}'", targetUrl);
            getRedirectStrategy().sendRedirect(request, response, targetUrl.concat(browserWarningParam));
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

    
}
