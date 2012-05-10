package idas.chox.web.viewdata;

import idas.chox.core.model.IPWhitelist;
import idas.chox.core.util.DateHelper;

public class IPWhitelistViewData {

    private int id;
    private String insName;
    private String choName;
    private String ipAddress;
    private String description;
    private String createdBy;
    private String createdDate;

    public IPWhitelistViewData(IPWhitelist object) {

        this.id = object.getId();
        if (object.getInsurer() != null)
            this.insName = object.getInsurer().getName();
        if (object.getChorganisation() != null)
            this.choName = object.getChorganisation().getName();
        this.ipAddress = object.getIpAddress();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.description = object.getDescription();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getId() {
        return id;
    }

    public String getChoName() {
        return choName;
    }

    public void setChoName(String choName) {
        this.choName = choName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
