package idas.chox.web.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 *
 * @author emmanuel
 */
public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    public CustomAuthenticationEntryPoint(String url) {
        super(url);
    }
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOG.debug("In CustomAuthenticationProcessingFilterEntryPoint...");
        if (isAjaxRequest(request)) {
            LOG.debug("Is AJAX request.");
           HttpServletResponse httpResponse = response;
           httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           request.getSession().invalidate();
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
