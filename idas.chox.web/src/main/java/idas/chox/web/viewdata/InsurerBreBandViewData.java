package idas.chox.web.viewdata;

import idas.chox.core.model.BreBand;
import idas.chox.core.util.DateHelper;

public class InsurerBreBandViewData {

    private int id;
    private String name;
    private int insurerId;
    private String insurerName;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String createdDate;

    public InsurerBreBandViewData(BreBand object) {

        this.id = object.getId();
        this.name = object.getName();
        this.insurerId = object.getInsurer().getId();
        this.insurerName = object.getInsurer().getName();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.LocalDateTimeFormat.format(object.getCreatedDate());
        this.status = object.isIsActive();

        if (object.isIsActive()) {
            this.statusDesc = "Yes";
        } else {
            this.statusDesc = "No";
        }
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

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public String getInsurerName() {
        return insurerName;
    }
}
