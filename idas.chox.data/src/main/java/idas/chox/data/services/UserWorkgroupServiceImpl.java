package idas.chox.data.services;

import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.services.UserWorkgroupService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class UserWorkgroupServiceImpl extends SecureDataService implements UserWorkgroupService {

    @Override
    public List<WebUserWorkgroup> getUserWorkgroupsByUser(int userId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);

        if (userId > 0) {
            criteria.add(Restrictions.eq("user.id", userId));
        }

        return findByCriteria(criteria);
    }

    @Override
    public List<WebUserWorkgroup> getUserWorkgroupsByWorkgroup(int  wgId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);

        if (wgId > 0) {
            criteria.add(Restrictions.eq("workgroup.id", wgId));
        }

        return findByCriteria(criteria);
    }

    @Override
    public WebUserWorkgroup getUserWorkgroup(int userWorkgroupId) {
        return (WebUserWorkgroup) get(WebUserWorkgroup.class, userWorkgroupId);
    }

    @Override
    public boolean isUserWorkgroupExist(Integer workgroupId, Integer webUserId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        criteria.add(Restrictions.eq("user.id", webUserId));
        if (findByCriteria(criteria).size() > 0) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveUserWorkgroup(WebUserWorkgroup userWorkgroup) {
        save(userWorkgroup);
    }
}
