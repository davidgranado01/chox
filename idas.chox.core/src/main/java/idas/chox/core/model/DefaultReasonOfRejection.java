package idas.chox.core.model;

import java.io.Serializable;

public class DefaultReasonOfRejection extends Entity implements Serializable {

    private String name;
    private String type;
    private String description;
    private boolean restricted;
    private boolean status;

    public DefaultReasonOfRejection() {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRestricted() {
		return restricted;
	}

	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}
}
