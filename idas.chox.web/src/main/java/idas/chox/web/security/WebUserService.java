package idas.chox.web.security;

import idas.chox.service.security.PermissionedUser;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.providers.dao.SaltSource;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

public class WebUserService implements UserDetailsService {
    
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private SaltSource saltSource;
  
    public WebUser findByUserName(String userName) {
        //return this.getUserService().findByEmail(email);
        return this.getUserService().findByUserName(userName);
    }

    public void persist(WebUser user, String emailId) {
        this.getUserService().persist(user, emailId);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        
        WebUser u = findByUserName(userName);
        
        if (userName == null || "".equals(userName.trim()) || u == null) {
            throw new UsernameNotFoundException(userName);
        }
        
        if(u.getInsurer()!=null){
            if(!u.getInsurer().isStatus()){
                throw new UsernameNotFoundException(userName);
            }
        }
        
        if(u.getChorganisation()!=null){
            if(!u.getChorganisation().isStatus()){
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
