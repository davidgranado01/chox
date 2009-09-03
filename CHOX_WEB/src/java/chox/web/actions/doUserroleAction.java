package chox.web.actions;

import chox.model.Insurer;
import chox.model.WebUser;
import chox.model.WebUserUserRole;
import chox.services.InsurerService;
import chox.services.UserService;
import chox.services.UserWorkgroupService;
import chox.services.WebUserUserRoleService;
import chox.web.viewdata.ActionResponse;
import java.util.List;

public class doUserroleAction extends AdminBaseModelAction{

    private List userroles;
    private int orgTypeId;
    private int webUserUserRoleId;  
    private int webUserRoleId;
    private int webUserId;
    private WebUserUserRoleService service;
    private UserWorkgroupService userWorkgroupService;
    private UserService userService;
    private InsurerService insurerService;
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

    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService)
    {
        this.userWorkgroupService = userWorkgroupService;
    }
    
    public void setInsurerService(InsurerService insurerService)
    {
        this.insurerService = insurerService;
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
            
            WebUser user = userService.getObject(webUserId);
            Insurer insurer = insurerService.getObject(user.getInsurer().getId());
            
            this.service.addNewUserRole(webUserId, webUserRoleId);
            
            if(service.isClaimHandlerRole(webUserRoleId) && insurer.isWorkgroupEnable()){
                String ackMsg = "Please add workgroups to this claim handler's user";
                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
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
        WebUser user = userService.getObject(webUserId);
        Insurer insurer = insurerService.getObject(user.getInsurer().getId());
        
        if(object.getWebUserRole().getId()==6){
            
            Integer recordDeleted = userWorkgroupService.DeleteObject(object.getWebUser().getId());
            
            if(insurer.isWorkgroupEnable()){
                String ackMsg = recordDeleted + " Workgroup(s) have been deleted";
                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
            }
            
        }
        
        this.service.DeleteObject(object);
        
        
        return SUCCESS;
        
    }    
}
