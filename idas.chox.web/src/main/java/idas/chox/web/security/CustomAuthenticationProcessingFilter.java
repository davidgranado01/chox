/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.security;

import idas.chox.service.security.PermissionedUser;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.Authentication;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;

/**
 *
 * @author emmanuel
 */
public class CustomAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

    protected static final String MEDIA_TYPE_PLAIN_TEXT = "text/plain";
    protected String passwordExpiredUrl;

    /* A place to put authentication so it will be available to
     * sendRedirect
     */
    private Authentication currentAuthentication;

    @Override
    public void afterPropertiesSet() throws Exception {

        // Ensure sendRedirect will always be called with url = true on successful auth.
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, Authentication authResult)
            throws IOException {

        super.onSuccessfulAuthentication(request, response, authResult);
        currentAuthentication = authResult;

    }

    @Override
    protected void sendRedirect(HttpServletRequest request,
            HttpServletResponse response,
            String targetUrl) throws IOException {

        if (currentAuthentication != null) {
            PermissionedUser user = (PermissionedUser) currentAuthentication.getPrincipal();
            if (user.getUser().getIsExpired()) {
                sendResponse(request, response, getRelativeUrl(request, getPasswordExpiredUrl()));
                return;
            }
        }

        super.sendRedirect(request, response, targetUrl);
    }

    private void sendResponse(HttpServletRequest req,
            HttpServletResponse resp, String redirectUrl) throws IOException {
        resp.sendRedirect(redirectUrl);
    }

    private String getRelativeUrl(HttpServletRequest request, String path) {
        if (path != null) {
            return request.getContextPath() + path;
        } else {
            return null;
        }
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
}
