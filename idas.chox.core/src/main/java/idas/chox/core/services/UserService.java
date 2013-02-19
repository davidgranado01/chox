package idas.chox.core.services;

import idas.chox.core.model.PasswordHistory;
import idas.chox.core.model.WebUser;
import idas.chox.core.search.SearchResult;
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

    public List<WebUser> getActiveClaimHandlersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable);

    public List<WebUser> getAllClaimHandlersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable);

    public List<WebUser> getOprUsersByChorganisation(int chorganisationId);

    public SearchResult getUsers(int organisationId, int organisationTypeId, int userRoleId, int start, int limit, String sort, String dir);

    public List<WebUser> getUsers();

    public void saveUser(WebUser user);

    public void savePasswordHistory(PasswordHistory passwordHistory);

    public void updateLastLogin(int userId);

    public List<PasswordHistory> getPasswordHistory(int userId, int count);

    public void block(int userId);

    public void unblock(int userId);
    
    public boolean failedLogin(int userId);
}
