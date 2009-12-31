package idas.chox.web.actions;

import idas.chox.core.model.IdLookupItem;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserWorkgroupService;
import java.util.ArrayList;
import java.util.List;

public class doUserWorkgroupAction extends BaseAction {

    private int organisationTypeId;
    private int webUserId;
    private int userWorkgroupId;
    private int workgroupId;
    private AdminUserWorkgroupService adminUserWorkgroupService;

    public void setAdminUserWorkgroupService(AdminUserWorkgroupService adminUserWorkgroupService) {
        this.adminUserWorkgroupService = adminUserWorkgroupService;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public int getUserWorkgroupId() {
        return userWorkgroupId;
    }

    public void setUserWorkgroupId(int userWorkgroupId) {
        this.userWorkgroupId = userWorkgroupId;
    }

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }

    public int getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(int organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public List getWorkgroups() {

        List items = new ArrayList<IdLookupItem>();

        try {

            items = adminUserWorkgroupService.getWorkgroups(this.webUserId);

        } catch (Exception ex) {
            handleException(this, ex);
        }

        return items;
    }

    public String checkUserWorkgroupAllowToDelete() {

        try {

            ActionResponse response = adminUserWorkgroupService.checkUserWorkgroupAllowToDelete(this.userWorkgroupId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeWebUserWorkgroupMapping() {

        try {

            ActionResponse response = adminUserWorkgroupService.removeWebUserWorkgroupMapping(this.userWorkgroupId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;

    }

    public String addNewWebUserWorkgroupMapping() {

        try {

            ActionResponse response = adminUserWorkgroupService.addNewWebUserWorkgroupMapping(this.workgroupId, this.webUserId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;

    }
}
