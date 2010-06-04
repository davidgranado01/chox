package idas.chox.core.model;

import java.io.Serializable;

public class ReasonOfRejection extends Entity implements Serializable {

    protected String name;
    protected String type;
    protected boolean status;
    protected boolean restricted;

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

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }
}
