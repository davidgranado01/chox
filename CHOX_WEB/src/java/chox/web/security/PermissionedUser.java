package chox.web.security;

import chox.model.WebUser;
import chox.model.WebUserRole;
import java.util.ArrayList;
import java.util.Set;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;

/**
 * @author Ian Roughley
 * @version $Id$
 */
public class PermissionedUser implements UserDetails {

    private WebUser user;
    private String roles;

    public PermissionedUser(WebUser user) {
        this.user = user;
    }

    public WebUser getUser() {
        return user;
    }

    public String getPassword() {
        return user == null ? "" : user.getPassword();
    }

    //we currently support single user single role only
    public GrantedAuthority[] getAuthorities() {
        String securityRole = getIsCHOXAdmin() ? "ROLE_ADMIN" : "ROLE_USER";
        return new GrantedAuthority[]{new GrantedAuthorityImpl(securityRole)};
    }

    public boolean isInRoleOf(String role) {
        if (roles == null || roles.isEmpty()) {
            Set roleSet = user.getRoles();
            for (Object r : roleSet) {
                String roleName = ((WebUserRole) r).getName();
                roles += roleName + "|";
            }
        }

        return roles.lastIndexOf(role) > 0;
    }

    public String getUsername() {
        return user == null ? "" : user.getEmail();
    }

    public boolean getIsCHO() {

        return isInRoleOf("ROLE_CHO");

    }

    public boolean getIsINS() {

        return isInRoleOf("ROLE_INS");

    }

    public boolean getIsCHOXAdmin() {
        return isInRoleOf("ROLE_CHOX_ADMIN");
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
