package idas.chox.core.model;

import java.io.Serializable;

public class ReasonOfRejection extends Entity implements Serializable {

    private String name;
    private String type;
    private String description;
    private Insurer insurer;
    private boolean status;
    private boolean restricted;
    
    public static final String TYPE_CLAIM = "Claim";
    public static final String TYPE_INVOICE = "Invoice";

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Insurer getInsurer() {
		return insurer;
	}

	public void setInsurer(Insurer insurer) {
		this.insurer = insurer;
	}
}
