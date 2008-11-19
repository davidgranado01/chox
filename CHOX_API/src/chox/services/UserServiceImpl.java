package chox.services;

import chox.data.HibernateUtil;
import chox.model.WebUser;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UserServiceImpl implements UserService {

    public UserServiceImpl() {
    }

    public WebUser findByEmail(String email) {

        Session currentSession = HibernateUtil.currentSession();
        Criteria criteria = currentSession.createCriteria(WebUser.class).add(Restrictions.eq("email", email));
        WebUser result = (WebUser) criteria.uniqueResult();
        return result;
    }

    public void persist(WebUser user, String emailId) {
    }

    public WebUser loadUserByUsername(String s) {
        WebUser u = findByEmail(s);
        return u;
    }
}
