package idas.chox.web.security;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(CsrfSecurityRequestMatcher.class);
    // Do not apply csrf filter for the following http methods
    private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    // Do not apply csrf filter for the web service call.
    private final String WEB_SERVICE_URL_STRING = "/services";
    // Do not apply csrf filter for the Logback call.
    private final String LOGBACK_LOGGING_URL_STRING = "/logBack";
//    private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("/services", null);

    @Override
    public boolean matches(HttpServletRequest request) {
        boolean result;
        
        /* 
         * if the http request method is one of (GET|HEAD|TRACE|OPTIONS) then return false.
         * returning false will not apply csrf filter for this request. 
         */
        if (allowedMethods.matcher(request.getMethod()).matches()) {
            result = false;
        }
//        return !unprotectedMatcher.matches(request);
//        return !request.getServletPath().contains(WEB_SERVICE_URL_STRING);
        else if (request.getServletPath().contains(WEB_SERVICE_URL_STRING)) {
            result = false;
        } else {
            result =  !request.getServletPath().contains(LOGBACK_LOGGING_URL_STRING);
        }
        
        LOG.debug("CSRF request matcher returning '{}'", result);

        return result;
    }
}
