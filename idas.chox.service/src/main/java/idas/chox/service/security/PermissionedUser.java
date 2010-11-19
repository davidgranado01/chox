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

    @Override
    public String getPassword() {
        return user == null ? "" : user.getPassword();
    }

    //we currently support single user single role only
    @Override
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

    @Override
    public String getUsername() {
        return user == null ? "" : user.getUserName();
    }

    public String getDisplayName() {
        return user.getDisplayName();
    }

    public boolean getIsCHO() {
        return isInRoleOf(WebUserRole.ROLE_CHO);
    }

    public boolean getIsINS() {
        return isInRoleOf(WebUserRole.ROLE_INS);

    }

    public boolean getIsCHOXAdmin() {
        return isInRoleOf(WebUserRole.ROLE_CHOX_ADMIN);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
