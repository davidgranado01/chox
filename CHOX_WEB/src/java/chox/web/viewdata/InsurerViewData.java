package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.Insurer;

public class InsurerViewData {

    private int id;
    private String name;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String createdDate;
    
    public InsurerViewData(Insurer object) {
        
        this.id = object.getId();
        this.name = object.getName();
        this.createdBy =object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());   
        this.status = object.isStatus();
        
        if(object.isStatus()){
            this.statusDesc = "Yes";
        }else{
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


    
}
