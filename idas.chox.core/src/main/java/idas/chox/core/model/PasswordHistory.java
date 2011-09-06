package idas.chox.core.model;

import java.io.Serializable;

/**
 *
 * @author John
 */
public class PasswordHistory  extends Entity implements Serializable {
    private WebUser webUser;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

}
