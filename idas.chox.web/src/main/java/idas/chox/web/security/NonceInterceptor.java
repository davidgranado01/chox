package idas.chox.web.security;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

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
        }
        Map<String, Object> sessionMap = context.getSession();
        if (sessionMap != null) {

            if (request != null && (("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                    && !request.getServletPath().contains("checkViewingStatus") && !request.getServletPath().contains("activityMonitoringAction"))
                    || !"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))) {
                // Get nonce from session
                if (!sessionMap.containsKey("SessionNonce")) {
                    // No nonce found - deny access
                    LOG.error("No nonce found in session.");
                    return "invalid.token";
                }
                String sessionNonce = (String) sessionMap.get("SessionNonce");

                // Get request nonce
                if (request.getParameter("nonce") == null) {
                    LOG.error("No nonce found in request '{}' with sessionNonce='{}' and sessionId='{}' (with query string '{}') - parameters follow:\n{}",
                            new Object[]{request.getRequestURL(), sessionNonce, session.getId(), request.getQueryString(), dumpParams(request)});
                    return "invalid.token";
                }
                String requestNonce = request.getParameter("nonce");

                // + symbols in requestNonce will be spaces so we'll have to update
                requestNonce = requestNonce.replace(' ', '+');
                // verify nonce
                if (!sessionNonce.equals(requestNonce)) {
                    LOG.error("Nonce values do not match: {} != {}", sessionNonce, requestNonce);
                    return "invalid.token";
                }
            }
        }

        return invocation.invoke();
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
