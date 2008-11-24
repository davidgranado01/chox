/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.data;

import chox.data.SecurityInfoProvider;
import chox.model.WebUser;
import chox.model.WebUserRole;
import chox.web.security.PermissionedUser;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;

/**
 *
 * @author Emmanuel
 */
public class WebSecurityInfoProvider implements SecurityInfoProvider {

    private PermissionedUser currentUser;

    public WebSecurityInfoProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof PermissionedUser) {
            currentUser = (PermissionedUser) authentication.getPrincipal();
        }
    }

    public WebUser getCurrentUSer() {


        return currentUser.getUser();
    }

    public boolean getIsCHO() {

        return currentUser.getIsCHO();

    }

    public boolean getIsINS() {

        return currentUser.getIsINS();

    }

    public boolean getIsCHOXAdmin() {

       return currentUser.getIsCHOXAdmin();

    }
}
