package idas.chox.service.security;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;

public class PermissionedUser implements UserDetails {

    private WebUser user;
    private ArrayList<SimpleGrantedAuthority> authorities;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<SimpleGrantedAuthority>();
            Set roleSet = user.getRoles();
            for (Object o : roleSet) {
                String roleName = ((WebUserRole) o).getName();
                authorities.add(new SimpleGrantedAuthority(roleName));
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
