package chox.web.actions;

import chox.Util.RoleHelper;
import chox.data.OrganisationType;
import chox.model.Insurer;
import chox.model.WebUser;
import chox.model.WebUserRole;
import chox.model.WebUserUserRole;
import chox.services.ClaimService;
import chox.services.InsurerService;
import chox.services.UserService;
import chox.services.UserWorkgroupService;
import chox.services.WebUserUserRoleService;
import chox.web.viewdata.ActionResponse;
import java.util.List;

public class doUserroleAction extends BaseAction{

    private List userroles;
    private int orgTypeId;
    private int webUserUserRoleId;  
    private int webUserRoleId;
    private String webUserRoleCode;
    private int webUserId;
    private WebUserUserRoleService service;
    private UserWorkgroupService userWorkgroupService;
    private UserService userService;
    private InsurerService insurerService;
    private String actionResult;
    private String workgroupValidationMsg;
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public String getWebUserRoleCode() {
        return webUserRoleCode;
    }

    public void setWebUserRoleCode(String webUserRoleCode) {
        this.webUserRoleCode = webUserRoleCode;
    }

    public String getJsonData() {
        return workgroupValidationMsg;
    }
   
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

        try{
            
            WebUser user = userService.getObject(webUserId);
            this.service.addNewUserRole(webUserId, webUserRoleId);

            if(user.getOrganisationType().equalsIgnoreCase(OrganisationType.INS)){

                Insurer insurer = insurerService.getObject(user.getInsurer().getId());

                if(service.isClaimHandlerRole(webUserRoleId)
                        && insurer.isWorkgroupEnable()
                        && (userWorkgroupService.getObjects(webUserId).size())<=0){
                    
                    String ackMsg = "Please assign one or more Workgroup(s) to this user";
                    getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
                    
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return SUCCESS;
    }
    
    public String checkRoleAllowToDelete(){

        boolean isAllowToDelete = true;
        String errMsg = "Are you sure you want to remove this role?";

        WebUser user = userService.getObject(this.webUserId);
        
        if(user.getInsurer()!=null){

            if(user.getInsurer().isWorkgroupEnable() && this.webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_COM)){
                if(!RoleHelper.isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH)){
                    // ONLY CAN DELETE WHEN NO WORKGROUPS
                    if(user.getWorkgroupIds().size()>0){
                        isAllowToDelete = false;
                        errMsg = "It is not possible to remove the assignment of a Claim Ownership Manager Role against a user who has workgroup(s). Please remove the workgroup(s) from this user.";
                    }
                }else{

                    boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupByUserExist(user.getWorkgroupIds(), user.getId());
                    boolean hasOtherComUsers = userService.isWorkgroupOwnByOtherUserByRole(user, WebUserRole.ROLE_COM);
System.out.println(">>>>>>>>>>> 1 :"+hasOpenClaims);
                    System.out.println(">>>>>>>>>>> 2 :"+hasOtherComUsers);
                    if(hasOpenClaims && !hasOtherComUsers){
System.out.println(">>>>>>>>>>> 3 :");
                        errMsg = "User "+user.getDisplayName()+" is the last user that has Claim Ownership Manager and is assigned to Workgroup(s). Are you sure you want to remove this workgroup?";
                    }
                }
            }

            if(user.getInsurer().isWorkgroupEnable()){

                if(this.webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_CH)){
                    if(!RoleHelper.isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_COM)){
                        // ONLY CAN DELETE WHEN NO WORKGROUPS
                        if(user.getWorkgroupIds().size()>0){
                            isAllowToDelete = false;
                            errMsg = "It is not possible to remove the assignment of a Claim Handler Role against a user who has workgroup(s).";
                        }

                    }else{
                        // CHECK CH ALLOW TO DELETE
                        if(user.getInsurer().isClaimOwnershipEnable() && claimService.isUserHasOpenClaim(this.webUserId)){
                            isAllowToDelete = false;
                            errMsg = "User "+user.getDisplayName()+" has open claim(s) assigned to them, it is not possible to remove the assignment of a Claim Handler Role against a user who has open claim(s)";
                        }
                    }
                }
                
            }else{

                // CHECK CH ALLOW TO DELETE
                if(user.getInsurer().isClaimOwnershipEnable() && claimService.isUserHasOpenClaim(this.webUserId)){
                    isAllowToDelete = false;
                    errMsg = "User "+user.getDisplayName()+" has open claim(s) assigned to them, it is not possible to remove the assignment of a Claim Handler Role against a user who has open claim(s)";
                }
            }
        }
        System.out.println(">>>>>>>>>>> 3 :"+errMsg);
        workgroupValidationMsg = "{isAllowToDelete:"+isAllowToDelete+",warningMsg:'"+errMsg+"'}";
        return SUCCESS;
    }

    public String removeRoleMapping(){
        WebUserUserRole object = this.service.getObject(this.webUserUserRoleId);
        this.service.DeleteObject(object);
        
        /*
        WebUser user = userService.getLatestObject(webUserId);
        if(user.getInsurer()!=null){
            
            Insurer insurer = insurerService.getObject(user.getInsurer().getId());

            if(!user.isClaimHandler() && insurer.isWorkgroupEnable()
                    && (
                        object.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CH)
                        || object.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_COM)
                        )){

                // Integer recordDeleted = userWorkgroupService.DeleteObject(object.getWebUser().getId());
                // String ackMsg = recordDeleted + " Workgroup(s) have been deleted";
                // getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
            }
        }
        */
        
        return SUCCESS;
        
    }    
}
