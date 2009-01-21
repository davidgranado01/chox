package chox.services;

import chox.model.WebUser;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class UserServiceImpl  extends DataService implements UserService {

    public UserServiceImpl() {
    }

    public WebUser findByEmail(String email) {

        Criteria criteria = getCurrentSession().createCriteria(WebUser.class).add(Restrictions.eq("email", email));
        WebUser result = (WebUser) criteria.uniqueResult();
        return result;
    }

    public void persist(WebUser user, String emailId) {
    }

    public WebUser loadUserByUsername(String s) {
        WebUser u = findByEmail(s);
        return u;
    }
    
    public WebUser getObject(int id)
    {
        UserCacheManager cacheManager = UserCacheManager.getInstance();
        WebUser user = cacheManager.getUserFromCache(id);
        if(user == null){
            user = (WebUser)getCurrentSession().get(WebUser.class, id);
            cacheManager.putUserToCache(user);
        }
        
        return user;
    }
}
