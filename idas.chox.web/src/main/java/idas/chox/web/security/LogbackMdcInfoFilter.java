package idas.chox.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import idas.chox.core.model.WebUser;
import idas.chox.service.security.PermissionedUser;


public class LogbackMdcInfoFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LogbackMdcInfoFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fc) throws IOException, ServletException {

        try {
            Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
            if (currentUser != null && currentUser.getPrincipal() instanceof PermissionedUser) {
                WebUser user = ((PermissionedUser) currentUser.getPrincipal()).getUser();
                // Set logged in user details to SL4J logger. This user details will be printed on every log message.
                LOG.trace("adding mdc info to request: {}", ((HttpServletRequest) request).getServletPath());
                MDC.put("userid", user.getDisplayName() + " " + user.getId());
            } else {
                LOG.trace("User information not available to set into the mdc information for request: {}", ((HttpServletRequest) request).getServletPath());
            }
            fc.doFilter(request, response);
        }
        finally {
            LOG.trace("Removing MDC userid from request: {}", ((HttpServletRequest) request).getServletPath());
            MDC.remove("userid");
        }
    }
}
