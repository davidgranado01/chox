package idas.chox.web.security;

import idas.chox.service.security.PermissionedUser;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class WebUserService implements UserDetailsService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private SaltSource saltSource;

    public WebUser findByUserName(String userName) {
        return userService.findByUserName(userName);
    }

    public void persist(WebUser user) {
        this.getUserService().persist(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

        WebUser u = null;

        if (userName != null || !userName.isEmpty()) {
            u = findByUserName(userName);
        }


        if (u == null) {
            throw new UsernameNotFoundException(userName);
        }

        if (u.getInsurer() != null) {
            if (!u.getInsurer().isStatus()) {
                throw new UsernameNotFoundException(userName);
            }
        }

        if (u.getChorganisation() != null) {
            if (!u.getChorganisation().isStatus()) {
                throw new UsernameNotFoundException(userName);
            }
        }
        UserDetails userDetail = new PermissionedUser(u);
        return userDetail;
    }

    public String encodePassword(final UserDetails userDetails) {
        Object salt = null;

        if (this.saltSource != null) {
            salt = this.saltSource.getSalt(userDetails);
        }
        return passwordEncoder.encodePassword(userDetails.getPassword(), salt);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public final void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public final void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

}
