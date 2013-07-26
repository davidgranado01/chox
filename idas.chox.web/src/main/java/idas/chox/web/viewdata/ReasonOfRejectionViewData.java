package idas.chox.web.viewdata;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.util.DateHelper;

public class ReasonOfRejectionViewData {
    
    private int id;
    private String rorName;
    private String createdDate;
    private String description;
    private String type;
    private boolean gtaActive;
    private boolean collaborationActive;
    private boolean insurerVsInsurerActive;
    private boolean subscriberActive;
    private boolean fixedFeeActive;
    private boolean insurerUploadActive;
    private boolean tpiActive;
    private boolean restricted;
    
    public ReasonOfRejectionViewData(ReasonOfRejection object) {
        this.id = object.getId();
        this.rorName = object.getRorName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.description = object.getDescription();
        this.type = object.getType();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.gtaActive = object.isGtaActive();
        this.collaborationActive = object.isCollaborationActive();
        this.insurerVsInsurerActive = object.isInsurerVsInsurerActive();
        this.subscriberActive = object.isSubscriberActive();
        this.fixedFeeActive = object.isFixedFeeActive();
        this.insurerUploadActive = object.isInsurerUploadActive();
        this.tpiActive = object.isTpiActive();
        this.restricted = object.isRestricted();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRorName() {
        return rorName;
    }
    public void setRorName(String name) {
        this.rorName = name;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
