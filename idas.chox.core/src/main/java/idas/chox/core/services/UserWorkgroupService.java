package idas.chox.core.services;

import idas.chox.core.model.WebUserWorkgroup;
import java.util.List;

public interface UserWorkgroupService {

    public List<WebUserWorkgroup> getObjects(int userId);

    public WebUserWorkgroup getObject(int id);

    public boolean DeleteObject(WebUserWorkgroup object);

    public Integer DeleteObject(int webUserId);

    public boolean AddObject(WebUserWorkgroup object);

    public boolean isObjectExist(int WorkgroupId);

    public boolean isObjectExist(int webUserId, int workgroupId);
}
