package idas.chox.core.model;

import java.io.Serializable;

public class SystemLog extends Entity implements Serializable {

    protected String actionId;
    protected String message;
    protected String status;

    public SystemLog() {
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
