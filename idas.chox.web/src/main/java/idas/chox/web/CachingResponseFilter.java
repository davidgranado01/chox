package idas.chox.web;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CachingResponseFilter implements Filter {

    FilterConfig fc;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request = (HttpServletRequest) req;

        if (response != null && request != null
                && (   request.getServletPath().contains("ext")
                    || request.getServletPath().contains("jquery")
                    || request.getServletPath().contains("libs"))) {

            // 1 year for images
            OffsetDateTime oneYearFromNow = OffsetDateTime.now(ZoneOffset.UTC).plus(Duration.ofDays(365));
            String cookieExpires = DateTimeFormatter.RFC_1123_DATE_TIME.format(oneYearFromNow);

            response.setHeader("Cache-control", "max-age=31536000");
            response.setHeader("Expires", cookieExpires);
        } else if (response != null && request != null
                && (request.getServletPath().contains("images"))) {

            // 30 Days for images
            OffsetDateTime oneMonthFromNow = OffsetDateTime.now(ZoneOffset.UTC).plus(Duration.ofDays(30));
            String cookieExpires = DateTimeFormatter.RFC_1123_DATE_TIME.format(oneMonthFromNow);
            
            response.setHeader("Cache-control", "max-age=2678400");
            response.setHeader("Expires", cookieExpires);
        } else if (response != null && request != null
                && (   request.getServletPath().contains("downloadExcelReport")
                    || request.getServletPath().contains("doExportAttachment")
                    || request.getServletPath().contains("doExportExcel")
                    || request.getServletPath().contains("doTaskExportExcel")
                    || request.getServletPath().contains("generateExcelReportForProcessedClaimDetails"))) {
            response.setDateHeader("Expires", 0);
            response.setHeader("X-XSS-Protection", "1");
            response.setHeader("X-Frame-Options", "DENY");
            response.setHeader("X-Content-Type-Options", "nosniff");
            if (request.isSecure()) {
                response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
            }
        } else if (response != null && request != null) {
            // 12 Hours for the rest
//            OffsetDateTime twelveHoursFromNow = OffsetDateTime.now(ZoneOffset.UTC).plus(Duration.ofDays(30));
//            String cookieExpires = DateTimeFormatter.RFC_1123_DATE_TIME.format(twelveHoursFromNow);
//            response.setHeader("Cache-control", "max-age=43200");
//            response.setHeader("Expires", cookieExpires);
            // This action is never cached and is always downloaded; even with
            // back/forward buttons.
            response.setHeader("Cache-control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("X-XSS-Protection", "1");
            response.setHeader("X-Frame-Options", "DENY");
            response.setHeader("X-Content-Type-Options", "nosniff");
            if (request.isSecure()) {
                response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
            }

        }

        // pass the request/response on
        chain.doFilter(req, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.fc = filterConfig;
    }

    @Override
    public void destroy() {
        this.fc = null;
    }
}
