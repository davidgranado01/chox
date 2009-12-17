package idas.chox.core.model;

import java.io.Serializable;

public class WebUserUserRole extends Entity implements Serializable{
    
    protected boolean active;
    protected WebUser webUser;
    protected WebUserRole webUserRole;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public WebUserRole getWebUserRole() {
        return webUserRole;
    }

    public void setWebUserRole(WebUserRole webUserRole) {
        this.webUserRole = webUserRole;
    }
    
}
