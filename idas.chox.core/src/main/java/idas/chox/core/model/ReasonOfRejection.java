package idas.chox.core.model;

import java.io.Serializable;

public class ReasonOfRejection extends Entity implements Serializable {

    private String rorName;
    private String type;
    private String description;
    private Insurer insurer;
    private boolean restricted;
    private boolean gtaActive;
    private boolean collaborationActive;
    private boolean insurerVsInsurerActive;
    private boolean subscriberActive;
    private boolean fixedFeeActive;
    private boolean insurerUploadActive;
    private boolean tpiActive;
    
    public static final String TYPE_CLAIM = "Claim";
    public static final String TYPE_INVOICE = "Invoice";

    public ReasonOfRejection() {
    }

    public String getRorName() {
        return rorName;
    }

    public void setRorName(String name) {
        this.rorName = name;
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
