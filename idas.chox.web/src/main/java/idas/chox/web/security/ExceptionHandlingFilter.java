package idas.chox.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingFilter.class);
    private DefaultRedirectStrategy defaultRedirectStrategy;

    public void setDefaultRedirectStrategy(DefaultRedirectStrategy defaultRedirectStrategy) {
        this.defaultRedirectStrategy = defaultRedirectStrategy;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fc) throws IOException, ServletException {

        try {
            fc.doFilter(request, response);
        } catch (Exception ex) {
            try {
                boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

                if (ex instanceof CsrfException || ex instanceof InvalidCsrfTokenException) {
                    if (isAjax) {
                        LOG.error("Invalid CSRF token found for AJAX request '{}'. Exception message: {}",
                                UrlUtils.buildFullRequestUrl(request), ex.getMessage(), ex);
                        response.setStatus(417);
                    } else {
                        LOG.error("Invalid CSRF token found for HTTP request '{}'. Exception message: {}",
                                UrlUtils.buildFullRequestUrl(request), ex.getMessage(), ex);
                        defaultRedirectStrategy.sendRedirect(request, response, "/jsp/InvalidCsrfToken.jsp");
                    }
                } else if (ex instanceof ClientAbortException || ex instanceof IllegalStateException) {
                    LOG.warn("Exception thrown:", ex);
                } else {
                    LOG.error("Exception thrown:", ex);
                }
            } catch (Exception ex2) {
                LOG.error("Error dealing with exception {}: {}", new Object[]{ex.getMessage(), ex2.getMessage(), ex});
            }
        }
    }
}
