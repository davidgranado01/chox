package chox.web.actions;

import chox.model.WebUserUserRole;
import chox.services.UserService;
import chox.services.WebUserUserRoleService;
import java.util.List;

public class doUserroleAction extends AdminBaseModelAction{

    private List userroles;
    private int orgTypeId;
    private int webUserUserRoleId;  
    private int webUserRoleId;
    private int webUserId;
    private WebUserUserRoleService service;
    private UserService userService;
    private String actionResult;

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
    
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

    public void setUserService(UserService userService){
        this.userService = userService;
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
        
        String sActionMsg = "";
        boolean bActionFlag = false;
        
        try{
                  
            this.service.addNewUserRole(webUserId, webUserRoleId);
            
            if(service.validateUserWithRole(webUserId, webUserRoleId)){
                actionResult="1:";
            }
            
            bActionFlag = true;
            sActionMsg = getSystemLogService().getObjectActionLogMsg("ADD", "webUserId:"+webUserId+"|webUserRoleId:"+webUserRoleId);
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM011", sActionMsg, bActionFlag, 3);
        
        return SUCCESS;
    }
    
    public String removeRoleMapping(){
        
        WebUserUserRole object = this.service.getObject(webUserUserRoleId);
        this.service.DeleteObject(object);
        return SUCCESS;
        
    }    
}
