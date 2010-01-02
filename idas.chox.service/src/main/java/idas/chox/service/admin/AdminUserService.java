/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.admin;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.services.DataService;
import idas.chox.service.ActionResponse;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;

/**
 *
 * @author Carlson
 */
public class AdminUserService extends DataService {

    private ActionResponse actionResponse;
    private UserService userService;
    private LookupService lookupService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;
    private WebUserUserRoleService webUserUserRoleService;
    private ClaimService claimService;

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
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

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }


    public ActionResponse updateUser(WebUser webUser) {

        this.actionResponse = new ActionResponse();

        if (!this.userService.isUserNameExist(webUser.getUserName(), webUser.getId())) {
            this.userService.saveUser(webUser);
        } else {
            this.getActionResponse().AddError("User Name is already exist!");
        }


        return this.actionResponse;
    }

    public ActionResponse doAddNewUser(WebUser webUser, Integer insurerId, Integer supplierId, String organisationTypeId) {


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

            if (this.userService.saveUser(webUser)) {

                if (webUserUserRoleService.addBaseNewUserRole(webUser.getId(), Integer.valueOf(organisationTypeId))) {
                    this.getActionResponse().AssignNewIdResult(webUser.getId());
                }

            } else {
                this.getActionResponse().AddError("Please try again!");
            }

        } else {
            this.getActionResponse().AddError("User Name is already exist!!");
        }

        return this.actionResponse;
        
    }

    public ActionResponse updateUserPassword(WebUser webUser){
        
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
}
