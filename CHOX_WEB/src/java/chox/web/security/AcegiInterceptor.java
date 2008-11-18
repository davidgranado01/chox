package chox.web.security;

import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.ActionInvocation;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;

import java.lang.reflect.Method;

/**
 * @author Ian Roughley
 * @version $Id$
 */
public class AcegiInterceptor extends AbstractInterceptor {

    public String intercept(ActionInvocation invocation) throws Exception {

        Object action = invocation.getAction();
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if( currentUser!=null ) {
            for( Method m: action.getClass().getDeclaredMethods() ) {
                if( m.getAnnotation(AcegiPrincipal.class)!=null
                        && currentUser.getPrincipal() instanceof PermissionedUser ) {
                    m.invoke(action,currentUser.getPrincipal());
                }
            }
        }

        return invocation.invoke();
    }
}
