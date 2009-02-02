package chox.services;

import chox.model.WebUser;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class UserServiceImpl extends HibernateDaoSupport implements UserService {

    public UserServiceImpl() {
    }

    public WebUser findByEmail(String email) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email));
        WebUser result = (WebUser) getByCriteria(criteria);
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
            user = (WebUser)get(WebUser.class, id);
            cacheManager.putUserToCache(user);
        }
        
        return user;
    }
    
     private Object get(final Class c, final int id) {

        return getHibernateTemplate().get(c, id);
    }
     
     private Object getByCriteria(final DetachedCriteria c) {

        List result = getHibernateTemplate().findByCriteria(c);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
