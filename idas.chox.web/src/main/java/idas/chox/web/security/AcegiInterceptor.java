package idas.chox.web.security;

import java.lang.reflect.Method;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import idas.chox.core.model.WebUser;
import idas.chox.service.security.PermissionedUser;


public class AcegiInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        Object action = invocation.getAction();
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser != null && currentUser.getPrincipal() instanceof PermissionedUser) {
            WebUser user = ((PermissionedUser) currentUser.getPrincipal()).getUser();
            // Set logged in user details to SL4J logger. This user details will be printed on every log message.
            for (Method m : action.getClass().getDeclaredMethods()) {
                if (m.getAnnotation(AcegiPrincipal.class) != null) {
                    m.invoke(action, currentUser.getPrincipal());
                }
            }
        }
        String result = invocation.invoke();
        return result;
    }
}
