package idas.chox.web.actions;

import idas.chox.core.services.LookupService;
import java.util.ArrayList;
import java.util.List;

public class OrganisationDropDownAction extends BaseAction {

    private List organisationList = null;
    private String SelectedOrganisationTypeId;
    private LookupService service;

    @Override
    public String execute() throws Exception {

        if (getSelectedOrganisationTypeId() != null && !getSelectedOrganisationTypeId().equals("")) {
            getOrganisationList(getSelectedOrganisationTypeId());
            return SUCCESS;
        } else {
            return SUCCESS;
        }
    }

    private void getOrganisationList(String id) {

        this.organisationList = new ArrayList();

        // 2: INSURER
        // 3: CHORGANISATION

        if (id.equalsIgnoreCase("2")) {
            this.organisationList = service.getAllInsurers();
        } else if (id.equalsIgnoreCase("3")) {
            this.organisationList = service.getAllSuppliers();
        }

    }

    public String getSelectedOrganisationTypeId() {
        return SelectedOrganisationTypeId;
    }

    public void setSelectedOrganisationTypeId(String SelectedOrganisationTypeId) {
        this.SelectedOrganisationTypeId = SelectedOrganisationTypeId;
    }

    public List getOrganisationList() {
        return organisationList;
    }

    public void setOrganisationList(List organisationList) {
        this.organisationList = organisationList;
    }

    public void setLookupService(LookupService service) {
        this.service = service;
    }
}
