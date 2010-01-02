package idas.chox.data.services;

import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.services.UserWorkgroupService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class UserWorkgroupServiceImpl extends SecureDataService implements UserWorkgroupService {

    public List<WebUserWorkgroup> getUserWorkgroupsByUser(int userId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);

        if (userId > 0) {
            criteria.add(Restrictions.eq("user.id", userId));
        }

        return findByCriteria(criteria);
    }

    public WebUserWorkgroup getUserWorkgroup(int userWorkgroupId) {
        return (WebUserWorkgroup) get(WebUserWorkgroup.class, userWorkgroupId);
    }

    public void deleteWebUserWorkgroup(WebUserWorkgroup webUserWorkgroup) {
        delete(webUserWorkgroup);
    }

    public boolean isUserWorkgroupExist(Integer workgroupId, Integer webUserId) {

        boolean isExist = false;

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserWorkgroup.class);

        if (workgroupId != null && workgroupId > 0) {
            criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        }

        if (webUserId != null && webUserId > 0) {
            criteria.add(Restrictions.eq("user.id", webUserId));
        }

        if (findByCriteria(criteria).size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    public void saveWebUserWorkgroup(WebUserWorkgroup webUserWorkgroup) {
        save(webUserWorkgroup);
    }
}
