package idas.chox.web.actions;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.RoleHelper;
import idas.chox.web.viewdata.ActionResponse;
import java.util.ArrayList;
import java.util.List;

public class doUserWorkgroupAction extends BaseAction {

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

        List<WebUserWorkgroup> selectedWorkgroups = service.getObjects(webUserId);
        List<Workgroup> availableWorkgroups = workgroupService.getObjects(insurerId);
        List<Integer> selectedList = new ArrayList<Integer>();

        for (WebUserWorkgroup o : selectedWorkgroups) {
            selectedList.add(o.getWorkgroup().getId());
        }

        for (Workgroup s : availableWorkgroups) {
            if (!selectedList.contains(s.getId())) {
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

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        return workgroupValidationMsg;
    }

    public String checkWorkgroupAllowToDelete(){

        boolean isAllowToDelete = true;
        String errMsg = "Are you sure you want to remove this workgroup?";

        WebUser wu = userService.getObject(this.webUserId);
        Workgroup wg = workgroupService.getObject(this.workgroupId);

        if(wu.getInsurer()!=null){

            if(wu.getInsurer().isWorkgroupEnable()){
                // WORKGROUP ENABLE VALIDATION

                if(RoleHelper.isUserCheckByWorkgroup(wu)){

                    if(claimService.isOpenClaimByWorkgroupExist(workgroupId)){

                        String sUserRoles = "";

                        if(RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_CH)
                            && !userService.isWorkgroupOwnByOtherUserByRole(wu, this.workgroupId, WebUserRole.ROLE_CH)){

                            // System.out.println("WG > CH : " + RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_CH));
                            sUserRoles = getUserRoleList(sUserRoles, "Insurer Claim Handler Role");
                            isAllowToDelete = false;

                        }

                        if(RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_COM)
                            && !userService.isWorkgroupOwnByOtherUserByRole(wu, this.workgroupId, WebUserRole.ROLE_COM)){

                            // System.out.println("WG > COM : " + RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_COM));
                            sUserRoles = getUserRoleList(sUserRoles, "Insurer Claim Ownership Manager Role");
                            isAllowToDelete = false;

                        }

                        if(RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_FNOL)
                            && !userService.isWorkgroupOwnByOtherUserByRole(wu, this.workgroupId, WebUserRole.ROLE_FNOL)){

                            // System.out.println("WG > FNOL : " + RoleHelper.isCheckSelectedRoleExist(wu.getRoles(), WebUserRole.ROLE_FNOL));
                            sUserRoles = getUserRoleList(sUserRoles, "Insurer FNOL Handler Role");
                            isAllowToDelete = false;

                        }

                        if(!isAllowToDelete){
                            errMsg = "User "+wu.getDisplayName()+" is the last user that has "+sUserRoles+" and is assigned to Workgroup "+wg.getName()+". Are you sure you want to remove this workgroup?";
                        }

                    }
                }
            }
        }

        workgroupValidationMsg = "{isAllowToDelete:"+isAllowToDelete+",warningMsg:'"+errMsg+"'}";
        return SUCCESS;
    }

    private String getUserRoleList(String iString, String userRoleName){

        if(iString.length()>0){
            iString += ", "+userRoleName;
        }else{
            iString = userRoleName;
        }

        return iString;
    }
    
        public String removeObject(){

        if(userWorkgroupId>0){

            boolean isAllowToDelete = true;
            String errMsg = "";

            WebUserWorkgroup model = service.getObject(userWorkgroupId);
            WebUser user = userService.getObject(this.webUserId);

            if(user.getInsurer()!=null){

                boolean isOpenItemForUser = claimService.isOpenClaimByWorkgroupIdByUserExist(user.getInsurer().getId(), model.getWorkgroup().getId(), this.webUserId);

                if(user.getInsurer().isClaimOwnershipEnable()
                        && isOpenItemForUser
                        && RoleHelper.isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH)){
                    isAllowToDelete = false;
                    errMsg += "User '"+user.getDisplayName()+"' has open claim(s) assigned to them within workgroup '"+model.getWorkgroup().getName()+"', it is not possible to remove the assignment of a Workgroup against a user who has open claim(s)";
                }
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

    public String addObject() {

        try {

            Workgroup selectedWorkgroup = workgroupService.getObject(workgroupId);
            WebUser webuser = this.userService.getObject(webUserId);

            if (this.service.isObjectExist(webUserId, workgroupId)) {

                getActionResponse().AddError("Selected workgroup '" + selectedWorkgroup.getName() + "' is already exist");

            } else {

                WebUserWorkgroup userworkgroup = new WebUserWorkgroup();
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
