package idas.chox.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import idas.chox.service.security.PermissionedUser;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;


public class WebUserService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(WebUserService.class);

    private UserService userService;
    private static PasswordEncoder passwordEncoder;
    static {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        passwordEncoder.setDefaultPasswordEncoderForMatches(new MessageDigestPasswordEncoder("MD5"));
    }
    
    public WebUser findByUserName(String userName) {
        return userService.findByUserName(userName);
    }

    public void persist(WebUser user) {
        this.getUserService().persist(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

        WebUser u = null;

        if (userName != null && !userName.isEmpty()) {
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
        LOG.debug("User loaded: {}-{}", userDetail.getUsername(), userDetail.getPassword());
        return userDetail;
    }

    public String encodePassword(final UserDetails userDetails) {
        LOG.debug("Returning encoded password: {}", passwordEncoder.encode(userDetails.getPassword()));
        return passwordEncoder.encode(userDetails.getPassword());
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
