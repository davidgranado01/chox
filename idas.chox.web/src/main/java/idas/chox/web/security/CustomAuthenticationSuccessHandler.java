package idas.chox.web.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.IPWhitelistService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.PermissionedUser;
import idas.chox.web.security.CustomAuthenticationSuccessHandler.BrowserUtil.BrowserType;

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

            INTERNET_EXPLORER, INTERNET_EXPLORER_PRE7, INTERNET_EXPLORER_7, INTERNET_EXPLORER_8, INTERNET_EXPLORER_9, MOZILA_FIREFOX, SAFARI, NETSCAPE, GOOGLE_CHROME, FLOCK, UNKNOWN
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
        boolean isInsurer = false;
        boolean isCHO = false;

        currentAuthentication = authentication;
        PermissionedUser permissionedUser = (PermissionedUser) currentAuthentication.getPrincipal();
        WebUser user = permissionedUser.getUser();
//        MDC.put("userid", user.getDisplayName() + " " + user.getId());

        /*
         * ToDo item: 6.10.3 Enable (optional) IP white-listing for both CHO and Insurers
         * NB: This should be refactored to use a custom Spring decision voter
         */
        int orgId = -1;
        if (user.isAnInsurer()) {
            isInsurer = true;
            blockMinutes = user.getInsurer().getBlockTime();
            if (user.getInsurer().isEnableIPWhitelist()) {
                orgId = user.getInsurer().getId();
            }
        } else if (user.isCHO()) {
            isCHO = true;
            blockMinutes = user.getChorganisation().getBlockTime();
            if (user.getChorganisation().isEnableIPWhitelist()) {
                orgId = user.getChorganisation().getId();
            }
        }

        switch (checkBrowserType(request)) {
            case INTERNET_EXPLORER_PRE7:
                LOG.info("User '{}' still using IE6.", user.toString());
                break;

            case INTERNET_EXPLORER_7:
                LOG.info("User '{}' still using IE7.", user.toString());
                break;

            case INTERNET_EXPLORER_8:
                LOG.info("User '{}' still using IE8.", user.toString());
                break;

            case INTERNET_EXPLORER_9:
                LOG.info("User '{}' still using IE9.", user.toString());
                break;

            default:
                break;
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
                } else {
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
            boolean blocked = true;
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
                LOG.warn("User '{}' denied access as account is currently blocked.", user.getFullName());
                String blockedMessage = null;
                if (user.isCHO()) {
                    blockedMessage = URLEncoder.encode(user.getChorganisation().getBlockedMessage(), "UTF-8");
                } else if (user.isAnInsurer()) {
                    blockedMessage = URLEncoder.encode(user.getInsurer().getBlockedMessage(), "UTF-8");
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

        // Update users last login time
        try {
            userService.updateLastLogin(user.getId());
        } catch (Exception ex) {
            LOG.warn("Error updating users last login time: {}", ex.getMessage());
            LOG.warn("UserID: {}, lastlogin='{}' version={}", new Object[]{user.getVersion(), user.getId(), user.getLastLoginDate()});
        }
        LOG.info("User '{}' logged-in successfully from IP address {} on browser '{}' with HTTP sessionId='{}'.",
                new Object[]{user.toString(), request.getRemoteAddr(), request.getHeader("user-agent"), request.getSession().getId()});

        // Check if KBBS Dashboards enabled and if so authenticate
        if ((isInsurer && user.getInsurer().isEnableKbbsDashboard()) || (isCHO && user.getChorganisation().isEnableKbbsDashboard())) {
            String kbbsAuthenticationToken = userService.kbbsAuthenticate(user);
            // Add Authentication Cookie
            if (kbbsAuthenticationToken != null) {
                Cookie cookie = new Cookie("ASP.NET_Token", kbbsAuthenticationToken);
                cookie.setDomain("idaschox.com");
                cookie.setMaxAge(-1);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setSecure(true);
                response.addCookie(cookie);
            }
        }

        // Add orgId to session
        if (user.isAnInsurer()) {
            if (orgId < 0) {
                orgId = user.getInsurer().getId();
            }
            LOG.debug("Adding insurer orgId {} to session {}", orgId, request.getSession());
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("insurerId", orgId);
            request.getSession().setAttribute("isInsurer", Boolean.TRUE);
            request.getSession().setAttribute("isCHO", Boolean.FALSE);
            request.getSession().setAttribute("isWorkgroupEnable", user.getInsurer().isWorkgroupEnable());
        } else if (user.isCHO()) {
            if (orgId < 0) {
                orgId = user.getChorganisation().getId();
            }
            LOG.debug("Adding cho orgId {} to session {}", orgId, request.getSession());
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("choId", orgId);
            request.getSession().setAttribute("isInsurer", Boolean.FALSE);
            request.getSession().setAttribute("isCHO", Boolean.TRUE);
            request.getSession().setAttribute("isWorkgroupEnable", Boolean.FALSE);
        } else {
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("isInsurer", Boolean.FALSE);
            request.getSession().setAttribute("isCHO", Boolean.FALSE);
            request.getSession().setAttribute("isWorkgroupEnable", Boolean.FALSE);
        }

        // Check password not expired
        int forcePasswordChangeDays = 0;

        Date passwordLastModifiedDate = user.getPasswordLastModifiedDate();
        long passwordNotChangedDays = DateHelper.getNumberOf24HourPeriodsBetween(passwordLastModifiedDate, new Date());

        if (permissionedUser.getIsCHO()) {
            forcePasswordChangeDays = user.getChorganisation().getForcePasswordChange();
        } else if (permissionedUser.getIsINS()) {
            forcePasswordChangeDays = user.getInsurer().getForcePasswordChange();
        }
        if (forcePasswordChangeDays > 0 && passwordNotChangedDays >= forcePasswordChangeDays) {
            LOG.debug("Password is '{}' days old and password expirey is set to '{}' days - forcing password change.",
                    passwordNotChangedDays, forcePasswordChangeDays);

            user.setIsExpired(Boolean.TRUE);
            user.setIsExpired(Boolean.TRUE); // Needed to update the permissioned user to get the expired message in the page

        } else {
            LOG.debug("No forced password change: password is '{}' days old, forced days set to '{}'", passwordNotChangedDays, forcePasswordChangeDays);
        }

        if (user.getIsExpired()) {
            LOG.debug("User password expired");
            getRedirectStrategy().sendRedirect(request, response, "/prv/openUserAccountRedirect.action?redirect=true");
        }

        // If RSA, check contact details
        if (user.isAnInsurer() && user.getInsurer().getName().equals("RSA") && user.isClaimHandler()) {
            LOG.debug("User is an RSA user");
            if (user.getTelephone() == null || user.getTelephone().length() == 0) {
                LOG.debug("User does not have any contact details - redirecting");
                getRedirectStrategy().sendRedirect(request, response, "/prv/addContactDetails.action?redirect=true");
            }
        }

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

    public static String requote(String jsonString) {
        return jsonString.replace('\'', '"');
    }

    private BrowserType checkBrowserType(HttpServletRequest req) {
        String userAgent = req.getHeader("user-agent");
        BrowserType type = BrowserType.UNKNOWN;

        if (userAgent != null) {
            if (userAgent.contains("MSIE")) {
                if (userAgent.contains("MSIE 6") || userAgent.contains("MSIE 5") || userAgent.contains("MSIE 4")) {
                    type = BrowserType.INTERNET_EXPLORER_PRE7;
                } else if (userAgent.contains("MSIE 7")) {
                    type = BrowserType.INTERNET_EXPLORER_7;
                } else if (userAgent.contains("MSIE 8")) {
                    type = BrowserType.INTERNET_EXPLORER_8;
                } else if (userAgent.contains("MSIE 9")) {
                    type = BrowserType.INTERNET_EXPLORER_9;
                } else {
                    type = BrowserType.INTERNET_EXPLORER;
                }
            } else if (userAgent.contains("Netscape")) {
                type = BrowserType.NETSCAPE;
            } else if (userAgent.contains("Chrome")) {
                type = BrowserType.GOOGLE_CHROME;
            } else if (userAgent.contains("Flock")) {
                type = BrowserType.FLOCK;
            } else if (userAgent.contains("Safari")) {
                type = BrowserType.SAFARI;
            } else if (userAgent.contains("Firefox")) {
                type = BrowserType.MOZILA_FIREFOX;
            }
        }
        return type;
    }

}
