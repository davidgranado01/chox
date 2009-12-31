package idas.chox.data.services;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.UserService;
import idas.chox.core.util.RoleHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

public class UserServiceImpl extends BaseDataService implements UserService {

    public UserServiceImpl() {
    }

    public WebUser findByEmail(String email) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email).ignoreCase());
        criteria.add(Restrictions.eq("status", true));
        WebUser result = (WebUser) getByCriteria(criteria);
        return result;
    }

    public boolean isEmailExist(String email) {

        boolean bFlag = true;

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email).ignoreCase());
        WebUser result = (WebUser) getByCriteria(criteria);

        if (result == null) {
            bFlag = false;
        }

        return bFlag;
    }

    public boolean isEmailExist(String email, int userId) {

        boolean bFlag = true;

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email).ignoreCase());
        criteria.add(Restrictions.ne("id", userId));
        WebUser result = (WebUser) getByCriteria(criteria);

        if (result == null) {
            bFlag = false;
        }

        return bFlag;
    }

    public WebUser loadUserByEmail(String email) {
        WebUser u = findByEmail(email);
        return u;
    }

    public WebUser findByUserName(String userName) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("userName", userName).ignoreCase());
        criteria.add(Restrictions.eq("status", true));
        WebUser result = (WebUser) getByCriteria(criteria);
        return result;
    }

    public boolean isUserNameExist(String userName) {

        boolean bFlag = true;

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("userName", userName).ignoreCase());
        WebUser result = (WebUser) getByCriteria(criteria);

        if (result == null) {
            bFlag = false;
        }

        return bFlag;
    }

    public boolean isUserNameExist(String userName, int userId) {

        boolean bFlag = true;

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("userName", userName).ignoreCase());

        if (userId > 0) {
            criteria.add(Restrictions.ne("id", userId));
        }

        WebUser result = (WebUser) getByCriteria(criteria);

        if (result == null) {
            bFlag = false;
        }

        return bFlag;
    }

    public WebUser loadUserByUsername(String userName) {
        WebUser u = findByUserName(userName);
        return u;
    }

    public void persist(WebUser user, String emailId) {
        this.save(user);
    }

    public WebUser getWebUser(int id) {
        WebUser user = new WebUser();
        user = (WebUser) get(WebUser.class, id);
        return user;
    }

    public Long getNumChoActiveUser(Integer choId) {
        String q = "select count(*) from WebUser where status = true and chorganisation.id = " + choId.toString();
        return getCount(q);
    }

    public Long getNumInsActiveUser(Integer insId) {
        String q = "select count(*) from WebUser where status = true and insurer.id = " + insId.toString();
        return getCount(q);
    }

    public List<WebUser> getUsers() {

        List<WebUser> users = new ArrayList<WebUser>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            criteria.addOrder(Order.asc("userName"));
            users = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return users;
    }

    /*
     * GET SELECTED USER ROLE
     * GET SELECTED USER WORKGROUPS
     * IF OTHER USERS WITH SAME ROLE AND
     * THOSE USERS HAVING SAME WORKGROUPS EXISTS
     */
    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, String selectedUserRole) {

        boolean isExist = false;

        if (user.getWorkgroupIds().size() > 0) {
            Iterator itr = user.getWorkgroupIds().iterator();
            while (itr.hasNext()) {
                int workgroupId = (Integer) itr.next();
                if (isWorkgroupOwnByOtherUserByRole(user, workgroupId, selectedUserRole)) {
                    isExist = true;
                }
            }
        }

        return isExist;
    }

    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, int selectedWorkgroupId, String selectedUserRole) {

        boolean bFlag = false;
        List<WebUser> users = new ArrayList<WebUser>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN).createAlias("this.workgroups", "wgs", CriteriaSpecification.LEFT_JOIN);

            criteria.add(Restrictions.eq("role.name", selectedUserRole));
            criteria.add(Restrictions.eq("wgs.id", selectedWorkgroupId));
            criteria.add(Restrictions.eq("insurer.id", user.getInsurer().getId()));
            criteria.add(Restrictions.eq("status", true));
            criteria.add(Restrictions.ne("id", user.getId()));

            users = findByCriteria(criteria);
            if (users.size() > 0) {
                bFlag = true;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return bFlag;
    }

    public List<WebUser> getClaimHanldersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable) {

        List<WebUser> users = new ArrayList<WebUser>();

        try {

            Criteria criteria = getSession().createCriteria(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
            criteria.add(Restrictions.eq("role.name", "ROLE_INS_CH"));

            if (workgroupEnable && selectedWorkgroupId > 0) {
                criteria.createAlias("this.workgroups", "wgs", CriteriaSpecification.LEFT_JOIN);
                criteria.add(Restrictions.eq("wgs.id", selectedWorkgroupId));
            }

            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("status", true));
            criteria.addOrder(Order.asc("firstName"));

            criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<HashMap> resultMap = criteria.list();

            for (HashMap m : resultMap) {
                users.add((WebUser) m.get("this"));
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<WebUser> getClaimHanldersByInsurer(int insurerId, boolean workgroupEnable) {

        List<WebUser> users = new ArrayList<WebUser>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            criteria.add(Restrictions.eq("insurer.id", insurerId));
            criteria.add(Restrictions.eq("status", true));
            criteria.addOrder(Order.asc("firstName"));
            users = findByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        List<WebUser> claimHandlers = new ArrayList<WebUser>();

        if (!workgroupEnable) {

            claimHandlers = users;

        } else {

            for (WebUser wu : users) {
                if (RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_CH)) {
                    claimHandlers.add(wu);
                }
            }
        }

        return claimHandlers;
    }

    public List<WebUser> getUsers(int organisationId, int organisationTypeId, int userRoleId) {
        
        List<WebUser> users = new ArrayList<WebUser>();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            
            if (organisationTypeId > 0 && organisationId > 0) {
                if (organisationTypeId==2) {
                    criteria.add(Restrictions.eq("insurer.id", organisationId));
                } else if (organisationTypeId==3) {
                    criteria.add(Restrictions.eq("chorganisation.id", organisationId));
                }
            }

            criteria.addOrder(Order.asc("userName"));
            
            List<WebUser> userData = findByCriteria(criteria);                        
            for (WebUser h : userData) {
                if (h.getOrganisationType().equalsIgnoreCase(OrganisationType.getOrganisationType(organisationTypeId))) {
                    users.add(h);
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return users;
    }

    public WebUser getUsers(int id) {

        return (WebUser) get(WebUser.class, id);
    }

    public boolean updateObject(WebUser object) {
        boolean bFlag = false;
        try {
            object.setEmail(object.getEmail().toLowerCase());
            save(object);
            bFlag = true;

        } catch (Throwable e) {
            bFlag = false;
            e.printStackTrace();
        }

        return bFlag;
    }
}
