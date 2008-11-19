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

    public PermissionedUser( WebUser user ) {
        this.user = user;
    }

    public WebUser getUser() {
        return user;
    }

    public String getPassword() {
        return user==null ? "" : user.getPassword();
    }

    public GrantedAuthority[] getAuthorities() 
    {
        WebUserRole role = user.getWebUserRole();
        return new GrantedAuthority[] { new GrantedAuthorityImpl(role.getName()) };
    }

    public String getUsername() {
        return user==null ? "" : user.getEmail();
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
