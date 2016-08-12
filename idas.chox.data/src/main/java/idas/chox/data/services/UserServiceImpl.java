package idas.chox.data.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONObject;

import idas.chox.core.model.PasswordHistory;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.UserService;
import idas.chox.core.util.RoleHelper;

public class UserServiceImpl extends BaseDataService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String KBBS_AUTHENTICATION_URL = "https://dashboards.idaschox.com/authentication/GenerateAccessToken";
    private static final String KBBS_INVALIDATE_URL = "https://dashboards.idaschox.com/authentication/InvalidateAccessToken";

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

        return result != null;
    }

    @Override
    public boolean isUserNameExist(String userName, int userId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).add(Restrictions.eq("userName", userName).ignoreCase());
        if (userId > 0) {
            criteria.add(Restrictions.ne("id", userId));
        }
        WebUser result = (WebUser) getByCriteria(criteria);

        return result != null;
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

        List<WebUser> users;
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN).createAlias("this.workgroups", "wgs", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("role.name", selectedUserRole));
        criteria.add(Restrictions.eq("wgs.id", selectedWorkgroupId));
        criteria.add(Restrictions.eq("insurer.id", user.getInsurer().getId()));
        criteria.add(Restrictions.eq("status", true));
        criteria.add(Restrictions.ne("id", user.getId()));

        users = findByCriteria(criteria);

        return users.size() > 0;
    }

    @Override
    public List<WebUser> getActiveClaimHandlersByInsurerWorkgroup(int insurerId, Set<Integer> selectedWorkgroupId, boolean workgroupEnable) {

        List<WebUser> users = new ArrayList<>();

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("role.name", "ROLE_INS_CH"));

        if (workgroupEnable && selectedWorkgroupId.size() > 0 && !selectedWorkgroupId.contains(-1)) {
            criteria.createAlias("this.workgroups", "wgs", CriteriaSpecification.LEFT_JOIN);
            criteria.add(Restrictions.in("wgs.id", selectedWorkgroupId));
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
    public List<WebUser> getAllClaimHandlersByInsurerWorkgroup(int insurerId, Set<Integer> selectedWorkgroupId, boolean workgroupEnable) {

        List<WebUser> users = new ArrayList<>();

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
        criteria.add(Restrictions.eq("role.name", "ROLE_INS_CH"));

        if (workgroupEnable && selectedWorkgroupId.size() > 0 && !selectedWorkgroupId.contains(-1)) {
            criteria.createAlias("this.workgroups", "wgs", CriteriaSpecification.LEFT_JOIN);
            criteria.add(Restrictions.in("wgs.id", selectedWorkgroupId));
        }

        criteria.add(Restrictions.eq("insurer.id", insurerId));
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
        List<WebUser> users = new ArrayList<>();

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class).createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
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

    @Override
    public SearchResult getUsers(int organisationId, int organisationTypeId, int userRoleId, int start, int limit, String sort, String dir, boolean activeUsersOnly) {
        List<WebUser> users = new ArrayList<>();

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
        if (activeUsersOnly) {
            criteria.add(Restrictions.eq("status", true));
        }
        if (organisationTypeId > 0) {
            if (organisationTypeId == 2) {
                if (organisationId > 0) {
                    criteria.add(Restrictions.eq("insurer.id", organisationId));
                } else {
                    criteria.add(Restrictions.isNotNull("insurer.id"));
                }
            } else if (organisationTypeId == 3) {
                if (organisationId > 0) {
                    criteria.add(Restrictions.eq("chorganisation.id", organisationId));
                } else {
                    criteria.add(Restrictions.isNotNull("chorganisation.id"));
                }
            } else if (organisationTypeId == 1) {
                criteria.add(Restrictions.isNull("insurer.id"));
                criteria.add(Restrictions.isNull("chorganisation.id"));
            }

            if (userRoleId > 0) {
                criteria.createAlias("this.roles", "role", CriteriaSpecification.LEFT_JOIN);
                criteria.add(Restrictions.eq("role.id", userRoleId));
            }
        }

        Integer totalCount = totalCount(criteria);

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

        if (userRoleId > 0 /*|| sort.equalsIgnoreCase("role")*/) {
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

        return new SearchResult(users, totalCount, null);
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void saveUser(WebUser user) {
        LOG.debug("Saving user '{}' (password='{}')", user.getFullName(), user.getPassword());
        user.setUserName(user.getUserName().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        save(user);
        LOG.debug("User '{}' saved (password='{}')", user.getFullName(), user.getPassword());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void persist(WebUser user) {
        user.setUserName(user.getUserName().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());
        save(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void updateLastLogin(int userId) {
        WebUser user = (WebUser) get(WebUser.class, userId);
        user.setLastLoginDate(new Date());
        user.setFailedLoginAttempts(0);
        user.setBlocked(false);
        user.setBlockedDate(null);
        save(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void block(int userId) {
        WebUser user = (WebUser) get(WebUser.class, userId);
        user.setBlocked(true);
        user.setBlockedDate(new Date());
        save(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void unblock(int userId) {
        WebUser user = (WebUser) get(WebUser.class, userId);
        user.setBlocked(false);
        user.setBlockedDate(null);
        user.setFailedLoginAttempts(0);
        user.setLastModifiedBy(user);
        save(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public boolean failedLogin(int userId) {
        WebUser user = (WebUser) get(WebUser.class, userId);

        if (!user.isBlocked()) {
            LOG.debug("User '{}' failed login - incrementing failed attempts", user.getDisplayName());
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= user.getMaxFailedLoginAttempts()) {
                LOG.debug("User '{}' blocked as failed login attemts {} > {}", new Object[]{user.getDisplayName(), user.getFailedLoginAttempts(), user.getMaxFailedLoginAttempts()});
                user.setBlocked(true);
                user.setBlockedDate(new Date());
            }
            save(user);
        }

        return user.isBlocked();
    }

    @Override
    public List<PasswordHistory> getPasswordHistory(int userId, int count) {
        List<PasswordHistory> passwordHistory = new ArrayList<>(count);

        if (count > 0) {
            DetachedCriteria criteria = DetachedCriteria.forClass(PasswordHistory.class);
            WebUser user = (WebUser) get(WebUser.class, userId);
            criteria.add(Restrictions.eq("webUser", user));
            criteria.addOrder(Order.desc("createdDate"));

            List<PasswordHistory> results = findByCriteria(criteria);

            if (results != null && !results.isEmpty()) {
                for (int i = 0; i < count && i < results.size(); i++) {
                    passwordHistory.add(results.get(i));
                }
            }
        }

        return passwordHistory;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void savePasswordHistory(PasswordHistory passwordHistory) {
        save(passwordHistory);
    }

    @Override
    public String kbbsAuthenticate(WebUser user) throws IOException {
            String username = getKbbsUsername(user);
            String password = null;
            if (username != null && username.contains("MAN") && user.isAnInsurer()) {
                password = user.getInsurer().getKbbsManagerPassword();
            } else if (username != null && user.isAnInsurer()) {
                password = user.getInsurer().getKbbsOperativePassword();
            } else if (username != null && username.contains("MAN") && user.isCHO()) {
                password = user.getChorganisation().getKbbsManagerPassword();
            } else if (username != null && user.isCHO()) {
                password = user.getChorganisation().getKbbsOperativePassword();
            }
            if (password != null) {
                LOG.debug("Authenticating against KBBS for user '{}' with password '{}'", username, password);
                try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                    HttpPost httppost = new HttpPost(KBBS_AUTHENTICATION_URL);
                    Map paramMap = new HashMap();
                    Map extraDataMap = new HashMap();
                    paramMap.put("UserName", username);
                    paramMap.put("Password", password);
                    extraDataMap.put("UniqueId", user.getId());
                    paramMap.put("ExtraData", extraDataMap);
                    JSONObject jsonObject = JSONObject.fromObject(paramMap);
                    StringEntity requestEntity = new StringEntity(jsonObject.toString());

                    requestEntity.setContentType("application/json");
                    LOG.debug("Setting HTTP POST entity to '{}'", jsonObject.toString());
                    httppost.setEntity(requestEntity);
                    httppost.addHeader("Origin", "https://www.idaschox.com");

                    RequestConfig requestConfig = RequestConfig.custom()
                            .setSocketTimeout(30000)
                            .setConnectTimeout(30000)
                            .setConnectionRequestTimeout(30000)
//                            .setAuthenticationEnabled(true)
                            .build();
                    httppost.setConfig(requestConfig);
                    CookieStore cookieStore = new BasicCookieStore();
                    HttpClientContext context = HttpClientContext.create();
                    context.setCookieStore(cookieStore);

                    LOG.debug("Executing request: {}", httppost.getRequestLine());

                    try (CloseableHttpResponse kbbsResponse = httpclient.execute(httppost, context)) {
                        int statusCode = kbbsResponse.getStatusLine().getStatusCode();
                        if (statusCode == HttpStatus.SC_OK) {
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            String content = responseHandler.handleResponse(kbbsResponse);
                            JSONObject json = JSONObject.fromObject(content);
                            String authenticationToken = json.getString("AuthenticationToken");
                            LOG.debug("----------------------------------------");
                            LOG.debug("KBBS AuthenticationToken for user '{}': {}", user.getFullName(), authenticationToken);
                            LOG.debug("----------------------------------------");
                            return authenticationToken;
                        }
                    } catch (IOException | ParseException e) {
                        LOG.error("Error Parsing response from KBBS authentication: {}", e.getMessage(), e);
                    }
                }
            }
            return null;
    }

    @Override
    public String kbbsInvalidate(String token) throws IOException {
                LOG.debug("Invalidating KBBS authentication token: {}", token);
                try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                    HttpPost httppost = new HttpPost(KBBS_INVALIDATE_URL);
                    Map paramMap = new HashMap();
                    paramMap.put("AuthenticationToken", token);
                    JSONObject jsonObject = JSONObject.fromObject(paramMap);
                    StringEntity requestEntity = new StringEntity(jsonObject.toString());

                    requestEntity.setContentType("application/json");
                    LOG.debug("Setting HTTP POST entity to '{}'", jsonObject.toString());
                    httppost.setEntity(requestEntity);
                    httppost.addHeader("Origin", "https://www.idaschox.com");

                    RequestConfig requestConfig = RequestConfig.custom()
                            .setSocketTimeout(30000)
                            .setConnectTimeout(30000)
                            .setConnectionRequestTimeout(30000)
//                            .setAuthenticationEnabled(true)
                            .build();
                    httppost.setConfig(requestConfig);
                    HttpClientContext context = HttpClientContext.create();

                    LOG.debug("Executing request: {}", httppost.getRequestLine());

                    try (CloseableHttpResponse kbbsResponse = httpclient.execute(httppost, context)) {
                        int statusCode = kbbsResponse.getStatusLine().getStatusCode();
                        if (statusCode == HttpStatus.SC_OK) {
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            String content = responseHandler.handleResponse(kbbsResponse);
                            JSONObject json = JSONObject.fromObject(content);
                            String result = json.toString();
                            LOG.debug("KBBS Invalidate Token Result: {}", result);
                            return result;
                        }
                    } catch (IOException | ParseException e) {
                        LOG.error("Error Parsing response from KBBS InvalidateToken: {}", e.getMessage(), e);
                    }
                }
            return null;
    }

    private String getKbbsUsername(WebUser user) {
        String username = null;

        if (user.isAnInsurer()) {
            if (RoleHelper.isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_INS_MNG)) {
                username = "IMAN." + user.getInsurer().getId();
            } else {
                username = "IOPR." + user.getInsurer().getId();
            }
        } else if (user.isCHO()) {
            if (RoleHelper.isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHO_MNG)) {
                username = "CMAN." + user.getChorganisation().getId();
            } else {
                username = "COPR." + user.getChorganisation().getId();
            }
        }

        return username;
    }


}
