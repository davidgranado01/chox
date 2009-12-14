package idas.chox.core.model;

import java.io.Serializable;

public class WebUserWorkgroup extends AuditableEntity implements Serializable {

    protected Workgroup workgroup;
    protected WebUser user;

    public WebUser getUser() {
        return user;
    }

    public void setUser(WebUser user) {
        this.user = user;
    }

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
    }
}
