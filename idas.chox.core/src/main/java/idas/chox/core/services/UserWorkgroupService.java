package idas.chox.core.services;

import idas.chox.core.model.WebUserWorkgroup;
import java.util.List;

public interface UserWorkgroupService {

    public List<WebUserWorkgroup> getUserWorkgroupsByUser(int userId);

    public WebUserWorkgroup getUserWorkgroup(int userWorkgroupId);

    public boolean isUserWorkgroupExist(Integer workgroupId, Integer webUserId);

    public void saveUserWorkgroup(WebUserWorkgroup userWorkgroup);
}
