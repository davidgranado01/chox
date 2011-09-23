package idas.chox.web.viewdata;

import idas.chox.core.model.Insurer;
import idas.chox.core.util.DateHelper;

public class InsurerViewData {

    private int id;
    private String name;
    private String vatNo;
    private String companyNo;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String createdDate;

    public InsurerViewData(Insurer object) {

        this.id = object.getId();
        this.name = object.getName();
        this.vatNo = object.getVatNo();
        this.companyNo = object.getCompanyNo();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.status = object.isStatus();

        if (object.isStatus()) {
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

    public String getCompanyNo() {
        return companyNo;
    }

    public String getVatNo() {
        return vatNo;
    }
}
