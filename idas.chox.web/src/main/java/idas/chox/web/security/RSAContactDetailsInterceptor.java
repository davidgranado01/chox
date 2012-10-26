package idas.chox.web.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import idas.chox.core.model.WebUser;
import idas.chox.service.security.PermissionedUser;

/**
 *
 * @author John
 */
public class RSAContactDetailsInterceptor extends AbstractInterceptor implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(RSAContactDetailsInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        LOG.debug("In RSAContactDetailsInterceptor with action: {}", invocation.getAction().toString());
        if (!(invocation.getAction() instanceof idas.chox.web.actions.UserAccountAction)) {
            Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

            if (currentUser != null && currentUser.getPrincipal() instanceof PermissionedUser) {
                LOG.debug("User is authenticated");
                PermissionedUser permissionedUser = (PermissionedUser) currentUser.getPrincipal();
                WebUser user = permissionedUser.getUser();
                if (user.isAnInsurer() && user.getInsurer().getName().equals("RSA") && user.isClaimHandler()) {
                    LOG.debug("User is an RSA user");
                    if (user.getTelephone() == null || user.getTelephone().length() ==0) {
                        LOG.debug("User does not have any contact details - redirecting");
                        return "rsa.nocontactdetails";
                    }
                }
            }
        }
        return invocation.invoke();
    }
}
