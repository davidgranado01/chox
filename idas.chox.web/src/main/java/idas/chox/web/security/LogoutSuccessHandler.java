package idas.chox.web.security;

import idas.chox.core.services.UserService;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LogoutSuccessHandler.class);
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            LOG.debug("Logging out.....");
            String result, kbbsToken = null;
            
            Cookie cookies[] = request.getCookies();
            Cookie kbbsCookie = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JD.Token")) {
                    kbbsCookie = cookie;
                    kbbsToken = cookie.getValue();
                }
            }
            if (kbbsToken != null && !kbbsToken.isEmpty()) {
                try {
                    result = userService.kbbsInvalidate(kbbsToken);
                    LOG.debug("Result from invalidating KBBS authentication token: {}", result);
                } catch (Exception ex) {
                    LOG.warn("Exception thrown invaludating KBBS token '{}': {}", kbbsToken, ex.getMessage());
                } finally {
                    kbbsCookie.setValue("");
                    kbbsCookie.setPath("/");
                    kbbsCookie.setMaxAge(0);
                    response.addCookie(kbbsCookie);
                }
           } else {
                 LOG.debug("No KBBBStoken to invalidate");
            }
            
        }

        setDefaultTargetUrl("/login");
        super.onLogoutSuccess(request, response, authentication);       
    }
}
