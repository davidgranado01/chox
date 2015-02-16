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

public class WebUserUserRoleServiceImpl extends SecureDataService implements WebUserUserRoleService {

    protected UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public WebUserUserRole getWebUserUserRole(int id) {
        return (WebUserUserRole) get(WebUserUserRole.class, id);
    }

    @Override
    public String getUserRoleName(int id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("id", id));
        WebUserRole object = (WebUserRole) getByCriteria(criteria);
        return object.getName();
    }

    @Override
    public List<WebUserUserRole> getMappedUserRole(Integer webUserId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserUserRole.class);
        criteria.add(Restrictions.eq("webUser.id", webUserId));
        return findByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveWebUserUserRole(WebUserUserRole object) {
        save(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void addNewUserRole(int webUserId, int webUserRoleId) {
        WebUserUserRole webUserUserRole = new WebUserUserRole();
        webUserUserRole.setActive(true);
        webUserUserRole.setWebUser(userService.getWebUser(webUserId));
        webUserUserRole.setWebUserRole(getWebUserRole(webUserRoleId));
        saveWebUserUserRole(webUserUserRole);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteWebUserUserRole(WebUserUserRole object) {
        delete(object);
    }

    @Override
    public WebUserRole getWebUserRole(String roleName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("name", roleName));
        return (WebUserRole) getByCriteria(criteria);
    }

    private WebUserRole getWebUserRole(int webUserRoleId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("id", webUserRoleId));
        return (WebUserRole) getByCriteria(criteria);
    }

    @Override
    public Set<WebUserRole> getWebUserRoles(int orgTypeId, boolean isWorkgroupEnabled,
                                boolean isClaimownershipEnabled, boolean isFnolEnabled,
                                boolean isEngineersEnabled, boolean isInsurerUploadEnabled,
                                boolean isSupervisorEnabled, boolean isAdmin, boolean canBeAssignedTasksOnly) {
        Set<WebUserRole> webUserRoles = new HashSet<WebUserRole>();
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("typeId", orgTypeId));
        criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHO));
        criteria.add(Restrictions.ne("name", WebUserRole.ROLE_INS));
        criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHOX));
        if (!isWorkgroupEnabled) {
            criteria.add(Restrictions.ne("showWorkgroupDisabled", false));
        }
        if (!isClaimownershipEnabled) {
            criteria.add(Restrictions.ne("showOwnershipDisabled", false));
        }
        if (!isFnolEnabled) {
            criteria.add(Restrictions.ne("fnolRelated", true));
        }
        if (!isEngineersEnabled) {
            criteria.add(Restrictions.ne("engineerRelated", true));
        }
        if (!isInsurerUploadEnabled) {
            criteria.add(Restrictions.ne("showInsurerUploadDisabled", false));
        }
        if (!isSupervisorEnabled) {
            criteria.add(Restrictions.ne("name", WebUserRole.ROLE_INS_SUP));
        }
        if (!isAdmin) {
            criteria.add(Restrictions.ne("showAdminOnly", true));
        }
        if (canBeAssignedTasksOnly) {
            criteria.add(Restrictions.eq("canBeAssignedTasks", true));
        }
        webUserRoles.addAll(findByCriteria(criteria));
        return webUserRoles;
    }

    @Override
    public List<IdLookupItem> getWebUserRolesLookupItem(int orgTypeId, boolean isWorkgroupEnabled,
                                boolean isClaimownershipEnabled, boolean isFnolEnabled,
                                boolean isEngineersEnabled, boolean isInsurerUploadEnabled,
                                boolean isSupervisorEnabled, boolean isAdmin) {

        Set<WebUserRole> webUserroles = getWebUserRoles(orgTypeId, isWorkgroupEnabled, isClaimownershipEnabled, isFnolEnabled, isEngineersEnabled, isInsurerUploadEnabled, isSupervisorEnabled, isAdmin, false);
        List<IdLookupItem> items = new ArrayList<IdLookupItem>();

        Iterator itr = webUserroles.iterator();
        while (itr.hasNext()) {
            WebUserRole s = (WebUserRole) itr.next();
            items.add(new IdLookupItem(s.getId(), s.getDescription()));
        }

        return items;
    }

    @Override
    public List<IdLookupItem> getSelectedUserAvailableRoleLookupItem(int orgTypeId, Integer webUserId,
                                boolean isWorkgroupEnabled, boolean isClaimownershipEnabled,
                                boolean isFnolEnabled, boolean isEngineersEnabled,
                                boolean isInsurerUploadEnabled, boolean isSupervisorEnabled,
                                boolean isAdmin) {

        List<IdLookupItem> items = new ArrayList<IdLookupItem>();
        Set<WebUserRole> webUserroles = getWebUserRoles(orgTypeId, isWorkgroupEnabled, isClaimownershipEnabled, isFnolEnabled, isEngineersEnabled, isInsurerUploadEnabled, isSupervisorEnabled, isAdmin, false);

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

    @Override
    public boolean isWorkgroupRelatedRoles(int roleId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("id", roleId));
        criteria.add(Restrictions.eq("workgroupRelated", true));
        WebUserRole webUserRole = (WebUserRole) getByCriteria(criteria);
        if (webUserRole != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isClaimOwnerRelatedRoles(int roleId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("id", roleId));
        criteria.add(Restrictions.eq("ownershipRelated", true));
        WebUserRole webUserRole = (WebUserRole) getByCriteria(criteria);
        if (webUserRole != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isWorkgroupRelatedRolesByCode(String roleCode) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("name", roleCode));
        criteria.add(Restrictions.eq("workgroupRelated", true));
        WebUserRole webUserRole = (WebUserRole) getByCriteria(criteria);
        if (webUserRole != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isClaimOwnerRelatedRolesByCode(String roleCode) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);
        criteria.add(Restrictions.eq("name", roleCode));
        criteria.add(Restrictions.eq("ownershipRelated", true));
        WebUserRole webUserRole = (WebUserRole) getByCriteria(criteria);
        if (webUserRole != null) {
            return true;
        }
        return false;
    }
}