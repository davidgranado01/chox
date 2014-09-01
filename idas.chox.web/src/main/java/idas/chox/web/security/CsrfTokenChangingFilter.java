package idas.chox.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsrfTokenChangingFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(CsrfTokenChangingFilter.class);

    private final CsrfTokenRepository tokenRepository;
    private final String INBOX_PAGE_REQUEST_STRING = "inboxPage.action";
    private final String CLAIM_DETAILS_PAGE_REQUEST_STRING = "claimDetails.action";

    public CsrfTokenChangingFilter(CsrfTokenRepository csrfTokenRepository) {
        this.tokenRepository = csrfTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // If this is a HTTP request and the url matches then generate new csrf token
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With")) 
                && (request.getServletPath().contains(INBOX_PAGE_REQUEST_STRING) 
                || request.getServletPath().contains(CLAIM_DETAILS_PAGE_REQUEST_STRING))) {
            CsrfToken csrfToken = tokenRepository.generateToken(request);
            CsrfToken oldCSRFToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
            csrfToken = new SaveOnAccessCsrfToken(tokenRepository, request, response, csrfToken);
            LOG.debug("Changing CSRF token from '{}' to '{}' using request attributes '{}' and '{}'",
                    new Object[] {oldCSRFToken.getToken(), csrfToken.getToken(), CsrfToken.class.getName(), csrfToken.getParameterName()});
            request.setAttribute(CsrfToken.class.getName(), csrfToken);
            request.setAttribute(csrfToken.getParameterName(), csrfToken);
        }

        filterChain.doFilter(request, response);
    }

    private static final class SaveOnAccessCsrfToken implements CsrfToken {

        private transient CsrfTokenRepository tokenRepository;
        private transient HttpServletRequest request;
        private transient HttpServletResponse response;
        private final CsrfToken delegate;

        public SaveOnAccessCsrfToken(CsrfTokenRepository tokenRepository,
                HttpServletRequest request, HttpServletResponse response,
                CsrfToken delegate) {
            super();
            this.tokenRepository = tokenRepository;
            this.request = request;
            this.response = response;
            this.delegate = delegate;
        }

        @Override
        public String getHeaderName() {
            return delegate.getHeaderName();
        }

        @Override
        public String getParameterName() {
            return delegate.getParameterName();
        }

        @Override
        public String getToken() {
            saveTokenIfNecessary();
            return delegate.getToken();
        }

        private void saveTokenIfNecessary() {
            if (this.tokenRepository == null) {
                return;
            }

            synchronized (this) {
                if (tokenRepository != null) {
                    this.tokenRepository.saveToken(delegate, request, response);
                    this.tokenRepository = null;
                    this.request = null;
                    this.response = null;
                }
            }
        }
    }
}
