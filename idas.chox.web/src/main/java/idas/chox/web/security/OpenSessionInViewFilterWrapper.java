package idas.chox.web.security;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;

/**
 *
 * @author john
 */
public class OpenSessionInViewFilterWrapper extends OpenSessionInViewFilter {
    private static final Logger LOG = LoggerFactory.getLogger(OpenSessionInViewFilterWrapper.class);
    static final Pattern STATIC_RESOURCES = Pattern.compile("(^/js/.*)|(^/css/.*)|(^/img/.*)|(^/fonts/.*)|(/favicon.ico)|(.*checkViewingStatus.*)|(.*activityMon.*)");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (STATIC_RESOURCES.matcher(path).matches()) {
            LOG.debug("Not adding session to request {}", path);
            filterChain.doFilter(request, response);
        } else {
            LOG.trace("Adding session to request {}", path);
            super.doFilterInternal(request, response, filterChain);
        }
    }
}
