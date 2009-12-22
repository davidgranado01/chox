/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;
import idas.chox.service.security.PermissionedUser;

public class AdminAction extends BaseAction {

    private String adminPanelName;
    private String actionResult;
    private PermissionedUser currentUser = getAuthenticatedUser();

    public String adminPanel() {
        return SUCCESS;
    }

    public String loadAdminPanel() {
        return this.adminPanelName;
    }

    public String getAdminPanelName() {
        return adminPanelName;
    }

    public void setAdminPanelName(String adminPanelName) {
        this.adminPanelName = adminPanelName;
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public int getOrgTypeId() {

        int OrgTypeId = 1;
        if (!currentUser.getIsCHOXAdmin()) {
            if (currentUser.getIsCHO()) {
                OrgTypeId = 3;
            } else if (currentUser.getIsINS()) {
                OrgTypeId = 2;
            }
        }

        return OrgTypeId;
    }

    public int getOrgId() {
        return getOrganisationId();
    }

    public WebUser getCurrentUser() {
        return currentUser.getUser();
    }

    public boolean isChoxAdmin() {
        return getIsChoxAdmin();
    }
    /*
    private int selectOrgTypeId = -1;
    private String gridViewType;
    private int selectOrgId = -1;
    private boolean isSelectable = false;
    public boolean isIsSelectable(){

    if(currentUser.getIsCHOXAdmin()){
    isSelectable = true;
    }

    return isSelectable;

    }
   
    public boolean getIsCHOXAdmin(){
    return currentUser.getIsCHOXAdmin();
    }
    
    public void setSelectOrgId(int selectOrgId) {

    this.selectOrgId = selectOrgId;

    }



    public void setSelectOrgTypeId(int selectOrgTypeId) {
    this.selectOrgTypeId = selectOrgTypeId;
    }
     */
    /*
    public String getGridViewType() {
    return gridViewType;
    }

    public void setGridViewType(String gridViewType) {
    this.gridViewType = gridViewType;
    }

    public String loadEncodePasswordPage()
    {
    return SUCCESS;
    }

    public String encodeAllUserPassword()
    {
    if(super.getIsChoxAdmin())
    {
    List<WebUser> webUsers = userService.getUsers();

    for(WebUser webUser : webUsers)
    {
    webUser.setPassword(encodePassword(webUser));
    userService.updateObject(webUser);
    }
    setActionResult("Encode All user password operation successed.");
    }

    return SUCCESS;
    }

    private String encodePassword(final WebUser webUser) {

    PasswordEncoder passwordEncoder = new org.acegisecurity.providers.encoding.Md5PasswordEncoder();
    return passwordEncoder.encodePassword(webUser.getPassword(), null);
    }
     */
    private UserService userService;
    private LookupService lookupService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }
}
