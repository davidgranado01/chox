package idas.chox.web.security;

import java.io.Serializable;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author John
 */
public class CachingHeadersInterceptor extends AbstractInterceptor implements
        Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(CachingHeadersInterceptor.class);
    private static final long serialVersionUID = -2773375159350215037L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        final ActionContext context = invocation.getInvocationContext();
        final HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);
        final HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        // Don't add to streaming requests or static content
        if (response != null
                && request != null
                && !request.getServletPath().contains("downloadExcelReport")
                && !request.getServletPath().contains("doExportAttachment")
                && !request.getServletPath().contains("doExportExcel")
                && !request.getServletPath().contains("doTaskExportExcel")
                && !request.getServletPath().contains("generateExcelReportForProcessedClaimDetails")) {
            // This action is never cached and is always downloaded; even with
            // back/forward buttons.
            response.setHeader("Cache-control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "-1");
            response.setHeader("X-XSS-Protection", "0");

        } else if (response != null && request != null) {
            response.setHeader("Expires", "-1");
            response.setHeader("X-XSS-Protection", "0");
        }

        return invocation.invoke();
    }
}
