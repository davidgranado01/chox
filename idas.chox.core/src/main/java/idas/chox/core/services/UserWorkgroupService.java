package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.WebUserWorkgroup;

public interface UserWorkgroupService {

    List<WebUserWorkgroup> getUserWorkgroupsByUser(int userId);

    List<WebUserWorkgroup> getUserWorkgroupsByWorkgroup(int wgId);

    WebUserWorkgroup getUserWorkgroup(int userWorkgroupId);

    boolean isUserWorkgroupExist(Integer workgroupId, Integer webUserId);

    void saveUserWorkgroup(WebUserWorkgroup userWorkgroup);
}
