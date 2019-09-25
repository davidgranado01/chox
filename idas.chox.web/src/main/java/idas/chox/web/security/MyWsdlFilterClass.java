package idas.chox.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


/*
 * This class extends OncePerRequestFilter to prevent the forward(httpServelet DispatcherType) request being filterd by this filter.
 */

public class MyWsdlFilterClass extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(MyWsdlFilterClass.class);
    private final String WEB_SERVICE_URL_STRING = "/services";
//    private final String LOGIN_PAGE_REQUEST_URL = "/login.action";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fc) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String serveletPath = request.getServletPath();
        String contextPath = request.getContextPath();
        String method = request.getMethod();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // if the user is not authenticated then return the login page unless the request is for the login page or from the login page. 
        String queryString = ((HttpServletRequest) request).getQueryString();
        if ((auth == null || !auth.isAuthenticated()) && ((queryString != null && queryString.toLowerCase().startsWith("wsdl")) || serveletPath.endsWith(WEB_SERVICE_URL_STRING)) && !method.equals("POST")) {
            LOG.debug("serveletPath='{}', queryString='{}', contextPath='{}', method='{}', auth={}: Denying", new Object[]{serveletPath, queryString, contextPath, method, auth == null ? "null" : auth.isAuthenticated()});
            return;
        }
        LOG.debug("serveletPath='{}', queryString='{}', contextPath='{}', method='{}', auth={}: Allowing", new Object[]{serveletPath, queryString, contextPath, method, auth == null ? "null" : auth.isAuthenticated()});
        // Continue with next filter chain
        fc.doFilter(request, response);
    }

}
