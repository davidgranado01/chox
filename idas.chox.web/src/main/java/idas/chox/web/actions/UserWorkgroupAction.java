package idas.chox.web.actions;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import idas.chox.web.viewdata.UserWorkgroupViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import org.springframework.security.annotation.Secured;

public class UserWorkgroupAction extends BaseAction {

    private List<UserWorkgroupViewData> userworkgroups;
    private int webUserId;
    private int organisationTypeId;
    private int userWorkgroupId;
    private int workgroupId;
    private AdminUserService adminUserService;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.userworkgroups);
        return "{totalCount:" + this.userworkgroups.size() + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
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

    public int getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(int organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    public List<UserWorkgroupViewData> getUserworkgroups() {
        return userworkgroups;
    }

    public void setUserworkgroups(List<UserWorkgroupViewData> userworkgroups) {
        this.userworkgroups = userworkgroups;
    }

    public int getWebUserId() {
        return this.webUserId;
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

    public String getUserWorkgroups() {

        List<WebUserWorkgroup> userworkgroupData = adminUserService.getUserWorkgroupsByUserId(webUserId);
        userworkgroups = new ArrayList<UserWorkgroupViewData>();

        for (WebUserWorkgroup h : userworkgroupData) {
            userworkgroups.add(new UserWorkgroupViewData(h));
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public List getAvailableWorkgroups() {
        List items = new ArrayList<IdLookupItem>();
        try {
            items = adminUserService.getWorkgroups(this.webUserId);
        } catch (Exception ex) {
            handleException(ex);
        }
        return items;
    }

    public String checkUserWorkgroupAllowToDelete() {

        try {

            ActionResponse response = adminUserService.checkUserWorkgroupAllowToDelete(this.userWorkgroupId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public String removeWebUserWorkgroupMapping() {

        try {

            ActionResponse response = adminUserService.removeWebUserWorkgroupMapping(this.workgroupId, this.webUserId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;

    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public String addNewWebUserWorkgroupMapping() {

        try {

            ActionResponse response = adminUserService.addNewWebUserWorkgroupMapping(this.workgroupId, this.webUserId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
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
