package idas.chox.service.admin;

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
import idas.chox.data.services.DataService;
import idas.chox.service.ActionResponse;
import java.util.ArrayList;
import java.util.List;

public class AdminUserWorkgroupService extends DataService {

    private ActionResponse actionResponse;
    private UserService userService;
    private ClaimService claimService;
    private WorkgroupService workgroupService;
    private UserWorkgroupService userWorkgroupService;

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService) {
        this.userWorkgroupService = userWorkgroupService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    public List getWorkgroups(int webUserId) {

        List items = new ArrayList<IdLookupItem>();

        WebUser webUser = userService.getWebUser(webUserId);
        List<Workgroup> availableWorkgroups = workgroupService.getAvailableUserWorkgroupsByInsurer(webUser.getInsurer().getId(), webUserId);

        for (Workgroup s : availableWorkgroups) {
            items.add(new IdLookupItem(s.getId(), s.getName()));
        }

        return items;
    }

    public ActionResponse checkUserWorkgroupAllowToDelete(int userWorkgroupId) {

        this.actionResponse = new ActionResponse();

        List<String> userRolesWithError = new ArrayList<String>();

        WebUserWorkgroup webUserWorkgroup = userWorkgroupService.getUserWorkgroup(userWorkgroupId);

        if (claimService.isOpenClaimByWorkgroupExist(webUserWorkgroup.getWorkgroup().getId())) {

            if (isLastUserRoleForSelectedWorkgroup(WebUserRole.ROLE_CH, webUserWorkgroup)) {
                userRolesWithError.add("Insurer Claim Handler Role");
            }

            if (isLastUserRoleForSelectedWorkgroup(WebUserRole.ROLE_COM, webUserWorkgroup)) {
                userRolesWithError.add("Insurer Claim Ownership Manager Role");
            }

            if (isLastUserRoleForSelectedWorkgroup(WebUserRole.ROLE_FNOL, webUserWorkgroup)) {
                userRolesWithError.add("Insurer FNOL Handler Role");
            }

        }

        if (userRolesWithError.size() > 0) {

            String userRoles = "";

            for (String s : userRolesWithError) {
                userRoles += s + ", ";
            }

            if (userRoles.length() >= 2) {
                userRoles.substring(0, (userRoles.length() - 1));
            }

            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User '" + webUserWorkgroup.getUser().getDisplayName() + "' is the last user that has " + userRoles + " and is assigned to Workgroup '" + webUserWorkgroup.getWorkgroup().getName() + "'. Are you sure you want to remove this Workgroup?");
        }

        return this.actionResponse;
    }

    private boolean isLastUserRoleForSelectedWorkgroup(String selectedRole, WebUserWorkgroup webUserWorkgroup) {
        boolean isRoleExist = RoleHelper.isCheckSelectedRoleExist(webUserWorkgroup.getUser().getRoles(), selectedRole);
        boolean isSelectedWorkgroupOwnByOther = userService.isWorkgroupOwnByOtherUserByRole(webUserWorkgroup.getUser(), webUserWorkgroup.getWorkgroup().getId(), selectedRole);
        return (isRoleExist && !isSelectedWorkgroupOwnByOther);
    }

    public ActionResponse removeWebUserWorkgroupMapping(int userWorkgroupId) {

        this.actionResponse = new ActionResponse();

        if (userWorkgroupId > 0) {

            WebUserWorkgroup webUserWorkgroup = userWorkgroupService.getUserWorkgroup(userWorkgroupId);
            boolean isOpenItemForUser = claimService.isOpenClaimByWorkgroupIdByUserExist(webUserWorkgroup.getUser().getInsurer().getId(), webUserWorkgroup.getWorkgroup().getId(), webUserWorkgroup.getUser().getId());

            if (webUserWorkgroup.getUser().getInsurer().isClaimOwnershipEnable() && isOpenItemForUser && RoleHelper.isCheckSelectedRoleExist(webUserWorkgroup.getUser().getRoles(), WebUserRole.ROLE_CH)) {
                getActionResponse().AddError("User '" + webUserWorkgroup.getUser().getDisplayName() + "' has open claim(s) assigned to them within Workgroup '" + webUserWorkgroup.getWorkgroup().getName() + "', it is not possible to remove the assignment of a Workgroup against a user who has open claim(s)");
            } else {
                userWorkgroupService.deleteWebUserWorkgroup(webUserWorkgroup);
            }

        } else {

            getActionResponse().AddError("Selected Workgroup is not valid");

        }

        return this.actionResponse;

    }

    public ActionResponse addNewWebUserWorkgroupMapping(int workgroupId, int webUserId) {

        this.actionResponse = new ActionResponse();

        Workgroup selectedWorkgroup = workgroupService.getWorkgroup(workgroupId);
        WebUser webuser = this.userService.getWebUser(webUserId);

        if (this.userWorkgroupService.isUserWorkgroupExist(workgroupId, webUserId)) {
            getActionResponse().AddError("Selected workgroup '" + selectedWorkgroup.getName() + "' is already exist");
        } else {

            WebUserWorkgroup userworkgroup = new WebUserWorkgroup();
            userworkgroup.setUser(webuser);
            userworkgroup.setWorkgroup(selectedWorkgroup);
            this.userWorkgroupService.saveWebUserWorkgroup(userworkgroup);
        }

        return this.actionResponse;

    }
}
