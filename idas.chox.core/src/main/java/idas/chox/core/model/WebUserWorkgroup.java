package idas.chox.core.model;

import java.io.Serializable;

public class WebUserWorkgroup extends Entity implements Serializable {

    private Workgroup workgroup;
    private WebUser user;

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
