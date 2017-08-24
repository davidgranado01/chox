package idas.chox.web.viewdata;

import idas.chox.core.model.IPWhitelist;
import idas.chox.core.util.DateHelper;

public class IPWhitelistViewData {

    final private int id;
    final private String insName;
    final private String choName;
    final private String ipAddress;
    final private String description;
    final private String createdBy;
    final private String createdDate;

    public IPWhitelistViewData(IPWhitelist object) {

        this.id = object.getId();
        if (object.getInsurer() != null) {
            this.insName = object.getInsurer().getName();
        } else {
            this.insName = "";
        }
        if (object.getChorganisation() != null) {
            this.choName = object.getChorganisation().getName();
        } else {
            this.choName = "";
        }
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

    public String getDescription() {
        return description;
    }

    public String getInsName() {
        return insName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

}
