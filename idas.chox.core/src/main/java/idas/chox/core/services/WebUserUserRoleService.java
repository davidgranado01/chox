package idas.chox.core.services;

import java.util.List;
import java.util.Set;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;

public interface WebUserUserRoleService {

    WebUserUserRole getWebUserUserRole(int webUserUserRoleId);

    void saveWebUserUserRole(WebUserUserRole webUserUserRole);

    void deleteWebUserUserRole(WebUserUserRole webUserUserRole);

    void addBaseNewUserRole(int webUserId, int typeId);

    void addNewUserRole(int webUserId, int webUserRoleId);

    List<WebUserUserRole> getMappedUserRole(Integer webUserId);

    Set<WebUserRole> getWebUserRoles(int orgTypeId, boolean isWorkgroupEnabled, boolean isClaimownershipEnabled, boolean isFnolEnabled, boolean isEngineersEnabled, boolean isInsurerUploadEnabled, boolean isSupervisorEnabled, boolean isAdmin, boolean canBeAssignedTasksOnly);

    List<IdLookupItem> getSelectedUserAvailableRoleLookupItem(int orgTypeId, Integer webUserId, boolean isWorkgroupEnabled, boolean isClaimownershipEnabled, boolean isFnolEnabled, boolean isEngineersEnabled, boolean isInsurerUploadEnabled, boolean isSupervisorEnabled, boolean isAdmin);

    List<IdLookupItem> getWebUserRolesLookupItem(int orgTypeId, boolean isWorkgroupEnabled, boolean isClaimownershipEnabled, boolean isFnolEnabled, boolean isEngineersEnabled, boolean isInsurerUploadEnabled, boolean isSupervisorEnabled, boolean isAdmin);

    String getUserRoleName(int id);

    boolean isWorkgroupRelatedRoles(int roleId);

    boolean isClaimOwnerRelatedRoles(int roleId);

    boolean isWorkgroupRelatedRolesByCode(String roleCode);

    boolean isClaimOwnerRelatedRolesByCode(String roleCode);
    
    WebUserRole getWebUserRole(String roleName);
}
