package idas.chox.web.viewdata;

import idas.chox.core.model.Workgroup;
import idas.chox.core.util.DateHelper;

public class WorkgroupViewData {

    private int id;
    private int insurerId;
    private String insurerName;
    private String name;
    private String site;
    private String team;
    private String createdBy;
    private String createdDate;
    private boolean status;
    private boolean stpExcluded;

    public WorkgroupViewData(Workgroup object) {

        this.id = object.getId();
        this.insurerId = object.getInsurer().getId();
        this.insurerName = object.getInsurer().getName();
        this.name = object.getName();
        this.site = object.getSite();
        this.team = object.getTeam();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.status = object.isStatus();
        this.stpExcluded = object.isStpExcluded();

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

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return status ? "Yes" : "No";
    }

    public boolean isStpExcluded() {
        return stpExcluded;
    }

    public void setStpExcluded(boolean stpExcluded) {
        this.stpExcluded = stpExcluded;
    }

    public String getStpExcludedDesc() {
        return stpExcluded ? "Yes" : "No";
    }

}
