/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.WebUser;
import chox.services.LookupService;
import chox.services.UserService;
import chox.web.security.PermissionedUser;
import java.util.List;
import org.acegisecurity.providers.encoding.PasswordEncoder;

public class AdminAction extends BaseAction{
    
    private String adminPanelName;
    private String gridViewType;
    private String actionResult;
    private int selectOrgTypeId=-1;
    private int selectOrgId=-1;
    private LookupService lookupService;
    private PermissionedUser currentUser = getAuthenticatedUser();
    
    private boolean isSelectable = false;
    
    public boolean isIsSelectable() {
        if(currentUser.getIsCHOXAdmin()){
            isSelectable = true;
        }
        return isSelectable;
    }
    
    public void setLookupService(LookupService lookupService)
    {
        this.lookupService = lookupService;
    }

    public int getSelectOrgId() {
        
        if(!currentUser.getIsCHOXAdmin()){
            if(currentUser.getIsCHO()){
                selectOrgId = currentUser.getUser().getChorganisation().getId();
            }else if(currentUser.getIsINS()){
                selectOrgId = currentUser.getUser().getInsurer().getId();
            }
        }
        
        return selectOrgId;
    }

    public void setSelectOrgId(int selectOrgId) {
        this.selectOrgId = selectOrgId;
    }

    
    public int getSelectOrgTypeId(){
        
        if(!currentUser.getIsCHOXAdmin()){
            if(currentUser.getIsCHO()){
                selectOrgTypeId = 3;
            }else if(currentUser.getIsINS()){
                selectOrgTypeId = 2;
            }
        }
        
        return selectOrgTypeId;
    }

    public void setSelectOrgTypeId(int selectOrgTypeId) {
        this.selectOrgTypeId = selectOrgTypeId;
    }
    
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

    public void getGridviewList(){
    }
    
    public String getGridViewType() {
        return gridViewType;
    }

    public void setGridViewType(String gridViewType) {
        this.gridViewType = gridViewType;
    }


    //Encode Password
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

    private UserService userService;

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return the actionResult
     */
    public String getActionResult() {
        return actionResult;
    }

    /**
     * @param actionResult the actionResult to set
     */
    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }



    
}
