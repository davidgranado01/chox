package chox.web.security;

import chox.model.WebUser;
import chox.model.WebUserRole;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;

/**
 * @author Ian Roughley
 * @version $Id$
 */
public class PermissionedUser implements UserDetails {

    private WebUser user;

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

    public String getUsername() {
        return user == null ? "" : user.getEmail();
    }

    public boolean getIsCHO() {
        if (user == null) {
            return false;
        } else {
            WebUserRole role = user.getWebUserRole();
            return role.getName().startsWith("ROLE_CHO");
        }
    }

    public boolean getIsINS() {
        if (user == null) {
            return false;
        } else {
            WebUserRole role = user.getWebUserRole();
            return role.getName().startsWith("ROLE_INS");
        }
    }

    public boolean getIsCHOXAdmin() {
        if (user == null) {
            return false;
        } else {
            WebUserRole role = user.getWebUserRole();
            return role.getName().equals("ROLE_CHO_ADMIN");
        }
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
