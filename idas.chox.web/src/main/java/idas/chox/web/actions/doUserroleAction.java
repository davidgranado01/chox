package idas.chox.web.actions;

import idas.chox.service.admin.AdminUserRoleService;
import idas.chox.service.ActionResponse;
import java.util.List;

public class doUserroleAction extends BaseAction {

    private int organisationTypeId;
    private int webUserId;
    private int webUserUserRoleId;
    private int webUserRoleId;
    private String webUserRoleCode;
    private AdminUserRoleService adminUserRoleService;

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(int organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }

    public int getWebUserRoleId() {
        return webUserRoleId;
    }

    public void setWebUserRoleId(int webUserRoleId) {
        this.webUserRoleId = webUserRoleId;
    }

    public int getWebUserUserRoleId() {
        return webUserUserRoleId;
    }

    public void setWebUserUserRoleId(int webUserUserRoleId) {
        this.webUserUserRoleId = webUserUserRoleId;
    }

    public String getWebUserRoleCode() {
        return webUserRoleCode;
    }

    public void setWebUserRoleCode(String webUserRoleCode) {
        this.webUserRoleCode = webUserRoleCode;
    }

    // </editor-fold>
    
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public List getAvailableUserroles() {
        return adminUserRoleService.getAvailableUserroles(organisationTypeId, webUserId);
    }

    public String addNewWebUserRoleMapping() {

        if (webUserId > 0 && webUserRoleId > 0) {

            try {

                ActionResponse response = adminUserRoleService.addNewWebUserRoleMapping(webUserId, webUserRoleId);
                setActionResponse(response);
                
            } catch (Exception ex) {
                handleException(this, ex);
                return ERROR;
            }
        }

        return SUCCESS;
    }

    public String removeWebUserRoleMapping() {
        
        if (this.webUserUserRoleId > 0) {
            
            try {
                ActionResponse response = adminUserRoleService.deleteWebUserRoleMapping(this.webUserUserRoleId);
                setActionResponse(response);
                
            } catch (Exception ex) {
                handleException(this, ex);
                return ERROR;
            }

        }
        
        return SUCCESS;
        
    }

    public void setAdminUserRoleService(AdminUserRoleService adminUserRoleService) {
        this.adminUserRoleService = adminUserRoleService;
    }

    public String checkRoleAllowToDelete() {

        try {

            ActionResponse response = adminUserRoleService.ValidateRoleToBeDeleted(this.webUserId, this.webUserRoleCode);
            setActionResponse(response);
            
        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }
        
        return SUCCESS;

    }
    
}
