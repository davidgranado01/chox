/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.services.LookupService;
import chox.web.security.PermissionedUser;
import java.util.List;

public class AdminAction extends BaseAction{
    
    private String adminPanelName;
    private String gridViewType; 
    private int selectOrgTypeId=-1;  
    private int selectOrgId=-1;
    private String objectId;
    private LookupService lookupService;
    private PermissionedUser currentUser = getAuthenticatedUser();
    
    private boolean isSelectable = false;
    
    public boolean isIsSelectable() {
        if(currentUser.getIsCHOXAdmin()){
            isSelectable = true;
        }
        return isSelectable;
    }
    
    public void setLookupService(LookupService lookupService)
    {
        this.lookupService = lookupService;
    }

    public int getSelectOrgId() {
        
        if(!currentUser.getIsCHOXAdmin()){
            if(currentUser.getIsCHO()){
                selectOrgId = currentUser.getUser().getChorganisation().getId();
            }else if(currentUser.getIsINS()){
                selectOrgId = currentUser.getUser().getInsurer().getId();
            }
        }
        
        return selectOrgId;
    }

    public void setSelectOrgId(int selectOrgId) {
        this.selectOrgId = selectOrgId;
    }

    
    public int getSelectOrgTypeId(){
        
        if(!currentUser.getIsCHOXAdmin()){
            if(currentUser.getIsCHO()){
                selectOrgTypeId = 3;
            }else if(currentUser.getIsINS()){
                selectOrgTypeId = 2;
            }
        }
        
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
/*
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    */
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
