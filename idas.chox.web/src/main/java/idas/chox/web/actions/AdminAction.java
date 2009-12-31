/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;
import idas.chox.service.security.PermissionedUser;

public class AdminAction extends BaseAction {

    private String adminPanelName;
    private String actionResult;

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

    public int getCurrentUserOrganisationType(){
        return getUserOrganisationType();
    }

    public int getCurrentUserOrganisationId() {
        return getUserOrganisationId();
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
