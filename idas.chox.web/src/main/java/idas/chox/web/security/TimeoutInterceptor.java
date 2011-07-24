package idas.chox.web.security;

import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author John
 */
public class TimeoutInterceptor extends AbstractInterceptor implements Serializable {
    private static final long serialVersionUID = -2773375159350225037L;
    private static final Logger LOG = LoggerFactory.getLogger(TimeoutInterceptor.class);
    private static final long TIMEOUT_PERIOD = 3600000; // 60 minutes

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
	final ActionContext context = invocation.getInvocationContext();
        boolean isAjax = false;

        Map<String, Object> sessionMap = context.getSession();
        final HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        final HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);
        if (sessionMap!= null && sessionMap.containsKey("timeAccessed")) {
            long lastTimeAccessed = (Long)sessionMap.get("timeAccessed");
            
            if (request != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                    && (request.getServletPath().contains("checkViewingStatus") || request.getServletPath().contains("activityMonitoringAction"))) {
                    isAjax = true;
            }

            if (System.currentTimeMillis() - lastTimeAccessed > TIMEOUT_PERIOD) {
                sessionMap.remove("timeAccessed");
                response.setStatus(408);
                return "session.expired";
            }
        }
        if (sessionMap!= null && !isAjax && !request.getServletPath().contains("login")) {
            sessionMap.put("timeAccessed", (Long)System.currentTimeMillis());
        }

//        Set<String> keys = sessionMap.keySet();
//        for (String key : keys) {
//            Object value = sessionMap.get(key);
//            LOG.debug("Session map: {} = {}", key, value.toString());
//        }
        return invocation.invoke();
    }
}
