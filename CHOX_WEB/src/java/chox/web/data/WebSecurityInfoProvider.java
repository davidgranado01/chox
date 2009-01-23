/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.data;

import chox.data.SecurityInfoProvider;
import chox.model.WebUser;
import chox.web.security.PermissionedUser;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;

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

    public WebUser getCurrentUSer() {


        return getPermissionedUser().getUser();
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
}
