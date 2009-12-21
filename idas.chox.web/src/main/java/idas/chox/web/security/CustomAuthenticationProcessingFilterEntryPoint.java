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
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilterEntryPoint;

/**
 *
 * @author emmanuel
 */
public class CustomAuthenticationProcessingFilterEntryPoint extends AuthenticationProcessingFilterEntryPoint {

    @Override
    public void commence(ServletRequest request, ServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if (isAjaxRequest((HttpServletRequest) request)) {
           HttpServletResponse httpResponse = (HttpServletResponse)response;
           httpResponse.setStatus(401);
        } else {
            // no ajax request
            super.commence(request, response, authException);
        }
    }

    public static boolean isAjaxRequest(HttpServletRequest httpRequest) {
        String requestedWith = httpRequest.getHeader("x-requested-with");
        return requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest");
    }
}
