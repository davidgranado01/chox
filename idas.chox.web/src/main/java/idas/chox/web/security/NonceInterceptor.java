package idas.chox.web.security;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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

        Map<String, Object> sessionMap = context.getSession();
        if (sessionMap != null) {

            final HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
            if (request != null && (("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                    && !request.getServletPath().contains("checkViewingStatus") && !request.getServletPath().contains("activityMonitoringAction"))
                    || !"XMLHttpRequest".equals(request.getHeader("X-Requested-With")))) {
                // Get nonce from session
                if (!sessionMap.containsKey("SessionNonce")) {
                    // No nonce found - deny access
                    LOG.error("No nonce found in session.");
                    return "invalid.token";
                }
                String sessionNonce = (String)sessionMap.get("SessionNonce");

                // Get request nonce
                if (request.getParameter("nonce") == null) {
                    LOG.error("No nonce found in request: {}", request.getRequestURL());
                    return "invalid.token";
                }
                String requestNonce = request.getParameter("nonce");
                LOG.debug("Request nonce value is: {}", requestNonce);

                // verify nonce
                if (!sessionNonce.equals(requestNonce)) {
                    LOG.error("Nonce values do not match: {} != {}", sessionNonce, requestNonce);
                    return "invalid.token";
                }
                LOG.debug("Session and request nonce values match - executing request: {}={}", sessionNonce, requestNonce);
            }
        }

        return invocation.invoke();
    }

}
