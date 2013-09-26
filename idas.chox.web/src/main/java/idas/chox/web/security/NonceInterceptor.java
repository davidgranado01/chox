package idas.chox.web.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;
import org.postgresql.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author John
 */
public class NonceInterceptor extends AbstractInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(NonceInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        final ActionContext context = invocation.getInvocationContext();

        final HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        HttpSession session = request.getSession(false);
        if (session == null) {
            LOG.error("No session in nonce interceptor for request '{}'", request.getRequestURL());
            return invocation.invoke();
        }
        Map<String, Object> sessionMap = context.getSession();
        LOG.debug("In NonceInterceptor with X-Requested-With = '{}', ServletPath = '{}'", request.getHeader("X-Requested-With"), request.getServletPath());
        LOG.debug("Request parameters: {}", NonceInterceptor.dumpParams(request));
        if (sessionMap != null) {
            if ((("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                    && !request.getServletPath().contains("checkViewingStatus") && !request.getServletPath().contains("activityMonitoringAction"))
                    || !"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))) {
                // Get nonce from session
                if (!sessionMap.containsKey("SessionNonce")) {
                    // No nonce found in session - must be first request, so add nonce
                    String nonceStr = generateNonce();

                    if (nonceStr != null) {
                        session.setAttribute("SessionNonce", nonceStr);
                        LOG.debug("Adding nonce '{}' to session {}", new Object[]{nonceStr, session.toString()});
                    } else {
                        LOG.error("Generated nonce is null.");
                    }
                    return invocation.invoke();
                }
                String sessionNonce = (String) sessionMap.get("SessionNonce");

                // Get request nonce
                if (request.getParameter("nonce") == null) {
                    if (request.getParameterMap() == null) {
                        LOG.error("No parameter map found in request '{}' with sessionNonce='{}' and sessionId='{}'",
                                new Object[]{request.getRequestURL(), sessionNonce, session.getId()});
                        return invocation.invoke();
                    } else if (request.getParameterMap().entrySet() == null || request.getParameterMap().entrySet().isEmpty()) {
                        LOG.error("No entries found in parameter map of request '{}' with sessionNonce='{}' and sessionId='{}'",
                                new Object[]{request.getRequestURL(), sessionNonce, session.getId()});
                        return invocation.invoke();
                    } else {
                        LOG.error("No nonce found in parameter map of request '{}' with sessionNonce='{}' and sessionId='{}'",
                                new Object[]{request.getRequestURL(), sessionNonce, session.getId()});
                        return "invalid.token";
                    }
                }

                String requestNonce = request.getParameter("nonce");

                // + symbols in requestNonce will be spaces so we'll have to update
                requestNonce = requestNonce.replace(' ', '+');
                // verify nonce
                if (!sessionNonce.equals(requestNonce)) {
                    LOG.error("Nonce values do not match: {} != {}", sessionNonce, requestNonce);
                    return "invalid.token";
                }

                // If this is a HTTP request, generate a new nonce
                if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                        && (request.getServletPath().contains("openClaimDetail.action") || request.getServletPath().contains("inbox.action"))) {
                    LOG.debug("Generating new nonce for servlet '{}'...", request.getServletPath());
                    String nonceStr = generateNonce();

                    if (nonceStr != null) {
                        session.setAttribute("SessionNonce", nonceStr);
                        LOG.debug("Replacing nonce '{}' with '{}' ", new Object[]{sessionNonce, nonceStr});
                    } else {
                        LOG.error("Generated nonce is null.");
                    }
                }
            } else {
                LOG.debug("No nonce check performed.");
            }
        } else {
            LOG.debug("No session map in request '{}", request.getServletPath());
        }

        return invocation.invoke();
    }

    private String generateNonce() {
        byte[] nonce = new byte[16];
        SecureRandom rand;
        try {
            SecureRandom.getInstance("SHA1PRNG").nextBytes(nonce);
        } catch (NoSuchAlgorithmException ex) {
            LOG.error("Could not get algorithm SHA1PRNG");
        }
        return Jsoup.clean(Base64.encodeBytes(nonce), Whitelist.none());
    }

    public static String dumpParams(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String[]>> entries = req.getParameterMap().entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(Arrays.toString(entry.getValue()))
                    .append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);  //Removes the last comma
        }
        return sb.toString();
    }
}
