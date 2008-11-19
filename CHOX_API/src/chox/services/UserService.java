package chox.services;

import chox.model.WebUser;

public interface UserService {

    public WebUser findByEmail( String email );

    public void persist( WebUser user, String emailId );

}
