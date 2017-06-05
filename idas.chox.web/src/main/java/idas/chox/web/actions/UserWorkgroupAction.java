package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import idas.chox.web.viewdata.UserWorkgroupViewData;

public class UserWorkgroupAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(UserWorkgroupAction.class);

    private List<UserWorkgroupViewData> userworkgroups;
    private int webUserId;
    private int organisationTypeId;
    private int userWorkgroupId;
    private int workgroupId;
    private AdminUserService adminUserService;
    private UserWorkgroupService userWorkgroupService;

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_USER"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(userworkgroups);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting userworkgroups to json string.");
        }
        return "{totalCount:" + this.userworkgroups.size() + ",results:" + jsonString + "}";
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

    public UserWorkgroupService getUserWorkgroupService() {
        return userWorkgroupService;
    }

    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService) {
        this.userWorkgroupService = userWorkgroupService;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getUserWorkgroups() {
        List<WebUserWorkgroup> userworkgroupData = adminUserService.getUserWorkgroupsByUserId(webUserId);
        userworkgroups = new ArrayList<>();

        for (WebUserWorkgroup h : userworkgroupData) {
            userworkgroups.add(new UserWorkgroupViewData(h));
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_USER"})
    public List getAvailableWorkgroups() {
        List items = new ArrayList<>();
        try {
            items = adminUserService.getWorkgroups(this.webUserId);
        } catch (Exception ex) {
            handleException(ex);
        }
        return items;
    }

    public String checkUserWorkgroupAllowToDelete() {

        try {
            if (userWorkgroupService.getUserWorkgroup(userWorkgroupId) != null) {
                ActionResponse response = adminUserService.checkUserWorkgroupAllowToDelete(this.userWorkgroupId);
                setActionResponse(response);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_USER"})
    public String removeWebUserWorkgroupMapping() {

        try {
            if (userWorkgroupService.isUserWorkgroupExist(workgroupId, webUserId)) {
                ActionResponse response = adminUserService.removeWebUserWorkgroupMapping(this.workgroupId, this.webUserId);
                setActionResponse(response);
            } else {
                throw new Exception("Record was updated by another transaction/user, please try again.",
                        new StaleObjectStateException(WebUserWorkgroup.class.getSimpleName().concat("Version"), 0));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;

    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_USER"})
    public String addNewWebUserWorkgroupMapping() {

        try {
            if (!userWorkgroupService.isUserWorkgroupExist(workgroupId, webUserId)) {
                ActionResponse response = adminUserService.addNewWebUserWorkgroupMapping(this.workgroupId, this.webUserId);
                setActionResponse(response);
            } else {
                throw new Exception("Record was updated by another transaction/user, please try again.",
                                new StaleObjectStateException(WebUserWorkgroup.class.getSimpleName().concat("Version"), 0));
            }

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
