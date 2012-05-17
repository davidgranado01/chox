package idas.chox.web.viewdata;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.util.DateHelper;

public class ReasonOfRejectionViewData {
    
    private int id;
    private String name;
    private String createdDate;
    private String description;
    private String type;
    private boolean status;
    private boolean restricted;
    
    public ReasonOfRejectionViewData(ReasonOfRejection object) {
        this.id = object.getId();
        this.name = object.getName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.description = object.getDescription();
        this.type = object.getType();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.status = object.isStatus();
        this.restricted = object.isRestricted();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean isRestricted() {
        return restricted;
    }
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

}
