package chox.services;

import chox.model.WebUserRole;
import chox.model.WebUserUserRole;
import java.util.List;

public interface WebUserUserRoleService {
    public WebUserUserRole getObject(int id);
    public void updateObject(WebUserUserRole object);
    public boolean addBaseNewUserRole(int webUserId, int typeId);
    public boolean addNewUserRole(int webUserId, int webUserRoleId);
    public List<WebUserUserRole> getUserRoleMapping(Integer webUserId, Integer webUserRoleId);
    public boolean DeleteObject(WebUserUserRole object);
    public List<WebUserRole> getWebUserroles(int orgTypeId);
    public List getSelectedUserAvailableRole(int orgTypeId, int webUserId);
}
