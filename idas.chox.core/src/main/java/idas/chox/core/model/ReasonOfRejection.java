package idas.chox.core.model;

import java.io.Serializable;

public class ReasonOfRejection extends AuditableEntity implements Serializable {

    protected String name;
    protected String type;
    protected boolean status;

    public ReasonOfRejection() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
