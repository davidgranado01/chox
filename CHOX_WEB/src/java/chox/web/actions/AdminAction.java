/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.services.LookupService;
import java.util.List;

public class AdminAction extends BaseAction{
    
    private String adminPanelName;
    private String gridViewType;
    private String mode;    
    private int selectOrgTypeId=-1;  
    private String objectId;
    private List insurers;
    private List suppliers;
    private LookupService lookupService;
    private boolean isSelectable = true;

    public boolean isIsSelectable() {
        return isSelectable;
    }

    public void setIsSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }
    
    public void setLookupService(LookupService lookupService)
    {
        this.lookupService = lookupService;
    }
    
    public List getInsurers() {
        insurers = this.lookupService.getAllActiveInsurers();
        return insurers;
    }
    
    public List getSuppliers() {
        suppliers = this.lookupService.getAllActiveSuppliers();
        return suppliers;
    }

    public int getSelectOrgTypeId() {
        return selectOrgTypeId;
    }

    public void setSelectOrgTypeId(int selectOrgTypeId) {
        this.selectOrgTypeId = selectOrgTypeId;
    }
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public String adminPanel() {
        return SUCCESS;
    }
    
    public String loadAdminPanel() {
        return this.adminPanelName;
    }

    public String getAdminPanelName() {
        return adminPanelName;
    }

    public void setAdminPanelName(String adminPanelName) {
        this.adminPanelName = adminPanelName;
    }

    public void getGridviewList(){
    }
    
    public String getGridViewType() {
        return gridViewType;
    }

    public void setGridViewType(String gridViewType) {
        this.gridViewType = gridViewType;
    }   
    
}
