/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilterEntryPoint;

/**
 *
 * @author emmanuel
 */
public class CustomAuthenticationProcessingFilterEntryPoint extends AuthenticationProcessingFilterEntryPoint {
    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationProcessingFilterEntryPoint.class);

    @Override
    public void commence(ServletRequest request, ServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOG.debug("In CustomAuthenticationProcessingFilterEntryPoint...");
        if (isAjaxRequest((HttpServletRequest) request)) {
            LOG.debug("Is AJAX request.");
           HttpServletResponse httpResponse = (HttpServletResponse)response;
           httpResponse.setStatus(401);
        } else {
            // no ajax request
            LOG.debug("Not an AJAX request.");
            super.commence(request, response, authException);
        }
    }

    public static boolean isAjaxRequest(HttpServletRequest httpRequest) {
        String requestedWith = httpRequest.getHeader("x-requested-with");
        return requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest");
    }
}
