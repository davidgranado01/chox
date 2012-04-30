package idas.chox.core.services;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import java.util.List;
import java.util.Set;

public interface WebUserUserRoleService {

    public WebUserUserRole getWebUserUserRole(int webUserUserRoleId);

    public void saveWebUserUserRole(WebUserUserRole webUserUserRole);

    public void deleteWebUserUserRole(WebUserUserRole webUserUserRole);

    public void addBaseNewUserRole(int webUserId, int typeId);

    public void addNewUserRole(int webUserId, int webUserRoleId);

    public List<WebUserUserRole> getMappedUserRole(Integer webUserId);

    public Set<WebUserRole> getWebUserroles(int orgTypeId, boolean isWorkgroupEnabled, boolean isClaimownershipEnabled, boolean isFnolEnabled, boolean isEngineersEnabled, boolean isInsurerUploadEnabled, boolean isSupervisorEnabled);

    public List<IdLookupItem> getSelectedUserAvailableRoleLookupItem(int orgTypeId, Integer webUserId, boolean isWorkgroupEnabled, boolean isClaimownershipEnabled, boolean isFnolEnabled, boolean isEngineersEnabled, boolean isInsurerUploadEnabled, boolean isSupervisorEnabled);

    public List<IdLookupItem> getWebUserrolesLookupItem(int orgTypeId, boolean isWorkgroupEnabled, boolean isClaimownershipEnabled, boolean isFnolEnabled, boolean isEngineersEnabled, boolean isInsurerUploadEnabled, boolean isSupervisorEnabled);

    public String getUserroleName(int id);

    public boolean isWorkgroupRelatedRoles(int roleId);

    public boolean isClaimOwnerRelatedRoles(int roleId);

    public boolean isWorkgroupRelatedRolesByCode(String roleCode);

    public boolean isClaimOwnerRelatedRolesByCode(String roleCode);
}
