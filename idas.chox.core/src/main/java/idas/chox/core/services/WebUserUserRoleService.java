package idas.chox.core.services;

import idas.chox.core.model.WebUserUserRole;
import java.util.List;
import java.util.Set;

public interface WebUserUserRoleService {

    // *****************************
    // STANDARD FUNCTOIN
    // *****************************
    public WebUserUserRole getObject(int id);

    public void updateObject(WebUserUserRole object);

    public boolean DeleteObject(WebUserUserRole object);

    // *****************************
    // VALIDATION
    // *****************************
    public boolean addBaseNewUserRole(int webUserId, int typeId);

    public boolean addNewUserRole(int webUserId, int webUserRoleId);

    // *****************************
    // EXTRACT 
    // *****************************    
    public List<WebUserUserRole> getMappedUserRole(Integer webUserId);

    public Set getWebUserroles(int orgTypeId);

    public List getSelectedUserAvailableRoleLookupItem(int orgTypeId, Integer webUserId);

    public List getWebUserrolesLookupItem(int orgTypeId);

    public String getUserroleName(int id);

    public boolean isClaimHandlerRole(int roleId);
}
