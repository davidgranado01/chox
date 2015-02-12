package idas.chox.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        final HttpServletRequest request = (HttpServletRequest) req;

        if (response != null && request != null
                && (request.getServletPath().contains("ext")
                || request.getServletPath().contains("jquery"))
                || request.getServletPath().contains("libs")) {

            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.YEAR, 1); //1 year for libraries

            response.setHeader("Cache-control", "max-age=31536000");
            response.setHeader("Expires", htmlExpiresDateFormat().format(cal.getTime()));
        } else if (response != null && request != null
                && (request.getServletPath().contains("images"))) {

            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.MONTH, 1); //1 month for images

            response.setHeader("Cache-control", "max-age=2678400");
            response.setHeader("Expires", htmlExpiresDateFormat().format(cal.getTime()));
        } else if (response != null && request != null) {

            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.HOUR, 12); //12 hours for rest

            response.setHeader("Cache-control", "max-age=43200");
            response.setHeader("Expires", htmlExpiresDateFormat().format(cal.getTime()));
        }

        // pass the request/response on
        chain.doFilter(req, response);
    }

    public static DateFormat htmlExpiresDateFormat() {
        DateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.UK);
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDateFormat;
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
