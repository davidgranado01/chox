package idas.chox.core.services;

import idas.chox.core.model.WebUser;
import java.util.List;

public interface UserService {

    public WebUser findByEmail(String email);

    public WebUser findByUserName(String userName);

    public boolean isUserNameExist(String userName);

    public boolean isUserNameExist(String userName, int userId);

    public WebUser loadUserByUsername(String userName);

    public void persist(WebUser user);

    public WebUser getWebUser(int id);

    public Long getNumChoActiveUser(Integer choId);

    public Long getNumInsActiveUser(Integer insId);

    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, String selectedUserRole);

    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, int selectedWorkgroupId, String selectedUserRole);

    public List<WebUser> getClaimHanldersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable);

    public List<WebUser> getUsers(int organisationId, int organisationTypeId, int userRoleId);

    public List<WebUser> getUsers();

    public void saveUser(WebUser user);
}
