package chox.web.security;

import chox.model.WebUser;

import chox.services.UserService;
import chox.services.UserServiceImpl;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

public class WebUserService implements UserDetailsService {
    
    private UserService service;
    
    public WebUserService() {
        setUserService(new UserServiceImpl());
    }
    
    public void setUserService(UserService service) {
        this.service = service;
    }
    

    public WebUser findByEmail(String email) {       
        return this.service.findByEmail(email);
    }

    public void persist(WebUser user, String emailId) {
        this.service.persist(user, emailId);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException, DataAccessException {
        WebUser u = findByEmail(s);
        if (s == null || "".equals(s.trim()) || u == null) {
            throw new UsernameNotFoundException(s);
        }
        return new PermissionedUser(u);
    }
    
    
}
