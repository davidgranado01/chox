package idas.chox.core.services;

import idas.chox.core.model.WebUser;
import java.util.List;

public interface UserService {

    public WebUser findByEmail(String email);

    public boolean isEmailExist(String email);

    public boolean isEmailExist(String email, int userId);

    public WebUser loadUserByEmail(String email);

    public WebUser findByUserName(String userName);

    public boolean isUserNameExist(String userName);

    public boolean isUserNameExist(String userName, int userId);

    public WebUser loadUserByUsername(String userName);

    public void persist(WebUser user, String emailId);

    public WebUser getWebUser(int id);

    public Long getNumChoActiveUser(Integer choId);

    public Long getNumInsActiveUser(Integer insId);

    public List<WebUser> getUsers();

    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, String selectedUserRole);

    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, int selectedWorkgroupId, String selectedUserRole);

    public List<WebUser> getClaimHanldersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable);

    public List<WebUser> getClaimHanldersByInsurer(int insurerId, boolean workgroupEnable);

    public List<WebUser> getUsers(int organisationId, int organisationTypeId, int userRoleId);

    public WebUser getUsers(int userId);

    public boolean saveUser(WebUser user);
}
