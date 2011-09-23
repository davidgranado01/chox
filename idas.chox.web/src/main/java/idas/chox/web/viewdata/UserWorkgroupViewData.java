package idas.chox.web.viewdata;

import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.util.DateHelper;

public class UserWorkgroupViewData {

    private int id;
    private int workgroupId;
    private String name;
    private String createdBy;
    private String createdDate;

    public UserWorkgroupViewData(WebUserWorkgroup webUserWorkgroup) {
        this.id = webUserWorkgroup.getId();
        this.workgroupId = webUserWorkgroup.getWorkgroup().getId();
        this.name = webUserWorkgroup.getWorkgroup().getName();
        this.createdBy = webUserWorkgroup.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(webUserWorkgroup.getCreatedDate());
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
