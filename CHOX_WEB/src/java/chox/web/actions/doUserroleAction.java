package chox.web.actions;

import chox.Util.RoleHelper;
import chox.data.OrganisationType;
import chox.model.ClaimStatus;
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

            if(user.getInsurer().isWorkgroupEnable()){
                // WORKGROUP ENABLE VALIDATION

                if(RoleHelper.isUserCheckByWorkgroup(user)){
                    // WORKGROUP ENABLE FOR CERTAIN USERS (CH, FNOL, COM)

                    String selectedRoleDescription = "";

                    if(this.webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_COM)){

                        selectedRoleDescription = "Claim Ownership Manager";

                        // DELETE COM
                        boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupsByUserExist(user.getInsurer().getId(), user.getWorkgroupIds(), -1);
                        boolean hasOtherComUsers = userService.isWorkgroupOwnByOtherUserByRole(user, WebUserRole.ROLE_COM);

                        if(hasOpenClaims && !hasOtherComUsers){
                            
                            errMsg = "User "+user.getDisplayName()+" is the last user that has "+selectedRoleDescription+" and is assigned to Workgroup(s). Are you sure you want to remove this role?";
                        }

                    }else if(this.webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_FNOL)){

                        selectedRoleDescription = "FNOL";

                        // DELETE FNOL
                        boolean hasOpenFnolClaims = claimService.isOpenClaimByWorkgroupsByStatusExist(user.getInsurer().getId(), user.getWorkgroupIds(), ClaimStatus.CLAIM_REFERRED_TO_FNOL);
                        boolean hasOtherFnolUsers = userService.isWorkgroupOwnByOtherUserByRole(user, WebUserRole.ROLE_FNOL);

                        if(hasOpenFnolClaims && !hasOtherFnolUsers){
                            
                            errMsg = "User "+user.getDisplayName()+" is the last user that has "+selectedRoleDescription+" and is assigned to Workgroup(s). Are you sure you want to remove this role?";
                        }

                    }else if(this.webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_CH)){

                        selectedRoleDescription = "Claim Handler";
                        
                        boolean hasOpenClaims = true;
                        boolean hasOtherCHUsers = true;

                        if(user.getInsurer().isClaimOwnershipEnable()){

                            // CLAIM OWNERSHIP
                            hasOpenClaims = claimService.isUserHasOpenClaim(user.getId());
                            
                            if(hasOpenClaims){
                                
                                isAllowToDelete = false;
                                errMsg = "User "+user.getDisplayName()+" has open claim(s) assigned to them, it is not possible to remove the assignment of a "+selectedRoleDescription+" Role against a user who has open claim(s)";
                            }

                        }else{

                            // WOPRKGROUP ONLY
                            hasOpenClaims = claimService.isOpenClaimByWorkgroupsByUserExist(user.getInsurer().getId(), user.getWorkgroupIds(), -1);
                            hasOtherCHUsers = userService.isWorkgroupOwnByOtherUserByRole(user, WebUserRole.ROLE_CH);

                            if(hasOpenClaims && !hasOtherCHUsers){
                                
                                errMsg = "User "+user.getDisplayName()+" is the last user that has "+selectedRoleDescription+" and is assigned to Workgroup(s). Are you sure you want to remove this role?";
                            }
                        }
                    }

                    if(isAllowToDelete){

                        if(user.getWorkgroupIds().size()>0 && RoleHelper.getWorkgroupRoleCount(user.getRoles())<=1){
                            isAllowToDelete = false;
                            errMsg = "It is not possible to remove the assignment of "+selectedRoleDescription+" against a user who has Workgroup(s). Please remove the Workgroup(s) from this user.";
                        }
                        
                    }

                }
                
            }else{

                if(user.getInsurer().isClaimOwnershipEnable()){
                    
                    // CLAIM OWNERSHIP
                    boolean hasOpenClaims = claimService.isUserHasOpenClaim(user.getId());

                    if(hasOpenClaims){
                        
                        isAllowToDelete = false;
                        errMsg = "User "+user.getDisplayName()+" has open claim(s) assigned to them, it is not possible to remove the assignment of a Claim Handler Role against a user who has open claim(s)";
                        
                    }
                }
            }
        }

        workgroupValidationMsg = "{isAllowToDelete:"+isAllowToDelete+",warningMsg:'"+errMsg+"'}";
        return SUCCESS;
        
    }

    public String removeRoleMapping(){
        WebUserUserRole object = this.service.getObject(this.webUserUserRoleId);
        this.service.DeleteObject(object);        
        return SUCCESS;
    }
    
}
