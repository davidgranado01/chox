package chox.services;

import chox.model.WebUser;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class UserServiceImpl extends DataService implements UserService {

    public UserServiceImpl() {
    }

    public WebUser findByEmail(String email) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email));
        criteria.add(Restrictions.eq("status", 1));
        WebUser result = (WebUser) getByCriteria(criteria);
        return result;
    }

    public void persist(WebUser user, String emailId) {
        this.save(user);
    }

    public WebUser loadUserByUsername(String s) {
        WebUser u = findByEmail(s);
        return u;
    }

    public WebUser getObject(int id) {
        UserCacheManager cacheManager = UserCacheManager.getInstance();
        WebUser user = cacheManager.getUserFromCache(id);
        if (user == null) {
            user = (WebUser) get(WebUser.class, id);
            cacheManager.putUserToCache(user);
        }

        return user;
    }

    public Long getNumChoActiveUser(Integer choId) {
        String q = "select count(*) from WebUser where status = 1 and chorganisation.id = " + choId.toString();
        return getCount(q);
    }

    public Long getNumInsActiveUser(Integer insId) {
        String q = "select count(*) from WebUser where status = 1 and insurer.id = " + insId.toString();
        return getCount(q);
    } 
    
    public List<WebUser> getUsers(int start, int limit){
        
        List<WebUser> users = new ArrayList<WebUser>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            criteria.addOrder(Order.asc("email"));      
            users = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return users;
    } 
    
    public void updateObject(WebUser object) {
        try {
            save(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }          
    }    
}
