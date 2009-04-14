package chox.services;

import chox.model.WebUser;
import java.util.List;

public interface UserService {

    public WebUser findByEmail(String email);

    public void persist(WebUser user, String emailId);

    public WebUser getObject(int id);

    public Long getNumChoActiveUser(Integer choId);

    public Long getNumInsActiveUser(Integer insId);
    public List<WebUser> getUsers(int start, int limit);
    public void updateObject(WebUser object);
}
