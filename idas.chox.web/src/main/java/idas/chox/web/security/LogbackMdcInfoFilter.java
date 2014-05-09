package idas.chox.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import idas.chox.core.model.WebUser;
import idas.chox.service.security.PermissionedUser;

import org.slf4j.MDC;

public class LogbackMdcInfoFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser != null && currentUser.getPrincipal() instanceof PermissionedUser) {
            WebUser user = ((PermissionedUser) currentUser.getPrincipal()).getUser();
            // Set logged in user details to SL4J logger. This user details will be printed on every log message.
            MDC.put("userid", user.getDisplayName() + " " + user.getId());
        }
        fc.doFilter(request, response);
        MDC.clear();
    }
}
