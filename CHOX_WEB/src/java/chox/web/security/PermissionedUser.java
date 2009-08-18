package chox.web.security;

import chox.model.WebUser;
import chox.model.WebUserRole;
import java.util.Set;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;

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
        //String securityRole = getIsCHOXAdmin() ? "ROLE_ADMIN" : "ROLE_USER";

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
        
        for(GrantedAuthority g : authorities)
        {
            if(g.getAuthority().equalsIgnoreCase(role))
            {
                return true;
            }
        }
        return false;
    }

    public String getUsername() {
        return user == null ? "" : user.getEmail();
    }

    public String getDisplayName() {
        return user.getFirstName() + ", " + user.getLastName();
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
