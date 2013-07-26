package idas.chox.core.model;

import java.io.Serializable;

public class ReasonOfRejectionTemplate extends Entity implements Serializable {

    private String name;
    private String type;
    private String description;
    private boolean restricted;
    private boolean gtaActive;
    private boolean collaborationActive;
    private boolean insurerVsInsurerActive;
    private boolean subscriberActive;
    private boolean fixedFeeActive;
    private boolean insurerUploadActive;
    private boolean tpiActive;

    public ReasonOfRejectionTemplate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    public boolean isGtaActive() {
        return gtaActive;
    }

    public void setGtaActive(boolean gtaActive) {
        this.gtaActive = gtaActive;
    }

    public boolean isCollaborationActive() {
        return collaborationActive;
    }

    public void setCollaborationActive(boolean collaborationActive) {
        this.collaborationActive = collaborationActive;
    }

    public boolean isInsurerVsInsurerActive() {
        return insurerVsInsurerActive;
    }

    public void setInsurerVsInsurerActive(boolean insurerVsInsurerActive) {
        this.insurerVsInsurerActive = insurerVsInsurerActive;
    }

    public boolean isSubscriberActive() {
        return subscriberActive;
    }

    public void setSubscriberActive(boolean subscriberActive) {
        this.subscriberActive = subscriberActive;
    }

    public boolean isFixedFeeActive() {
        return fixedFeeActive;
    }

    public void setFixedFeeActive(boolean fixedFeeActive) {
        this.fixedFeeActive = fixedFeeActive;
    }

    public boolean isInsurerUploadActive() {
        return insurerUploadActive;
    }

    public void setInsurerUploadActive(boolean insurerUploadActive) {
        this.insurerUploadActive = insurerUploadActive;
    }

    public boolean isTpiActive() {
        return tpiActive;
    }

    public void setTpiActive(boolean tpiActive) {
        this.tpiActive = tpiActive;
    }
}
