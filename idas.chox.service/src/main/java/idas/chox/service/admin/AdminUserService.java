package idas.chox.service.admin;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.RoleHelper;
import idas.chox.data.services.DataService;
import idas.chox.service.ActionResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;

public class AdminUserService extends DataService {

    private ActionResponse actionResponse;
    private UserService userService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;
    private WebUserUserRoleService webUserUserRoleService;
    private ClaimService claimService;
    private WorkgroupService workgroupService;
    private UserWorkgroupService userWorkgroupService;

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    // <editor-fold defaultstate="collapsed" desc="USERS">

    public ActionResponse updateUser(WebUser webUser) {

        this.actionResponse = new ActionResponse();

        if (!this.userService.isUserNameExist(webUser.getUserName(), webUser.getId())) {
            this.userService.saveUser(webUser);
        } else {
            this.getActionResponse().AddError("User Name is already exist!");
        }

        return this.actionResponse;
    }

    public ActionResponse doAddNewUser(WebUser webUser, Integer insurerId, Integer supplierId, Integer organisationTypeId) {


        if (!this.userService.isUserNameExist(webUser.getUserName())) {

            webUser.setIsExpired(true);

            if (insurerId > 0) {
                Insurer selectInsurer = insurerService.getInsurer(insurerId);
                webUser.setInsurer(selectInsurer);
            }

            if (supplierId > 0) {
                Chorganisation selectChorganisation = chorganisationService.getChorganisation(supplierId);
                webUser.setChorganisation(selectChorganisation);
            }

            webUser.setPassword(encodePassword(webUser.getPassword()));

            this.userService.saveUser(webUser);

            webUserUserRoleService.addBaseNewUserRole(webUser.getId(), organisationTypeId);
            this.getActionResponse().AssignNewIdResult(webUser.getId());


        } else {
            this.getActionResponse().AddError("User Name is already exist!!");
        }

        return this.actionResponse;

    }

    public WebUser getUser(int userId) {
        return this.userService.getWebUser(userId);
    }

    public List<WebUser> getUsers(int organisationId, int organisationTypeId, int userRoleId) {
        return this.userService.getUsers(organisationId, organisationTypeId, userRoleId);
    }

    public ActionResponse updateUserPassword(WebUser webUser) {

        this.actionResponse = new ActionResponse();
        webUser.setPassword(encodePassword(webUser.getPassword()));
        this.userService.saveUser(webUser);
        return this.actionResponse;

    }

    private String encodePassword(String sPassword) {
        PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        return passwordEncoder.encodePassword(sPassword, null);
    }

    public ActionResponse triggerUserStatus(WebUser webUser) {

        this.actionResponse = new ActionResponse();

        webUser.setStatus(!webUser.getStatus());

        boolean isAllowUpdate = true;

        if (!webUser.getStatus() && claimService.isUserHasOpenClaim(webUser.getId())) {
            String ackMsg = "This user currently has assigned claims. Please reassign these claims before de-activating this user account";
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
            isAllowUpdate = false;
        }

        if (isAllowUpdate) {
            this.userService.saveUser(webUser);
        }


        return this.actionResponse;
    }

    public ActionResponse triggerPasswordExpiredStatus(WebUser webUser) {
        this.actionResponse = new ActionResponse();
        webUser.setIsExpired(!webUser.getIsExpired());
        this.userService.saveUser(webUser);
        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="USER ROLES">
    public List<WebUserUserRole> getMappedUserRole(int webUserId){
        return webUserUserRoleService.getMappedUserRole(webUserId);
    }

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

    public ActionResponse deleteWebUserRoleMapping(int webUserUserRoleId) {

        WebUserUserRole object = this.webUserUserRoleService.getWebUserUserRole(webUserUserRoleId);
        this.webUserUserRoleService.deleteWebUserUserRole(object);
        return this.actionResponse;
    }

    public List getAvailableUserroles(int organisationTypeId, int webUserId) {
        return this.webUserUserRoleService.getSelectedUserAvailableRoleLookupItem(organisationTypeId, webUserId);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="USER WORKGROUP">
    public List<WebUserWorkgroup> getUserWorkgroupsByUserId(int webUserId) {

        return userWorkgroupService.getUserWorkgroupsByUser(webUserId);
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }
    
    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
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
    // </editor-fold>
}
