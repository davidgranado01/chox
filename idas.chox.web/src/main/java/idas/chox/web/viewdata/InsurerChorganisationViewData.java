package idas.chox.web.viewdata;

import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.util.DateHelper;

public class InsurerChorganisationViewData {

    private int id;
    private int insurerId;
    private int chorganisationId;
    private String insurerName;
    private String chorganisationName;
    private boolean chorganisationStatus;
    private String chorganisationStatusDesc;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String createdDate;
    private String tpiRegexString;
    private String tpiIdentifier;
    private boolean tpiClaimOnly;
    private String tpiWorkgroupName;
    private String tpiClaimOwnerName;



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
        this.createdDate = DateHelper.LocalDateTimeFormat.format(insurerChorganisation.getCreatedDate());
        this.status = insurerChorganisation.isStatus();
        this.chorganisationStatus = insurerChorganisation.getChorganisation().isStatus();

        if (insurerChorganisation.isStatus()) {
            this.statusDesc = "Yes";
        } else {
            this.statusDesc = "No";
        }

        if (insurerChorganisation.getChorganisation().isStatus()) {
            this.chorganisationStatusDesc = "Yes";
        } else {
            this.chorganisationStatusDesc = "No";
        }

        if (insurerChorganisation.isTpiClaimOnly()){
            this.tpiClaimOnly=true;
        }else{
            this.tpiClaimOnly=false;
        }

        if(insurerChorganisation.getTpiRegexExpression()!=null){
            this.tpiRegexString=insurerChorganisation.getTpiRegexExpression();
        }else{
            this.tpiRegexString="";
        }
        if (insurerChorganisation.getTpiClaimOwner()!=null){
            this.tpiClaimOwnerName=insurerChorganisation.getTpiClaimOwner().getDisplayName();
        }else{
            this.tpiClaimOwnerName="";
        }
        if(insurerChorganisation.getTpiWorkgroup()!=null){
            this.tpiWorkgroupName=insurerChorganisation.getTpiWorkgroup().getName();
        }else{
            this.tpiWorkgroupName="";
        }
        if(insurerChorganisation.getTpiIdentificationString()!=null){
            this.tpiIdentifier=insurerChorganisation.getTpiIdentificationString();
        }else{
            this.tpiIdentifier="";
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

    public boolean isChorganisationStatus() {
        return chorganisationStatus;
    }

    public String getChorganisationStatusDesc() {
        return chorganisationStatusDesc;
    }

    public boolean isTpiClaimOnly() {
        return tpiClaimOnly;
    }

    public void setTpiClaimOnly(boolean tpiClaimOnly) {
        this.tpiClaimOnly = tpiClaimOnly;
    }

    public String getTpiClaimOwnerName() {
        return tpiClaimOwnerName;
    }

    public void setTpiClaimOwnerName(String tpiClaimOwnerName) {
        this.tpiClaimOwnerName = tpiClaimOwnerName;
    }

    public String getTpiIdentifier() {
        return tpiIdentifier;
    }

    public void setTpiIdentifier(String tpiIdentifier) {
        this.tpiIdentifier = tpiIdentifier;
    }

    public String getTpiRegexString() {
        return tpiRegexString;
    }

    public void setTpiRegexString(String tpiRegexString) {
        this.tpiRegexString = tpiRegexString;
    }

    public String getTpiWorkgroupName() {
        return tpiWorkgroupName;
    }

    public void setTpiWorkgroupName(String tpiWorkgroupName) {
        this.tpiWorkgroupName = tpiWorkgroupName;
    }
}
