package idas.chox.core.services;

import idas.chox.core.model.WebUserWorkgroup;
import java.util.List;

public interface UserWorkgroupService {

    public List<WebUserWorkgroup> getUserWorkgroupsByUser(int userId);

    public WebUserWorkgroup getUserWorkgroup(int userWorkgroupId);

    public void deleteWebUserWorkgroup(WebUserWorkgroup webUserWorkgroup);

    public void saveWebUserWorkgroup(WebUserWorkgroup webUserWorkgroup);

    public boolean isUserWorkgroupExist(Integer workgroupId, Integer webUserId);

}