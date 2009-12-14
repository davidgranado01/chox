package idas.chox.core.model;

import java.io.Serializable;

public class ReasonOfDelay extends AuditableEntity implements Serializable {

    protected String name;
    protected String description;
    protected boolean status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
