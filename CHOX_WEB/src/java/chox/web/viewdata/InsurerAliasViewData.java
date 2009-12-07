package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.InsurerAlias;

/**
 *
 * @author Carlson
 */
public class InsurerAliasViewData {

    private int id;
    private String name;
    private int insurerId;
    private String insurerName;
    private String createdBy;
    private String createdDate;

    public InsurerAliasViewData(InsurerAlias object) {
        
        this.id = object.getId();
        this.insurerId = object.getInsurer().getId();
        this.insurerName = object.getInsurer().getName();
        this.name = object.getAliasName();
        this.createdBy =object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());   
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
    
    
    
    
}
