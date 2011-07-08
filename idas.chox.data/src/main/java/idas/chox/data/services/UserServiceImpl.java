package idas.chox.data.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.WebUser;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.UserService;
import java.math.BigInteger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;

public class UserServiceImpl extends BaseDataService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
    }

    @Override
    public WebUser findByEmail(String email) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("email", email).ignoreCase());
        criteria.add(Restrictions.eq("status", true));
        WebUser result = (WebUser) getByCriteria(criteria);
        return result;
    }

    @Override
    public WebUser findByUserName(String userName) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("userName", userName).ignoreCase());
        criteria.add(Restrictions.eq("status", true));
        WebUser result = (WebUser) getByCriteria(criteria);
        return result;
    }

    @Override
    public boolean isUserNameExist(String userName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("userName", userName).ignoreCase());
        WebUser result = (WebUser) getByCriteria(criteria);
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isUserNameExist(String userName, int userId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("userName", userName).ignoreCase());
        if (userId > 0) {
            criteria.add(Restrictions.ne("id", userId));
        }
        WebUser result = (WebUser) getByCriteria(criteria);
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public WebUser loadUserByUsername(String userName) {
        return findByUserName(userName);
    }

    @Override
    public Long getNumChoActiveUser(Integer choId) {
        String q = "select count(*) from WebUser where status = true and chorganisation.id = " + choId.toString();
        return getCount(q);
    }

    @Override
    public Long getNumInsActiveUser(Integer insId) {
        String q = "select count(*) from WebUser where status = true and insurer.id = " + insId.toString();
        return getCount(q);
    }

    @Override
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

    @Override
    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, int selectedWorkgroupId, String selectedUserRole) {

        List<WebUser> users = new ArrayList<WebUser>();
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN).createAlias("this.workgroups", "wgs", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("role.name", selectedUserRole));
        criteria.add(Restrictions.eq("wgs.id", selectedWorkgroupId));
        criteria.add(Restrictions.eq("insurer.id", user.getInsurer().getId()));
        criteria.add(Restrictions.eq("status", true));
        criteria.add(Restrictions.ne("id", user.getId()));

        users = findByCriteria(criteria);
        if (users.size() > 0) {
            return true;
        }

        return false;
    }

    @Override
    public List<WebUser> getClaimHanldersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable) {

        List<WebUser> users = new ArrayList<WebUser>();

        Criteria criteria = getSession().createCriteria(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("role.name", "ROLE_INS_CH"));

        if (workgroupEnable && selectedWorkgroupId > 0) {
            criteria.createAlias("this.workgroups", "wgs", CriteriaSpecification.LEFT_JOIN);
            criteria.add(Restrictions.eq("wgs.id", selectedWorkgroupId));
        }

        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("lastName"));

        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<HashMap> resultMap = criteria.list();

        for (HashMap m : resultMap) {
            users.add((WebUser) m.get("this"));
        }

        return users;
    }

    @Override
    public List<WebUser> getOprUsersByChorganisation(int chorganisationId) {
        List<WebUser> users = new ArrayList<WebUser>();

        Criteria criteria = getSession().createCriteria(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("role.name", "ROLE_CHO_OPR"));
        criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("lastName"));

        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<HashMap> resultMap = criteria.list();

        for (HashMap m : resultMap) {
            users.add((WebUser) m.get("this"));
        }

        return users;
    }

    private void addSort(Criteria criteria, String sort, String dir) {
        if (dir.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(sort));
        } else {
            criteria.addOrder(Order.asc(sort));
        }
    }

    private Integer countClaims(Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        List totalCountResult = criteria.list();
        criteria.setProjection(null);
        return (Integer) totalCountResult.get(0);
    }

    @Override
    public SearchResult getUsers(int organisationId, int organisationTypeId, int userRoleId, int start, int limit, String sort, String dir) {
        List<WebUser> users = new ArrayList<WebUser>();

        Criteria criteria = getSession().createCriteria(WebUser.class);
        if (organisationTypeId > 0) {
            if (organisationTypeId == 2) {
                if (organisationId > 0) {
                    criteria.add(Restrictions.eq("insurer.id", organisationId));
                    if (userRoleId > 0) {
                        criteria.createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
                        criteria.add(Restrictions.eq("role.id", userRoleId));
//                    total = (BigInteger) getSession().createSQLQuery("select count(*) from web_user, web_user_user_role, web_user_role where web_user.id = web_user_user_role.web_user_id and web_user.insurer_id=" + organisationId + " and web_user_role.id = web_user_user_role.web_user_role_id and web_user_role.id =" + userRoleId).uniqueResult();
                    }
                } else {

                    criteria.add(Restrictions.isNotNull("insurer.id"));
                }

            } else if (organisationTypeId == 3) {
                if (organisationId > 0) {
                    criteria.add(Restrictions.eq("chorganisation.id", organisationId));
                    if (userRoleId > 0) {
                        criteria.createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
                        criteria.add(Restrictions.eq("role.id", userRoleId));
//                    total = (BigInteger) getSession().createSQLQuery("select count(*) from web_user, web_user_user_role, web_user_role where web_user.id = web_user_user_role.web_user_id and web_user.chorganisation_id=" + organisationId + " and web_user_role.id = web_user_user_role.web_user_role_id and web_user_role.id =" + userRoleId).uniqueResult();
                    }
                } else {
                    criteria.add(Restrictions.isNotNull("chorganisation.id"));
                }

            } else if (organisationTypeId == 1) {
                criteria.add(Restrictions.isNull("insurer.id"));
                criteria.add(Restrictions.isNull("chorganisation.id"));
                if (userRoleId > 0) {
                    criteria.createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
                    criteria.add(Restrictions.eq("role.id", userRoleId));
                }
            }
        }

        Integer totalCount = countClaims(criteria);


        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);

        if (!sort.isEmpty() && !dir.isEmpty()) {
            if (sort.equalsIgnoreCase("userName")) {
                addSort(criteria, "userName", dir);
            } else if (sort.equalsIgnoreCase("name")) {
                addSort(criteria, "firstName", dir);
                addSort(criteria, "lastName", dir);
            } else if (sort.equalsIgnoreCase("email")) {
                addSort(criteria, "email", dir);
            } else if (sort.equalsIgnoreCase("orgName")) {
                if (organisationTypeId == 2) {
                    addSort(criteria, "insurer.id", dir);
                } else if (organisationTypeId == 3) {
                    addSort(criteria, "chorganisation.id", dir);
                }
            } else if (sort.equalsIgnoreCase("statusDesc")) {
                addSort(criteria, "status", dir);
            } else if (sort.equalsIgnoreCase("role")) {
                if (userRoleId <= 0) {
                    criteria.createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
                }
                addSort(criteria, "role.id", dir);
            } else if (sort.equalsIgnoreCase("isExpired")) {
                addSort(criteria, "isExpired", dir);
            } else if (sort.equalsIgnoreCase("lastLoginDate")) {
                addSort(criteria, "lastLoginDate", dir);
            } else if (sort.equalsIgnoreCase("createdDate")) {
                addSort(criteria, "createdDate", dir);
            } else if (sort.equalsIgnoreCase("createdBy")) {
                addSort(criteria, "createdBy", dir);
            } else if (sort.equalsIgnoreCase("id")) {
                addSort(criteria, "id", dir);
            }
        } else {
            criteria.addOrder(Order.asc("userName"));
        }

        if (userRoleId > 0 || sort.equalsIgnoreCase("role")) {
            criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<HashMap> resultMap = criteria.list();

            for (HashMap m : resultMap) {
                users.add((WebUser) m.get("this"));
            }
        } else {
            List<WebUser> userData = criteria.list();

            for (WebUser m : userData) {
                users.add(m);
            }

        }



//        
//        for (WebUser h : userData) {
//            if (h.getOrganisationType().equalsIgnoreCase(OrganisationType.getOrganisationType(organisationTypeId))) {
//                if (!h.getUserName().equals("system")) {
//                    users.add(h);
//                }
//            }
//        }
        return new SearchResult(users, totalCount);

    }

    @Override
    public List<WebUser> getUsers() {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
        return findByCriteria(criteria);
    }

    @Override
    public WebUser getWebUser(int id) {
        WebUser user = (WebUser) get(WebUser.class, id);
        return user;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(WebUser user) {
        user.setUserName(user.getUserName().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        save(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void persist(WebUser user) {
        user.setUserName(user.getUserName().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        save(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void updateLastLogin(int userId) {
        WebUser user = (WebUser) get(WebUser.class, userId);
        user.setLastLoginDate(new Date());
        save(user);
    }
}
