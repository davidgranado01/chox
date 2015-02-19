package idas.chox.web.security;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.WebUser;
import idas.chox.service.security.PermissionedUser;

/**
 *
 * @author Emmanuel
 */
public class WebSecurityInfoProvider implements SecurityInfoProvider, Serializable {

    public WebSecurityInfoProvider() {
    }

    public PermissionedUser getPermissionedUser() {
        PermissionedUser permissionedUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof PermissionedUser) {
            permissionedUser = (PermissionedUser) authentication.getPrincipal();
        }

        return permissionedUser;
    }

    @Override
    public WebUser getCurrentUser() {

        PermissionedUser permissionedUser = getPermissionedUser();
        if (permissionedUser != null) {
            return permissionedUser.getUser();
        }
        return null;
    }

    @Override
    public boolean getIsCHO() {

        return getPermissionedUser().getIsCHO();

    }

    @Override
    public boolean getIsINS() {

        return getPermissionedUser().getIsINS();

    }

    @Override
    public boolean getIsCHOXAdmin() {

        return getPermissionedUser().getIsCHOXAdmin();

    }

    @Override
    public boolean isInRoleOf(String role) {
        return getPermissionedUser().isInRoleOf(role);
    }
}
