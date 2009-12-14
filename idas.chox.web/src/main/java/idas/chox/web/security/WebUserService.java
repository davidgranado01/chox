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
  
    public WebUser findByEmail(String email) {       
        return this.getUserService().findByEmail(email);
    }

    public void persist(WebUser user, String emailId) {
        this.getUserService().persist(user, emailId);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException, DataAccessException {
        
        WebUser u = findByEmail(s);
        
        if (s == null || "".equals(s.trim()) || u == null) {
            throw new UsernameNotFoundException(s);
        }
        
        if(u.getInsurer()!=null){
            if(!u.getInsurer().isStatus()){
                throw new UsernameNotFoundException(s);
            }
        }
        
        if(u.getChorganisation()!=null){
            if(!u.getChorganisation().isStatus()){
                throw new UsernameNotFoundException(s);
            }
        }
        UserDetails userDetail = new PermissionedUser(u);
        //u.setPassword(encodePassword(userDetail));
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
