/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.WebUserUserRole;
import chox.services.WebUserUserRoleService;
import java.util.List;

public class doUserroleAction extends BaseAction{

    private List userroles;
    private int orgTypeId;
    private String actionResult;
    private int webUserUserRoleId;  
    private int webUserRoleId;
    private int webUserId;
    private WebUserUserRoleService service;
    
    /*
    private LookupService lookupService;
    public void setLookupService(LookupService lookupService)
    {
        this.lookupService = lookupService;
    }
    */
    
    public List getUserroles() {
        userroles = this.service.getSelectedUserAvailableRoleLookupItem(orgTypeId, webUserId);
        return userroles;
    }
    
    public int getWebUserRoleId() {
        return webUserRoleId;
    }

    public void setWebUserRoleId(int webUserRoleId) {
        this.webUserRoleId = webUserRoleId;
    }
    
    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }

    public int getWebUserUserRoleId() {
        return webUserUserRoleId;
    }

    public void setWebUserUserRoleId(int webUserUserRoleId) {
        this.webUserUserRoleId = webUserUserRoleId;
    }

    public void setWebUserUserRoleService(WebUserUserRoleService service)
    {
        this.service = service;
    }
        
    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public int getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(int orgTypeId) {
        this.orgTypeId = orgTypeId;
    }
    
    public String doRenderActionPage(){
        return SUCCESS;
    }  
    
    public String addNewRoleMapping(){
        this.service.addNewUserRole(webUserId, webUserRoleId);
        return SUCCESS;
    }
    
    public String removeRoleMapping(){
        
        WebUserUserRole object = this.service.getObject(webUserUserRoleId);
        this.service.DeleteObject(object);
        return SUCCESS;
        
    }    
}
