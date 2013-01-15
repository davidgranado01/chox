package idas.chox.web.viewdata;

import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.util.DateHelper;

public class InsurerChorganisationViewData {

    private int id;
    private int insurerId;
    private int chorganisationId;
    private String insurerName;
    private String chorganisationName;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String createdDate;

    public InsurerChorganisationViewData(InsurerChorganisation insurerChorganisation) {

        this.id = insurerChorganisation.getId();

        if (insurerChorganisation.getChorganisation() != null) {
            this.chorganisationId = insurerChorganisation.getChorganisation().getId();
            this.chorganisationName = insurerChorganisation.getChorganisation().getName();
        }

        if (insurerChorganisation.getInsurer() != null) {
            this.insurerId = insurerChorganisation.getInsurer().getId();
            this.insurerName = insurerChorganisation.getInsurer().getName();
        }

        this.createdBy = insurerChorganisation.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(insurerChorganisation.getCreatedDate());
        this.status = insurerChorganisation.getChorganisation().isStatus();

        if (insurerChorganisation.getChorganisation().isStatus()) {
            this.statusDesc = "Yes";
        } else {
            this.statusDesc = "No";
        }        
    }

    public int getChorganisationId() {
        return chorganisationId;
    }

    public String getChorganisationName() {
        return chorganisationName;
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

    public int getInsurerId() {
        return insurerId;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
   
}
