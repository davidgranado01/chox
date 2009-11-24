package chox.web.actions;

import chox.Util.RoleHelper;
import chox.model.IdLookupItem;
import chox.model.UserWorkgroup;
import chox.model.WebUser;
import chox.model.Workgroup;
import chox.services.ClaimService;
import chox.services.UserService;
import chox.services.UserWorkgroupService;
import chox.services.WorkgroupService;
import chox.web.viewdata.ActionResponse;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class doUserWorkgroupAction extends BaseAction{
    
    private int userWorkgroupId;
    private int webUserId;
    private int workgroupId;
    private int insurerId;
    private UserWorkgroupService service;
    private WorkgroupService workgroupService;
    private UserService userService;
    private ClaimService claimService;

    private String workgroupValidationMsg;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    public void setUserWorkgroupService(UserWorkgroupService service) {
        this.service = service;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }
    
    public List getWorkgroups() {
        
        List items = new ArrayList<IdLookupItem>();
        
        List<UserWorkgroup> selectedWorkgroups = service.getObjects(webUserId);
        List<Workgroup> availableWorkgroups = workgroupService.getObjects(insurerId);
        List<Integer> selectedList = new ArrayList<Integer>();
        
        for (UserWorkgroup o : selectedWorkgroups) {
            selectedList.add(o.getWorkgroup().getId());
        }
        
        for (Workgroup s : availableWorkgroups) {
            if(!selectedList.contains(s.getId())){
                items.add(new IdLookupItem(s.getId(), s.getName()));
            }
        }
        
        return items;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getUserWorkgroupId() {
        return userWorkgroupId;
    }

    public void setUserWorkgroupId(int userWorkgroupId) {
        this.userWorkgroupId = userWorkgroupId;
    }

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }
    
    public String doRenderActionPage(){
        return SUCCESS;
    }  

   public String getJsonData() {
        return workgroupValidationMsg;
    }

    public String checkWorkgroupAllowToDelete(){

        UserWorkgroup model = service.getObject(userWorkgroupId);

        int selectedInsurerId = model.getUser().getInsurer().getId();
        int selectedWorkgroupId = model.getWorkgroup().getId();
        String selectedWorkgroupName = model.getWorkgroup().getName();
        String selectedUserName = model.getUser().getDisplayName();

        boolean isAllowToDelete = true;
        String errMsg = "";
        
        // NOT OTHER COM ROLE
        // WITH OPEN ITEM FOR THIS WORKGROUP
        if(claimService.isOpenClaimByWorkgroupExist(selectedWorkgroupId)){

            if(RoleHelper.isCheckSelectedRoleExist(model.getUser().getRoles(), "ROLE_INS_COM")
                    && !userService.isOtherWorkgroupEnableCOMUserWithWorkgroupExist(selectedInsurerId, selectedWorkgroupId, this.webUserId)){                
                isAllowToDelete = false;
                errMsg = "User "+selectedUserName+" is the last user that has an Insurer Claim Ownership Manager role and is assigned to Workgroup "+selectedWorkgroupName+". Are you sure you want to remove this workgroup?";
            }

            /*
            // CH
            if(RoleHelper.isCheckSelectedRoleExist(model.getUser().getRoles(), "ROLE_INS_CH")
                    && !userService.isOtherWorkgroupEnableCHUserWithWorkgroupExist(selectedInsurerId, selectedWorkgroupId, this.webUserId)){
                isAllowToDelete = false;
                errMsg = "User "+selectedUserName+" is the last user that has an Insurer Claim Handler role and is assigned to Workgroup "+selectedWorkgroupName+". Are you sure you want to remove this workgroup?<br/>";
            }
            */
        }
        
        workgroupValidationMsg = "{isAllowToDelete:"+isAllowToDelete+",warningMsg:'"+errMsg+"'}";
        return SUCCESS;
    }

    public String removeObject(){

        if(userWorkgroupId>0){

            boolean isAllowToDelete = true;
            String errMsg = "";
            UserWorkgroup model = service.getObject(userWorkgroupId);

            int selectedWorkgroupId = model.getWorkgroup().getId();
            String selectedWorkgroupName = model.getWorkgroup().getName();
            String selectedUserName = model.getUser().getDisplayName();
            
            // CH USER AND OWNERSHIP IS TRUE
            if(RoleHelper.isUserCheckByOwnership(model.getUser())
                    && claimService.isOpenClaimByWorkgroupByUserExist(selectedWorkgroupId, this.webUserId)){
                isAllowToDelete = false;
                errMsg += "User '"+selectedUserName+"' has open claim(s) assigned to them within workgroup '"+selectedWorkgroupName+"', it is not possible to remove the assignment of a Workgroup against a user who has open claim(s).<br/>";
            }
            
            if(isAllowToDelete){
                
                String ackMsg = "Workgroup '" + model.getWorkgroup().getName() + "' has been removed";
                service.DeleteObject(model);
                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
                
            }else{
                getActionResponse().AddError(errMsg);
            }
            
        }else{
            
            getActionResponse().AddError("Selected workgroup is not valid");
            
        }

        return SUCCESS;
        
    }
    
    public String addObject(){
                
        try{
            
            Workgroup selectedWorkgroup = workgroupService.getObject(workgroupId);
            WebUser webuser = this.userService.getObject(webUserId);
            
            if(this.service.isObjectExist(webUserId, workgroupId)){
                
                getActionResponse().AddError("Selected workgroup '"+selectedWorkgroup.getName()+"' is already exist");
                
            }else{
                
                UserWorkgroup userworkgroup = new UserWorkgroup();
                userworkgroup.setUser(webuser);
                userworkgroup.setWorkgroup(selectedWorkgroup);
            
                this.service.AddObject(userworkgroup);
            }

        } catch (Exception ex) {
            
            ex.printStackTrace();
            
        }
        
        
        return SUCCESS;
    }    
     
}
