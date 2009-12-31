package idas.chox.service.admin;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.RoleHelper;
import idas.chox.data.services.DataService;
import idas.chox.service.ActionResponse;
import java.util.List;

public class AdminUserRoleService extends DataService {

    private ActionResponse actionResponse;
    private UserService userService;
    private ClaimService claimService;
    private WebUserUserRoleService webUserUserRoleService;
    private UserWorkgroupService userWorkgroupService;

    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService) {
        this.userWorkgroupService = userWorkgroupService;
    }

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    // <editor-fold defaultstate="collapsed" desc="USERROLE - ADD">
    public ActionResponse addNewWebUserRoleMapping(int webUserId, int webUserRoleId) {

        this.actionResponse = new ActionResponse();

        WebUser webUser = userService.getWebUser(webUserId);

        this.webUserUserRoleService.addNewUserRole(webUserId, webUserRoleId);

        if (webUser.getOrganisationType().equalsIgnoreCase(OrganisationType.INS)) {

            if (webUserUserRoleService.isClaimHandlerRole(webUserRoleId) && webUser.getInsurer().isWorkgroupEnable() && (userWorkgroupService.getUserWorkgroupsByUser(webUserId).size()) <= 0) {
                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Please assign one or more Workgroup(s) to this user");
            }
        }

        return getActionResponse();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="USERROLE - REMOVE VALIDATION">
    public ActionResponse ValidateRoleToBeDeleted(int webUserId, String webUserRoleCode) {

        this.actionResponse = new ActionResponse();
        WebUser webUser = userService.getWebUser(webUserId);

        if (webUser.getInsurer() != null) {
            if (webUser.getInsurer().isWorkgroupEnable()) {
                doInsurerUserRoleValidation(webUser, webUserRoleCode);
            } else {
                if (webUser.getInsurer().isClaimOwnershipEnable()) {
                    // CLAIM OWNERSHIP
                    if (claimService.isUserHasOpenClaim(webUser.getId())) {
                         getActionResponse().AddError("User " + webUser.getDisplayName() + " has open claim(s) assigned to them, it is not possible to remove the assignment of a 'Claim Handler' Role against a user who has open claim(s)");
                    }
                }
            }
        }

        return getActionResponse();

    }

    private void doInsurerUserRoleValidation(WebUser webUser, String webUserRoleCode) {

        if (RoleHelper.isUserCheckByWorkgroup(webUser)) {

            if (webUser.getWorkgroupIds().size() > 0) {
                // WITH WORKGROUP EXIST

                if (webUser.getWorkgroupRelatedRoles().size() == 1) {
                    // HAS ONLY ONE WORKGROUP RELATED ROLES
                    
                    getActionResponse().AddError("It is not possible to remove this role against a user who has workgroup(s). Please remove the workgroup(s) from this user.");

                } else {

                    doValidateWebUserByRole(webUser, webUserRoleCode);

                }

            }

        }

    }

    private void doValidateWebUserByRole(WebUser webUser, String webUserRoleCode) {

        if (webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_COM)) {
            doComRoleValidation(webUser);
        } else if (webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_FNOL)) {
            doFnolRoleValidation(webUser);
        } else if (webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_CH)) {
            doClaimHandlerRoleValidation(webUser);
        }

    }

    public void doComRoleValidation(WebUser webUser) {

        boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupsByUserExist(webUser.getInsurer().getId(), webUser.getWorkgroupIds(), -1);
        boolean hasOtherUsers = userService.isWorkgroupOwnByOtherUserByRole(webUser, WebUserRole.ROLE_COM);

        if (hasOpenClaims && !hasOtherUsers) {
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User " + webUser.getDisplayName() + " is the last user that has 'Claim Ownership Manager' and is assigned to Workgroup(s). Are you sure you want to remove this role?");
            
        }

    }

    public void doFnolRoleValidation(WebUser webUser) {

        boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupsByStatusExist(webUser.getInsurer().getId(), webUser.getWorkgroupIds(), ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        boolean hasOtherUsers = userService.isWorkgroupOwnByOtherUserByRole(webUser, WebUserRole.ROLE_FNOL);

        if (hasOpenClaims && !hasOtherUsers) {
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User " + webUser.getDisplayName() + " is the last user that has 'Insurer FNOL Handler' and is assigned to Workgroup(s). Are you sure you want to remove this role?");
            
        }
    }

    public void doClaimHandlerRoleValidation(WebUser webUser) {

        if (webUser.getInsurer().isClaimOwnershipEnable()) {

            // CLAIM OWNERSHIP ENABLED
            if (claimService.isUserHasOpenClaim(webUser.getId())) {
                getActionResponse().AddError("User " + webUser.getDisplayName() + " has open claim(s) assigned to them, it is not possible to remove the assignment of a 'Claim Handler' Role against a user who has open claim(s)");
            }

        } else {

            // WOPRKGROUP ENABLED ONLY
            boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupsByUserExist(webUser.getInsurer().getId(), webUser.getWorkgroupIds(), -1);
            boolean hasOtherUsers = userService.isWorkgroupOwnByOtherUserByRole(webUser, WebUserRole.ROLE_CH);

            if (hasOpenClaims && !hasOtherUsers) {

                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User " + webUser.getDisplayName() + " is the last user that has 'Claim Handler' and is assigned to Workgroup(s). Are you sure you want to remove this role?");

            }
        }

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="USERROLE - DELETE">
    public ActionResponse deleteWebUserRoleMapping(int webUserUserRoleId) {
        
        WebUserUserRole object = this.webUserUserRoleService.getObject(webUserUserRoleId);
        this.webUserUserRoleService.DeleteObject(object);
        return this.actionResponse;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="USERROLE - GET LIST">
    public List getAvailableUserroles(int organisationTypeId, int webUserId) {
        return this.webUserUserRoleService.getSelectedUserAvailableRoleLookupItem(organisationTypeId, webUserId);
    }
    // </editor-fold>
}
