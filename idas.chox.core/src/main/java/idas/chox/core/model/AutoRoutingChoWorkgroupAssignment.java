package idas.chox.core.model;

import java.io.Serializable;

public class AutoRoutingChoWorkgroupAssignment extends Entity implements Serializable {

    private Workgroup workgroup;
    private Chorganisation chorganisation;

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }
}
