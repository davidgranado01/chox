/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.security;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.WebUser;
import idas.chox.service.security.PermissionedUser;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

/**
 *
 * @author Emmanuel
 */
public class WebSecurityInfoProvider implements SecurityInfoProvider {

    public WebSecurityInfoProvider() {
    }

    public PermissionedUser getPermissionedUser() {
        PermissionedUser permissionedUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof PermissionedUser) {
            permissionedUser = (PermissionedUser) authentication.getPrincipal();
        } else {
            permissionedUser = null;
        }

        return permissionedUser;
    }

    public WebUser getCurrentUser() {

        PermissionedUser permissionedUser = getPermissionedUser();
        if (permissionedUser != null) {
            return permissionedUser.getUser();
        }
        return null;
    }

    public boolean getIsCHO() {

        return getPermissionedUser().getIsCHO();

    }

    public boolean getIsINS() {

        return getPermissionedUser().getIsINS();

    }

    public boolean getIsCHOXAdmin() {

        return getPermissionedUser().getIsCHOXAdmin();

    }

    public boolean isInRoleOf(String role) {
        return getPermissionedUser().isInRoleOf(role);
    }
}
