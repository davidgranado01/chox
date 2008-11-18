package chox.web.security;

import chox.model.User;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

public class WebUserService implements UserDetailsService {

    public WebUserService() {
    }

    public User findByEmail(String email) {
        User u = new User();
        u.setEmail(email);
        u.setPassword("123");
        return u;
    }

    public void persist(User user, String emailId) {
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException, DataAccessException {
        User u = findByEmail(s);
        if (s == null || "".equals(s.trim()) || u == null) {
            throw new UsernameNotFoundException(s);
        }
        return new PermissionedUser(u);
    }
    
    
}
