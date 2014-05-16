package idas.chox.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.filter.GenericFilterBean;

import org.apache.catalina.connector.ClientAbortException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandlingFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingFilter.class);
    private DefaultRedirectStrategy defaultRedirectStrategy;

    public void setDefaultRedirectStrategy(DefaultRedirectStrategy defaultRedirectStrategy) {
        this.defaultRedirectStrategy = defaultRedirectStrategy;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {

        try {
            fc.doFilter(request, response);
        } catch (Exception ex) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final HttpServletResponse httpResponse = (HttpServletResponse) response;
            boolean isAjax = "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"));

            if (ex instanceof CsrfException) {
                LOG.warn("Invalid CSRF token found for {}. Exception message {}", UrlUtils.buildFullRequestUrl(httpRequest), ex.getMessage());
                if (isAjax) {
                    httpResponse.setStatus(417);
                } else {
                    defaultRedirectStrategy.sendRedirect(httpRequest, httpResponse, "/jsp/InvalidCsrfToken.jsp");
                }
            } else if (!(ex instanceof ClientAbortException)) {
                LOG.error("Exception thrown:", ex);
            }
        }
    }
}
