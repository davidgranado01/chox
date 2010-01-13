package idas.chox.data.services;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WebUserUserRoleService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class WebUserUserRoleServiceImpl extends BaseDataService implements WebUserUserRoleService {

    public WebUserUserRoleServiceImpl() {
    }
    protected UserService userService;
    protected WebUserUserRoleService webUserUserRoleService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setWebUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    public WebUserUserRole getWebUserUserRole(int id) {
        return (WebUserUserRole) get(WebUserUserRole.class, id);
    }

    public String getUserroleName(int id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("id", id));
        WebUserRole object = (WebUserRole) getByCriteria(criteria);
        return object.getName();
    }

    public List<WebUserUserRole> getMappedUserRole(Integer webUserId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserUserRole.class);
        criteria.add(Restrictions.eq("webUser.id", webUserId));
        return findByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveWebUserUserRole(WebUserUserRole object) {
        save(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void addNewUserRole(int webUserId, int webUserRoleId) {
        WebUserUserRole webUserUserRole = new WebUserUserRole();
        webUserUserRole.setActive(true);
        webUserUserRole.setWebUser(userService.getWebUser(webUserId));
        webUserUserRole.setWebUserRole(getWebUserRole(webUserRoleId));
        saveWebUserUserRole(webUserUserRole);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void addBaseNewUserRole(int webUserId, int typeId) {
        WebUserUserRole webUserUserRole = new WebUserUserRole();
        webUserUserRole.setActive(true);
        webUserUserRole.setWebUser(userService.getWebUser(webUserId));
        webUserUserRole.setWebUserRole(getUserOrgBaseRoleId(typeId));
        saveWebUserUserRole(webUserUserRole);
    }

    private WebUserRole getUserOrgBaseRoleId(int orgTypeId) {

        String webUserRoleName = "-";

        switch (orgTypeId) {
            case 1:
                webUserRoleName = WebUserRole.ROLE_CHOX;
                break;
            case 2:
                webUserRoleName = WebUserRole.ROLE_INS;
                break;
            case 3:
                webUserRoleName = WebUserRole.ROLE_CHO;
                break;
        }

        return getWebUserRole(webUserRoleName);

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteWebUserUserRole(WebUserUserRole object) {
        delete(object);
    }

    private WebUserRole getWebUserRole(String roleName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("name", roleName));
        return (WebUserRole) getByCriteria(criteria);
    }

    private WebUserRole getWebUserRole(int webUserRoleId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("id", webUserRoleId));
        return (WebUserRole) getByCriteria(criteria);
    }

    public Set getWebUserroles(int orgTypeId) {
        Set webUserRoles = new HashSet<WebUserRole>();
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("typeId", orgTypeId));
        criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHO));
        criteria.add(Restrictions.ne("name", WebUserRole.ROLE_INS));
        criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHOX));
        webUserRoles.addAll(findByCriteria(criteria));
        return webUserRoles;
    }

    public List getWebUserrolesLookupItem(int orgTypeId) {

        Set webUserroles = getWebUserroles(orgTypeId);
        List items = new ArrayList<IdLookupItem>();

        Iterator itr = webUserroles.iterator();
        while (itr.hasNext()) {
            WebUserRole s = (WebUserRole) itr.next();
            items.add(new IdLookupItem(s.getId(), s.getDescription()));
        }

        return items;
    }

    public List getSelectedUserAvailableRoleLookupItem(int orgTypeId, Integer webUserId) {

        List items = new ArrayList<IdLookupItem>();
        Set webUserroles = getWebUserroles(orgTypeId);
        List<WebUserUserRole> selectedWebUserroles = getMappedUserRole(webUserId);
        List<Integer> selectedList = new ArrayList<Integer>();

        for (WebUserUserRole o : selectedWebUserroles) {
            selectedList.add(o.getWebUserRole().getId());
        }

        Iterator itr = webUserroles.iterator();
        while (itr.hasNext()) {
            WebUserRole s = (WebUserRole) itr.next();
            if (!selectedList.contains(s.getId())) {
                items.add(new IdLookupItem(s.getId(), s.getDescription()));
            }
        }

        return items;
    }

    public boolean isClaimHandlerRole(int roleId) {

        boolean isClaimHandler = false;
        WebUserRole webUserRole = new WebUserRole();

        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("id", roleId));

        criteria.add(Restrictions.disjunction().add(Restrictions.eq("name", WebUserRole.ROLE_CH)).add(Restrictions.eq("name", WebUserRole.ROLE_COM)).add(Restrictions.eq("name", WebUserRole.ROLE_FNOL)));

        webUserRole = (WebUserRole) getByCriteria(criteria);

        if (webUserRole != null) {
            isClaimHandler = true;
        }

        return isClaimHandler;
    }
}
