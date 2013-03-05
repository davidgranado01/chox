package idas.chox.web.actions;

import idas.chox.core.services.WebUserUserRoleService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;

public class WebUserRoleDropDownAction extends BaseAction {

    private String selectedOrganisationTypeId;
    private List userroleList = null;
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
            getUserRoleList(getSelectedOrganisationTypeId());
            return SUCCESS;
        } else {
            return SUCCESS;
        }
    }

    private void getUserRoleList(String id) {
        this.userroleList = new ArrayList();
        this.userroleList = service.getWebUserRolesLookupItem(Integer.valueOf(id), getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
              getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), getInsurerIsSupervisorEnabled(), true);
    }

    public String getSelectedOrganisationTypeId() {
        return selectedOrganisationTypeId;
    }

    public void setSelectedOrganisationTypeId(String SelectedOrganisationTypeId) {
        this.selectedOrganisationTypeId = SelectedOrganisationTypeId;
    }

    public List getUserroleList() {
        return userroleList;
    }

    public void setUserroleList(List userroleList) {
        this.userroleList = userroleList;
    }
}

