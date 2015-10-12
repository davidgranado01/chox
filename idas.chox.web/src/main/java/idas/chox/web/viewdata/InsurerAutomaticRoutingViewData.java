package idas.chox.web.viewdata;

import idas.chox.core.model.AutomaticRoutingPolicy;
import idas.chox.core.util.DateHelper;

public class InsurerAutomaticRoutingViewData {
    private int id;
    private int insurerId;
    private String insurerName;
    private int workgroupId;
    private String workgroupName;
    private String expression;
    private String createdBy;
    private String createdDate;

    public InsurerAutomaticRoutingViewData(AutomaticRoutingPolicy automaticRouting) {
        this.id = automaticRouting.getId();
        this.expression = automaticRouting.getExpression();
        this.insurerId = automaticRouting.getInsurer().getId();
        this.insurerName = automaticRouting.getInsurer().getName();
        this.workgroupId = automaticRouting.getWorkgroup().getId();
        this.workgroupName = automaticRouting.getWorkgroup().getName();
        this.createdBy = automaticRouting.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(automaticRouting.getCreatedDate());
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }


    
}
