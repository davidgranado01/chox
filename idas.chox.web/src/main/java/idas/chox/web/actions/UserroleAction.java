package idas.chox.web.actions;

import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import idas.chox.web.viewdata.UserroleViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserroleAction extends BaseAction {

    private List<UserroleViewData> userroles;
    private int webUserId;
    private int organisationTypeId;
    private int webUserUserRoleId;
    private int webUserRoleId;
    private String webUserRoleCode;
    private AdminUserService adminUserService;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.userroles);
        return "{totalCount:" + this.userroles.size() + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(int organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
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

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getUseroles() {

        try {

            userroles = new ArrayList<UserroleViewData>();

            List<WebUserUserRole> userroleData = adminUserService.getMappedUserRole(webUserId);

            for (WebUserUserRole h : userroleData) {
                if (!h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHO) && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_INS) && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX)) {
                    userroles.add(new UserroleViewData(h));
                }
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public List getAvailableUserroles() {
        return adminUserService.getAvailableUserroles(organisationTypeId, webUserId);
    }

    public String addNewWebUserRoleMapping() {

        if (webUserId > 0 && webUserRoleId > 0) {

            try {

                ActionResponse response = adminUserService.addNewWebUserRoleMapping(webUserId, webUserRoleId);
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
                ActionResponse response = adminUserService.deleteWebUserRoleMapping(this.webUserUserRoleId);
                setActionResponse(response);

            } catch (Exception ex) {
                handleException(this, ex);
                return ERROR;
            }

        }

        return SUCCESS;

    }

    public String checkRoleAllowToDelete() {

        try {

            ActionResponse response = adminUserService.ValidateRoleToBeDeleted(this.webUserId, this.webUserRoleCode);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }
    // </editor-fold>
}