package idas.chox.web.actions;

import idas.chox.core.services.WebUserUserRoleService;
import java.util.ArrayList;
import java.util.List;

public class WebUserroleDropDownAction extends BaseAction {

    private String SelectedOrganisationTypeId;
    private List userroleList = null;
    private WebUserUserRoleService service;

    public void setWebUserUserRoleService(WebUserUserRoleService service) {
        this.service = service;
    }

    @Override
    public String execute() throws Exception {

        if (getSelectedOrganisationTypeId() != null && !getSelectedOrganisationTypeId().equals("")) {
            getUserroleList(getSelectedOrganisationTypeId());
            return SUCCESS;
        } else {
            return SUCCESS;
        }
    }

    private void getUserroleList(String id) {
        this.userroleList = new ArrayList();
        this.userroleList = service.getWebUserrolesLookupItem(Integer.valueOf(id));
    }

    public String getSelectedOrganisationTypeId() {
        return SelectedOrganisationTypeId;
    }

    public void setSelectedOrganisationTypeId(String SelectedOrganisationTypeId) {
        this.SelectedOrganisationTypeId = SelectedOrganisationTypeId;
    }

    public List getUserroleList() {
        return userroleList;
    }

    public void setUserroleList(List userroleList) {
        this.userroleList = userroleList;
    }
}

