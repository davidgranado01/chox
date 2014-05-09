package idas.chox.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.web.filter.GenericFilterBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class TimeoutFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(TimeoutFilter.class);
    private static final long TIMEOUT_PERIOD = 3600000; // 60 minutes
    private DefaultRedirectStrategy defaultRedirectStrategy;
    private final String TIME_ACCESSED_SESSION_ATTRIB = "timeAccessed";
    private final String SESSION_EXPIRED_JSP_URL = "/jsp/SessionExpired.jsp";
    private final String LOGIN_PAGE_REQUEST_URL = "/login.action";
    private final String INBOX_PAGE_REQUEST_URL = "/prv/inbox.action";
    private final String LOGIN_STRING = "login";
    private final String LOGIN_FORM_AUTH_CHECK_STRING = "j_spring_security_check";
    private final String LOGOUT_STRING = "j_spring_security_logout";
    private final String CHECK_VIEWING_STATUS_STRING = "checkViewingStatus";
    private final String ACTIVITY_MONITOR_CHECK_STRING = "activityMonitoringAction";

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) sr;
        final HttpServletResponse response = (HttpServletResponse) sr1;
        HttpSession session = request.getSession();

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        boolean isHiddenViewingStatusRequest = false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // if the user is not authenticated then return the login page unless the request is for login page. 
        if (!((auth != null && auth.isAuthenticated()) || request.getServletPath().contains(LOGIN_PAGE_REQUEST_URL)
                || request.getServletPath().contains(LOGIN_FORM_AUTH_CHECK_STRING))) {
            defaultRedirectStrategy.sendRedirect(request, response, LOGIN_PAGE_REQUEST_URL);
            return;
        }
        
        if (auth != null && auth.isAuthenticated() 
                && (request.getServletPath().contains(LOGIN_FORM_AUTH_CHECK_STRING) 
                || request.getServletPath().contains(LOGIN_PAGE_REQUEST_URL))) {
            defaultRedirectStrategy.sendRedirect(request, response, INBOX_PAGE_REQUEST_URL);
            return;
        }

        if (session.getAttribute(TIME_ACCESSED_SESSION_ATTRIB) != null) {
            long lastTimeAccessed = (Long) session.getAttribute(TIME_ACCESSED_SESSION_ATTRIB);

            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                    && (request.getServletPath().contains(CHECK_VIEWING_STATUS_STRING) || request.getServletPath().contains(ACTIVITY_MONITOR_CHECK_STRING))) {
                isHiddenViewingStatusRequest = true;
            }

            if (System.currentTimeMillis() - lastTimeAccessed > TIMEOUT_PERIOD) {

                session.removeAttribute(TIME_ACCESSED_SESSION_ATTRIB);
                request.getSession().invalidate();
                SecurityContextHolder.clearContext();

                LOG.info("Login session has been expired.");
                if (isAjax) { // This error code(418) is caught by extjs and ajax global exception handler and appropriate error message is shown to the user.

                    /*
                     *  Custom error status 418 set instead of standard timout error status 408 , to stop struts calling global exception handler.
                     *  Struts global exception handler uses CustomAuthenticationProcessingFilterEntryPoint which change the response status to 401 , to avoid this we use custom http status 418.
                     *  Struts global exception is called for error status 408, by Only request from firefox render engine (firefox, camino) , so to avoid this custom error status used.
                     */

                    response.setStatus(418);
                    return;
                } else if (request.getServletPath().contains(LOGOUT_STRING)) { // If this is logout request then directly go to login page.
                    defaultRedirectStrategy.sendRedirect(request, response, LOGIN_PAGE_REQUEST_URL);
                    return;
                } else if (!request.getServletPath().contains(SESSION_EXPIRED_JSP_URL)) { // If this is for any other request(non ajax) then show the error message.
                    defaultRedirectStrategy.sendRedirect(request, response, SESSION_EXPIRED_JSP_URL);
                    return;
                }

            }
            // If not an Ajax requst and not one of the checked url then should not proceed as TIME_ACCESSED_SESSION_ATTRIB is null.
        } else if (!isAjax
                && !request.getServletPath().contains(SESSION_EXPIRED_JSP_URL)
                && !request.getServletPath().contains(LOGIN_STRING)) {
            /* 
             * Tomcat delete the previously provided session to the login.action(login form) page if the user did not login before the timeout period.
             * So this casuse problem with spring csrf filter. 
             * To avoid this we need to check if this is new session or previously provided session. 
             * If this is previouly provided session then we do not need to perform any redirection. The request will continue.
             * If this is new session then we need to generate the login form page again to have the valid csrf token in the login form page. 
             * 
             */
            if (request.getServletPath().contains(LOGIN_FORM_AUTH_CHECK_STRING)) {
                if (session.isNew()) {
                    defaultRedirectStrategy.sendRedirect(request, response, LOGIN_PAGE_REQUEST_URL);
                    return;
                }
            } else {
                defaultRedirectStrategy.sendRedirect(request, response, SESSION_EXPIRED_JSP_URL);
                return;
            }

        } else if (isAjax) { // Ajax request shold not proceed to the application when TIME_ACCESSED_SESSION_ATTRIB is null. 
            response.setStatus(418);
            return;
        }
        // Only set the access time if the request is not in one of the following - hidden request, login page request and session expired page request.
        if (!isHiddenViewingStatusRequest && !request.getServletPath().contains(LOGIN_STRING) && !request.getServletPath().contains(SESSION_EXPIRED_JSP_URL)) {
            session.setAttribute(TIME_ACCESSED_SESSION_ATTRIB, (Long) System.currentTimeMillis());
        }
        // Continue with next filter chain
        // All the request(having timeAccessed param) within the timeout period and the below 2 request without timeAccessed param only can proceed.
        // /jsp/SessionExpired.jsp , /login.action
        fc.doFilter(request, response);
    }

    public void setDefaultRedirectStrategy(DefaultRedirectStrategy defaultRedirectStrategy) {
        this.defaultRedirectStrategy = defaultRedirectStrategy;
    }
}
