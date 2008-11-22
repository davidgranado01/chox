package chox.services;

import chox.model.WebUser;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class UserServiceImpl  extends DataService implements UserService {

    public UserServiceImpl() {
    }

    public WebUser findByEmail(String email) {

        currentSession.beginTransaction();
        Criteria criteria = currentSession.createCriteria(WebUser.class).add(Restrictions.eq("email", email));
        WebUser result = (WebUser) criteria.uniqueResult();
        currentSession.getTransaction().commit();
        return result;
    }

    public void persist(WebUser user, String emailId) {
    }

    public WebUser loadUserByUsername(String s) {
        WebUser u = findByEmail(s);
        return u;
    }
}
