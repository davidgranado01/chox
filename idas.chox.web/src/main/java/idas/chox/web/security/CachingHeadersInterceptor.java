package idas.chox.web.security;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.StrutsStatics;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.io.Serializable;
/**
 *
 * @author John
 */
public class CachingHeadersInterceptor extends AbstractInterceptor implements Serializable {

    private static final long serialVersionUID = -2773375159350215037L;

    @Override
	public String intercept(ActionInvocation invocation) throws Exception {

		final ActionContext context = invocation.getInvocationContext();

		final HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);
		if(response!=null) {
			response.setHeader("Cache-control","no-cache, no-store");
			response.setHeader("Pragma","no-cache");
			response.setHeader("Expires","-1");
		}

		return invocation.invoke();
	}
}
