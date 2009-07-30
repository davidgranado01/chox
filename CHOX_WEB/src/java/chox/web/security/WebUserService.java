package chox.web.security;

import chox.model.WebUser;

import chox.services.UserService;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

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
