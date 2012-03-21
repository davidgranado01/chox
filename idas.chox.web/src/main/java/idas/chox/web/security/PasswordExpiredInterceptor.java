package idas.chox.web.security;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.PermissionedUser;
import java.io.Serializable;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class PasswordExpiredInterceptor extends AbstractInterceptor implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordExpiredInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        LOG.debug("In PasswordExpiredInterceptor with action: {}", invocation.getAction().toString());
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (!(invocation.getAction() instanceof idas.chox.web.actions.UserAccountAction) && currentUser != null && currentUser.getPrincipal() instanceof PermissionedUser) {
            PermissionedUser user = (PermissionedUser) currentUser.getPrincipal();
            WebUser webUser = user.getUser();
            int forcePasswordChangeDays = 0;

            Date passwordLastModifiedDate = webUser.getPasswordLastModifiedDate();
            long passwordNotChangedDays = DateHelper.getNumberOf24HourPeriodsBetween(passwordLastModifiedDate, new Date());

            if (user.getIsCHO()) {
                forcePasswordChangeDays = user.getUser().getChorganisation().getForcePasswordChange();
            } else if (user.getIsINS()) {
                forcePasswordChangeDays = user.getUser().getInsurer().getForcePasswordChange();
            }
            if (forcePasswordChangeDays > 0 && passwordNotChangedDays >= forcePasswordChangeDays) {
                LOG.debug("Password is '{}' days old and password expirey is set to '{}' days - forcing password change.",
                        passwordNotChangedDays, forcePasswordChangeDays);

                webUser.setIsExpired(Boolean.TRUE);
                
            } else {
                LOG.debug("No forced password change: password is '{}' days old, forced days set to '{}'", passwordNotChangedDays, forcePasswordChangeDays);
            }

            if (webUser.getIsExpired()) {
                LOG.debug("User password expired");
                return "password.expired";
            }
        }
        return invocation.invoke();
    }
}
