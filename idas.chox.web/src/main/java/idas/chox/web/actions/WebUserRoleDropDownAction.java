package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.services.WebUserUserRoleService;

public class WebUserRoleDropDownAction extends BaseAction {

    private String selectedOrganisationTypeId;
    private List userRoleList = null;
    private WebUserUserRoleService service;

    public void setWebUserUserRoleService(WebUserUserRoleService service) {
        this.service = service;
    }

    @Override
    public String execute() throws Exception {

        if ((getIsCHO() && ! "3".equals(selectedOrganisationTypeId)) || (getIsInsurer() && ! "2".equals(selectedOrganisationTypeId))) {
            throw new AccessDeniedException("You do not have the correct access role to view the requested data. You will now be logged out.");
        }

        if (getSelectedOrganisationTypeId() != null && !getSelectedOrganisationTypeId().equals("")) {
            setupUserRoleList(getSelectedOrganisationTypeId());
            return SUCCESS;
        } else {
            return SUCCESS;
        }
    }

    private void setupUserRoleList(String id) {
        this.userRoleList = new ArrayList();
        this.userRoleList = service.getWebUserRolesLookupItem(Integer.valueOf(id), getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
              getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), getIsSupervisorEnabled(), true);
    }

    public String getSelectedOrganisationTypeId() {
        return selectedOrganisationTypeId;
    }

    public void setSelectedOrganisationTypeId(String selectedOrganisationTypeId) {
        this.selectedOrganisationTypeId = selectedOrganisationTypeId;
    }

    public List getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List userRoleList) {
        this.userRoleList = userRoleList;
    }
}

