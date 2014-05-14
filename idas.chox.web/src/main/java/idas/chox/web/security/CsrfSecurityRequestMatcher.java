package idas.chox.web.security;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
//import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CsrfSecurityRequestMatcher implements RequestMatcher {
    // Do not apply csrf token for the following http methods

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    // Do not apply csrf token for the web service call
    private final String WEB_SERVICE_URL_STRING = "/services";
//    private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("/services", null);

    @Override
    public boolean matches(HttpServletRequest request) {
        if (allowedMethods.matcher(request.getMethod()).matches()) {
            return false;
        }
//        return !unprotectedMatcher.matches(request);
        return !request.getServletPath().contains(WEB_SERVICE_URL_STRING);
    }
}
