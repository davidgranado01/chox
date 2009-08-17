package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.ChoBandOrganisation;

public class ChoBandChorganisationViewData {

    private int id;
    private int insurerId;
    private String insurerName;
    private int bandId;
    private int chorganisationId;
    private String chorganisationName;
    private boolean chorganisationStatus;
    private String chorganisationStatusDesc;
    private String createdBy;
    private String createdDate;
    
    
    
    public ChoBandChorganisationViewData(ChoBandOrganisation object) {
         
        this.id = object.getId();
        
        if(object.getChorganisation()!=null){
            this.chorganisationId = object.getChorganisation().getId();
            this.chorganisationName = object.getChorganisation().getName();
        }
        
        /*
        if(object.getInsurer()!=null){
            this.insurerId = object.getInsurer().getId();
            this.insurerName = object.getInsurer().getName();
        }
        */
        
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());                    
        
        this.chorganisationStatus = object.getChorganisation().isStatus();
        
        if(object.getChorganisation().isStatus()){
            this.chorganisationStatusDesc = "Yes";
        }else{
            this.chorganisationStatusDesc = "No";
        }
        
    }
    
    public int getBandId() {
        return bandId;
    }

    public int getChorganisationId() {
        return chorganisationId;
    }

    public String getChorganisationName() {
        return chorganisationName;
    }

    public boolean isChorganisationStatus() {
        return chorganisationStatus;
    }

    public String getChorganisationStatusDesc() {
        return chorganisationStatusDesc;
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
    
    
}
