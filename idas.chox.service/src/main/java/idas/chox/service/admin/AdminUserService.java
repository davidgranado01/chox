package idas.chox.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.PasswordHistory;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.model.Workgroup;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.RoleHelper;
import idas.chox.data.services.SecureDataService;
import idas.chox.service.ActionResponse;

public class AdminUserService extends SecureDataService {

    private static final Logger LOG = LoggerFactory.getLogger(AdminUserService.class);
    private ActionResponse actionResponse;
    private UserService userService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;
    private WebUserUserRoleService webUserUserRoleService;
    private ClaimService claimService;
    private WorkgroupService workgroupService;
    private UserWorkgroupService userWorkgroupService;
    private final String passwordPatternString = "^.*(?=.{<minPasswordLength>,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$";

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public ActionResponse updateUserBrowserWarning(int webUserId, boolean showSplash) {
        this.actionResponse = new ActionResponse();
        WebUser webUser = userService.getWebUser(webUserId);
        webUser.setShowSplash(showSplash);
        userService.saveUser(webUser);
        this.actionResponse.AssignMessageResult("Browser warning will not be shown in the future.");
        return this.actionResponse;
    }

    // <editor-fold defaultstate="collapsed" desc="USERS">
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public ActionResponse updateUser(WebUser webUser) {
        this.actionResponse = new ActionResponse();
        if (!this.userService.isUserNameExist(webUser.getUserName(), webUser.getId())) {
            this.userService.saveUser(webUser);
        } else {
            this.getActionResponse().AddError("User Name already exists in CHOX");
        }
        return this.actionResponse;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public ActionResponse updateUserTelephone(int webUserId, String newTelephone) {
        LOG.debug("Updating user telephone number to '{}'", newTelephone);
        this.actionResponse = new ActionResponse();
        WebUser webUser = userService.getWebUser(webUserId);
        webUser.setTelephone(newTelephone);
        userService.saveUser(webUser);
//        this.evict(webUser); 
        LOG.debug("DONE Updating user telephone for user '{}'", webUser.getId());
        this.actionResponse.AssignMessageResult("Your contact telephone number has been updated.");
        return this.actionResponse;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public ActionResponse updateUserPassword(int webUserId, String newPassword, String oldPassword) {
        int minPasswordLength = 8;
        WebUser webUser = userService.getWebUser(webUserId);
        if (webUser.isAnInsurer()) {
            minPasswordLength = webUser.getInsurer().getMinimumPasswordLength();
        }
        else if (!webUser.isCHOXAdmin()) {
            minPasswordLength = webUser.getChorganisation().getMinimumPasswordLength();
        }
        
        this.actionResponse = new ActionResponse();
        Pattern passwordPattern = Pattern.compile(passwordPatternString.replace("<minPasswordLength>", Integer.toString(minPasswordLength)));
        if (!passwordPattern.matcher(newPassword).matches()) {
            LOG.warn("Invalid password found: {}", newPassword);
            this.actionResponse.AddError("Invalid password provided");
            return this.actionResponse;
        }

        if (!webUser.getPassword().equals(encodePassword(oldPassword))) {
            LOG.debug("Error trying to update user password for user '{}'", webUser.getId());
            LOG.debug("Current password is '{}' but got '{}'", webUser.getPassword(), encodePassword(oldPassword));
            this.actionResponse.AddError("'Current Password' is not correct.");
        } else if (webUser.getPassword().equals(encodePassword(newPassword))) {
            this.actionResponse.AddError("'New password' is the same as the old one.");
        } else if (!validatePasswordHistory(webUserId, encodePassword(newPassword))) {
            this.actionResponse.AddError("'New password' is the same as a previous one.");
        } else {
            LOG.debug("Setting new password '{}'(encoded '{}')", newPassword, encodePassword(newPassword));
            webUser.setPassword(encodePassword(newPassword));
            webUser.setIsExpired(Boolean.FALSE);
            webUser.setPasswordLastModifiedDate(new Date());
            userService.saveUser(webUser);
//            this.evict(webUser);
            LOG.debug("DONE Updating user password for user '{}'", webUser.getId());
            this.actionResponse.AssignMessageResult("Your password has been changed.");
        }
        return this.actionResponse;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public ActionResponse doAddNewUser(WebUser webUser, Integer insurerId, Integer supplierId, Integer organisationTypeId) {

        this.actionResponse = new ActionResponse();

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
            webUser.setPasswordLastModifiedDate(new Date());
            userService.saveUser(webUser);
            webUserUserRoleService.addBaseNewUserRole(webUser.getId(), organisationTypeId);
            this.actionResponse.AssignNewIdResult(webUser.getId());

        } else {
            this.actionResponse.AddError("User Name already exists in CHOX");
        }
        return this.actionResponse;
    }

    public WebUser getUser(int userId) {
        return this.userService.getWebUser(userId);
    }

    public SearchResult getUsers(int organisationId, int organisationTypeId, int userRoleId, int start, int limit, String sort, String dir, boolean activeUsersOnly) {
        return this.userService.getUsers(organisationId, organisationTypeId, userRoleId, start, limit, sort, dir, activeUsersOnly);
    }
    

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public ActionResponse updateUserPassword(WebUser webUser) {
        int minPasswordLength = 8;

        if (webUser.isAnInsurer()) {
            minPasswordLength = webUser.getInsurer().getMinimumPasswordLength();
        }
        else if (!webUser.isCHOXAdmin()) {
            minPasswordLength = webUser.getChorganisation().getMinimumPasswordLength();
        }
        
        this.actionResponse = new ActionResponse();
        Pattern passwordPattern = Pattern.compile(passwordPatternString.replace("<minPasswordLength>", Integer.toString(minPasswordLength)));
        if (!passwordPattern.matcher(webUser.getPassword()).matches()) {
            LOG.warn("Invalid password found: {}", webUser.getPassword());
            this.actionResponse.AddError("Invalid password provided");
        } else if (!validatePasswordHistory(webUser.getId(), encodePassword(webUser.getPassword()))) {
            LOG.warn("Invalid password found: {} (matches previous password)", webUser.getPassword());
            this.actionResponse.AddError("New password is the same as a previous one.");
        } else {
            LOG.debug("Updating user password to '{}'", webUser.getPassword());
            webUser.setPassword(encodePassword(webUser.getPassword()));
            webUser.setPasswordLastModifiedDate(new Date());
            this.userService.saveUser(webUser);
        }
        return this.actionResponse;

    }

    public String encodePassword(String sPassword) {
        PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        return passwordEncoder.encodePassword(sPassword, null);
    }

    public ActionResponse triggerUserStatus(WebUser webUser) {

        this.actionResponse = new ActionResponse();

        if (webUser.isBlocked()) {
            webUser.setBlocked(false);
            webUser.setStatus(true);
            webUser.setFailedLoginAttempts(0);
            webUser.setBlockedDate(null);
            webUser.setStatus(true);
        }
        else {
            webUser.setStatus(!webUser.getStatus());
            // CHOX-313: clear last_login_date when user activated
            if (webUser.getStatus()) {
                webUser.setLastLoginDate(null);
            }
        }

        boolean isAllowUpdate = true;
        

        if (!webUser.getStatus() && claimService.isUserHasOpenClaim(webUser.getId(), (webUser.getInsurer() != null))) {
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "This user currently has assigned claims. Please reassign these claims before de-activating this user account");
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

    public List<IdLookupItem> getAvailableUserRoles(int organisationTypeId, int webUserId) {
        return getAvailableUserRoles(organisationTypeId, webUserId, true, true, true, true, true, true, true);
    }

    public Set<WebUserRole> getAllAvailableUserRoles(int organisationTypeId, boolean isWorkgroupEnebled,
                                    boolean isClaimownershipEnabled, boolean isFnolEnabled,
                                    boolean isEngineersEnabled, boolean isInsurerUploadEnabled,
                                    boolean isSupervisorEnabled, boolean isAdmin) {
        return getAllAvailableUserRoles(organisationTypeId, isWorkgroupEnebled, isClaimownershipEnabled, isFnolEnabled, isEngineersEnabled, isInsurerUploadEnabled, isSupervisorEnabled, isAdmin, false);
    }

    public Set<WebUserRole> getAllAvailableUserRoles(int organisationTypeId, boolean isWorkgroupEnebled,
                                    boolean isClaimownershipEnabled, boolean isFnolEnabled,
                                    boolean isEngineersEnabled, boolean isInsurerUploadEnabled,
                                    boolean isSupervisorEnabled, boolean isAdmin, boolean canBeAssignedTasksOnly) {
        return this.webUserUserRoleService.getWebUserRoles(organisationTypeId, isWorkgroupEnebled, isClaimownershipEnabled, isFnolEnabled, isEngineersEnabled, isInsurerUploadEnabled, isSupervisorEnabled, isAdmin, canBeAssignedTasksOnly);
    }

    // <editor-fold defaultstate="collapsed" desc="USER ROLES">
    public List<IdLookupItem> getAvailableUserRoles(int organisationTypeId, int webUserId,
            boolean isWorkgroupEnebled, boolean isClaimownershipEnabled, boolean isFnolEnebled, 
            boolean isEngineersEnabled, boolean isInsurerUploadEnabled, boolean isSupervisorEnabled, boolean isAdmin) {
        List<IdLookupItem> availableUserRoles = this.webUserUserRoleService.getSelectedUserAvailableRoleLookupItem(organisationTypeId, webUserId, isWorkgroupEnebled, isClaimownershipEnabled, isFnolEnebled, isEngineersEnabled, isInsurerUploadEnabled, isSupervisorEnabled, isAdmin);

        return availableUserRoles;
    }

    public List<WebUserUserRole> getMappedUserRole(int webUserId) {
        return webUserUserRoleService.getMappedUserRole(webUserId);
    }

    public ActionResponse addNewWebUserRoleMapping(int webUserId, int webUserRoleId) {

        this.actionResponse = new ActionResponse();

        WebUser webUser = userService.getWebUser(webUserId);
        this.webUserUserRoleService.addNewUserRole(webUserId, webUserRoleId);

        if (webUser.getOrganisationType().equalsIgnoreCase(OrganisationType.INS)) {

            if (webUserUserRoleService.isWorkgroupRelatedRoles(webUserRoleId) && webUser.getInsurer().isWorkgroupEnable() && (userWorkgroupService.getUserWorkgroupsByUser(webUserId).size()) <= 0) {
                this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Please assign one or more Workgroup(s) to this user");
            }

        }

        return this.actionResponse;
    }

    public ActionResponse deleteWebUserRoleMapping(int webUserUserRoleId) {
        this.actionResponse = new ActionResponse();
        WebUserUserRole object = this.webUserUserRoleService.getWebUserUserRole(webUserUserRoleId);
        this.webUserUserRoleService.deleteWebUserUserRole(object);
        return this.actionResponse;
    }

    public ActionResponse validateRoleToBeDeleted(int webUserId, String webUserRoleCode) {

        this.actionResponse = new ActionResponse();

        WebUser webUser = userService.getWebUser(webUserId);

        if (webUserUserRoleService.isWorkgroupRelatedRolesByCode(webUserRoleCode)) {

            if (webUser.getInsurer() != null) {

                if (webUser.getInsurer().isWorkgroupEnable()) {

                    doInsurerUserRoleValidation(webUser, webUserRoleCode);

                } else {

                    if (webUser.getInsurer().isClaimOwnershipEnable()) {
                        if (claimService.isUserHasOpenClaim(webUser.getId(), (webUser.getInsurer() != null))) {
                            this.actionResponse.AddError("User " + webUser.getDisplayName() + " has open claim(s) assigned to them, it is not possible to remove the assignment of a 'Claim Handler' Role against a user who has open claim(s)");
                        }
                    }
                }
            }

        }

        return this.actionResponse;

    }

    private void doInsurerUserRoleValidation(WebUser webUser, String webUserRoleCode) {
        if (RoleHelper.isWorkgroupValidationEnabledUser(webUser)) {
            if (webUser.getWorkgroups().size() > 0) {
                // WITH WORKGROUP EXIST
                if (webUser.getWorkgroupRelatedRoles().size() == 1) {
                    // HAS ONLY ONE WORKGROUP RELATED ROLES
                    this.actionResponse.AddError("It is not possible to remove this role against a user who has workgroup(s). Please remove the workgroup(s) from this user.");
                } else {
                    doValidateWebUserByRole(webUser, webUserRoleCode);
                }
            }
        }
    }

    private void doValidateWebUserByRole(WebUser webUser, String webUserRoleCode) {
        if (webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_INS_COM)) {
            doComRoleValidation(webUser);
        } else if (webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_INS_FNOL)) {
            doFnolRoleValidation(webUser);
        } else if (webUserRoleCode.equalsIgnoreCase(WebUserRole.ROLE_INS_CH)) {
            doClaimHandlerRoleValidation(webUser);
        }
    }

    private void doComRoleValidation(WebUser webUser) {

        boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupsByUserExist(webUser.getInsurer().getId(), webUser.getWorkgroupIds(), -1);
        boolean hasOtherUsers = userService.isWorkgroupOwnByOtherUserByRole(webUser, WebUserRole.ROLE_INS_COM);

        if (hasOpenClaims && !hasOtherUsers) {
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User " + webUser.getDisplayName() + " is the last user that has 'Claim Ownership Manager' and is assigned to Workgroup(s). Are you sure you want to remove this role?");
        }
    }

    private void doFnolRoleValidation(WebUser webUser) {

        boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupsByStatusExist(webUser.getInsurer().getId(), webUser.getWorkgroupIds(), ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        boolean hasOtherUsers = userService.isWorkgroupOwnByOtherUserByRole(webUser, WebUserRole.ROLE_INS_FNOL);

        if (hasOpenClaims && !hasOtherUsers) {
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User " + webUser.getDisplayName() + " is the last user that has 'Insurer FNOL Handler' and is assigned to Workgroup(s). Are you sure you want to remove this role?");
        }
    }

    private void doClaimHandlerRoleValidation(WebUser webUser) {

        if (webUser.getInsurer().isClaimOwnershipEnable()) {

            // CLAIM OWNERSHIP ENABLED
            if (claimService.isUserHasOpenClaim(webUser.getId(), (webUser.getInsurer() != null))) {
                this.actionResponse.AddError("User " + webUser.getDisplayName() + " has open claim(s) assigned to them, it is not possible to remove the assignment of a 'Claim Handler' Role against a user who has open claim(s)");
            }

        } else {

            // WOPRKGROUP ENABLED ONLY
            boolean hasOpenClaims = claimService.isOpenClaimByWorkgroupsByUserExist(webUser.getInsurer().getId(), webUser.getWorkgroupIds(), -1);
            boolean hasOtherUsers = userService.isWorkgroupOwnByOtherUserByRole(webUser, WebUserRole.ROLE_INS_CH);

            if (hasOpenClaims && !hasOtherUsers) {
                this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User " + webUser.getDisplayName() + " is the last user that has 'Claim Handler' and is assigned to Workgroup(s). Are you sure you want to remove this role?");
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="USER WORKGROUP">
    public List<WebUserWorkgroup> getUserWorkgroupsByUserId(int webUserId) {
        return userWorkgroupService.getUserWorkgroupsByUser(webUserId);
    }

    public List<IdLookupItem> getWorkgroups(int webUserId) {

        List<IdLookupItem> items = new ArrayList<>();
        WebUser webUser = userService.getWebUser(webUserId);
        List<Workgroup> availableWorkgroups = workgroupService.getAvailableUserWorkgroupsByInsurer(webUser.getInsurer().getId(), webUserId);

        availableWorkgroups.forEach((s) -> {
            items.add(new IdLookupItem(s.getId(), s.getName()));
        });

        return items;
    }

    public ActionResponse checkUserWorkgroupAllowToDelete(int userWorkgroupId) {

        this.actionResponse = new ActionResponse();

        List<String> userRolesWithError = new ArrayList<>();

        WebUserWorkgroup webUserWorkgroup = userWorkgroupService.getUserWorkgroup(userWorkgroupId);

        if (claimService.isOpenClaimByWorkgroupExist(webUserWorkgroup.getWorkgroup().getId())) {

            if (isLastUserRoleForSelectedWorkgroup(WebUserRole.ROLE_INS_CH, webUserWorkgroup)) {
                userRolesWithError.add("Insurer Claim Handler Role");
            }

            if (isLastUserRoleForSelectedWorkgroup(WebUserRole.ROLE_INS_COM, webUserWorkgroup)) {
                userRolesWithError.add("Insurer Claim Ownership Manager Role");
            }

            if (isLastUserRoleForSelectedWorkgroup(WebUserRole.ROLE_INS_FNOL, webUserWorkgroup)) {
                userRolesWithError.add("Insurer FNOL Handler Role");
            }

        }

        if (userRolesWithError.size() > 0) {

            String userRoles = "";

            userRoles = userRolesWithError.stream().map((s) -> s + ", ").reduce(userRoles, String::concat);

            if (!userRoles.isEmpty()) {
                this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User '"
                        + webUserWorkgroup.getUser().getDisplayName() + "' is the last user that has "
                        + userRoles + "and is assigned to Workgroup '" + webUserWorkgroup.getWorkgroup().getName()
                        + "'. Are you sure you want to remove this Workgroup?");
            } else {

                this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_YESNO, "User '"
                        + webUserWorkgroup.getUser().getDisplayName()
                        + "' is the last user that is assigned to Workgroup '" + webUserWorkgroup.getWorkgroup().getName()
                        + "'. Are you sure you want to remove this Workgroup?");
            }
        }

        return this.actionResponse;
    }

    private boolean isLastUserRoleForSelectedWorkgroup(String selectedRole, WebUserWorkgroup webUserWorkgroup) {
        boolean isRoleExist = RoleHelper.isCheckSelectedRoleExist(webUserWorkgroup.getUser().getRoles(), selectedRole);
        boolean isSelectedWorkgroupOwnByOther = userService.isWorkgroupOwnByOtherUserByRole(webUserWorkgroup.getUser(), webUserWorkgroup.getWorkgroup().getId(), selectedRole);
        return (isRoleExist && !isSelectedWorkgroupOwnByOther);
    }

    public ActionResponse removeWebUserWorkgroupMapping(int workgroupId, int webUserId) {

        this.actionResponse = new ActionResponse();

        if (webUserId > 0 && workgroupId > 0) {

            WebUser webUser = userService.getWebUser(webUserId);
            Workgroup workgroup = workgroupService.getWorkgroup(workgroupId);

            boolean isOpenItemForUser = claimService.isOpenClaimByWorkgroupIdByUserExist(webUser.getInsurer().getId(), workgroupId, webUserId);

            if (webUser.getInsurer().isClaimOwnershipEnable() && isOpenItemForUser && RoleHelper.isCheckSelectedRoleExist(webUser.getRoles(), WebUserRole.ROLE_INS_CH)) {
                getActionResponse().AddError("User '" + webUser.getDisplayName() + "' has open claim(s) assigned to them within Workgroup '" + workgroup.getName() + "', it is not possible to remove the assignment of a Workgroup against a user who has open claim(s)");
            } else {
                webUser.getWorkgroups().remove(workgroup);
                userService.saveUser(webUser);
            }
        } else {

            getActionResponse().AddError("Selected Workgroup is not valid");

        }

        return this.actionResponse;

    }

    public ActionResponse addNewWebUserWorkgroupMapping(int workgroupId, int webUserId) {

        this.actionResponse = new ActionResponse();

        Workgroup workgroup = workgroupService.getWorkgroup(workgroupId);
        WebUser webuser = this.userService.getWebUser(webUserId);

        if (this.userWorkgroupService.isUserWorkgroupExist(workgroupId, webUserId)) {
            getActionResponse().AddError("Selected workgroup '" + workgroup.getName() + "' is already exist");
        } else {
            WebUserWorkgroup userworkgroup = new WebUserWorkgroup();
            userworkgroup.setUser(webuser);
            userworkgroup.setWorkgroup(workgroup);
            this.userWorkgroupService.saveUserWorkgroup(userworkgroup);
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

    private boolean validatePasswordHistory(int webUserId, String encodeNewPassword) {
        boolean passwordOk = true;
        WebUser webUser = userService.getWebUser(webUserId);
        int uniqueHistory = 0;
        LOG.debug("Checking password history for user: '{}' (id={})", webUser.getDisplayName(), webUserId);

        if (webUser.isAnInsurer() && webUser.getInsurer()!=null && webUser.getInsurer().getUniquePasswordHistory() > 1) {
            uniqueHistory = webUser.getInsurer().getUniquePasswordHistory();
        }
        else if (!webUser.isAnInsurer() && !webUser.isCHOXAdmin() && webUser.getChorganisation()!=null && webUser.getChorganisation().getUniquePasswordHistory() > 1) {
            uniqueHistory = webUser.getChorganisation().getUniquePasswordHistory();
        }

        if (passwordOk && uniqueHistory > 0) {
            List<PasswordHistory> passwordHistory = userService.getPasswordHistory(webUserId, uniqueHistory);
        
            for(PasswordHistory p: passwordHistory) {
                if (encodeNewPassword.equals(p.getPassword())) {
                    passwordOk = false;
                    break;
                }
            }
        }
        
        if (passwordOk) {
            // Add current password to password history
            PasswordHistory p = new PasswordHistory();
            p.setPassword(encodeNewPassword);
            p.setWebUser(webUser);
            userService.savePasswordHistory(p);
        }
        
        return passwordOk;
    }
}
