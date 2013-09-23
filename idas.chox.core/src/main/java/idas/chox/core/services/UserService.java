package idas.chox.core.services;

import java.util.List;
import java.util.Set;

import idas.chox.core.model.PasswordHistory;
import idas.chox.core.model.WebUser;
import idas.chox.core.search.SearchResult;

public interface UserService {

    WebUser findByEmail(String email);

    WebUser findByUserName(String userName);

    boolean isUserNameExist(String userName);

    boolean isUserNameExist(String userName, int userId);

    WebUser loadUserByUsername(String userName);

    void persist(WebUser user);

    WebUser getWebUser(int id);

    Long getNumChoActiveUser(Integer choId);

    Long getNumInsActiveUser(Integer insId);

    boolean isWorkgroupOwnByOtherUserByRole(WebUser user, String selectedUserRole);

    boolean isWorkgroupOwnByOtherUserByRole(WebUser user, int selectedWorkgroupId, String selectedUserRole);

    List<WebUser> getActiveClaimHandlersByInsurerWorkgroup(int insurerId, Set<Integer> selectedWorkgroupId, boolean workgroupEnable);

    List<WebUser> getAllClaimHandlersByInsurerWorkgroup(int insurerId, Set<Integer> selectedWorkgroupId, boolean workgroupEnable);

    List<WebUser> getOprUsersByChorganisation(int chorganisationId);

    SearchResult getUsers(int organisationId, int organisationTypeId, int userRoleId, int start, int limit, String sort, String dir, boolean activeUsersOnly);

    List<WebUser> getUsers();

    void saveUser(WebUser user);

    void savePasswordHistory(PasswordHistory passwordHistory);

    void updateLastLogin(int userId);

    List<PasswordHistory> getPasswordHistory(int userId, int count);

    void block(int userId);

    void unblock(int userId);
    
    boolean failedLogin(int userId);
}
