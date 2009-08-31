package chox.services;

import chox.model.UserWorkgroup;
import java.util.List;

public interface UserWorkgroupService {
    public List<UserWorkgroup> getObjects(int userId);
    public UserWorkgroup getObject(int id);
    public boolean DeleteObject(UserWorkgroup object);
    public boolean isObjectExist(int WorkgroupId);
}
