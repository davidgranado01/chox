package idas.chox.service.security;

import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import java.util.Set;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

public class PermissionedUser implements UserDetails {

    private WebUser user;
    private String roles;
    private GrantedAuthority[] authorities;

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

        if (authorities == null) {

            Set roleSet = user.getRoles();
            authorities = new GrantedAuthority[roleSet.size()];

            int i = 0;
            for (Object o : roleSet) {
                String roleName = ((WebUserRole) o).getName();
                authorities[i] = new GrantedAuthorityImpl(roleName);
                i++;
            }
        }
        return authorities;
    }

    public boolean isInRoleOf(String role) {

        for (GrantedAuthority g : authorities) {
            if (g.getAuthority().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    public String getUsername() {
        return user == null ? "" : user.getEmail();
    }

    public String getDisplayName() {
        return user.getDisplayName();
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
