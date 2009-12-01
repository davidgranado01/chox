package chox.services;

import chox.model.WebUser;
import java.util.List;

public interface UserService {

    public WebUser findByEmail(String email);

    public void persist(WebUser user, String emailId);
    public WebUser getLatestObject(int id);
    public WebUser getObject(int id);
    public Long getNumChoActiveUser(Integer choId);
    public Long getNumInsActiveUser(Integer insId);
    public List<WebUser> getUsers();
    public WebUser getUsers(int id);
    public List<WebUser> getUsers(int orgId, String orgType);
    public boolean updateObject(WebUser object);
    public boolean isEmailExist(String email);
    public boolean isEmailExist(String email, int userId);
    public List<WebUser> getClaimHanldersByInsurerWorkgroup(int insurerId, int selectedWorkgroupId, boolean workgroupEnable);
    public List<WebUser> getClaimHanldersByInsurer(int insurerId, boolean workgroupEnable);
    public boolean isOtherWorkgroupEnableCOMUserWithWorkgroupExist(int insurerId, int selectedWorkgroupId, int userId);
    public boolean isOtherWorkgroupEnableCHUserWithWorkgroupExist(int insurerId, int selectedWorkgroupId, int userId);

    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, String selectedUserRole);
    public boolean isWorkgroupOwnByOtherUserByRole(WebUser user, int selectedWorkgroupId, String selectedUserRole);
    
}
