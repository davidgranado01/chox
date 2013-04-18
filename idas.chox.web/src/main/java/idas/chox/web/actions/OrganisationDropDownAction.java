package idas.chox.web.actions;

import idas.chox.core.services.LookupService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;

public class OrganisationDropDownAction extends BaseAction {

    private List organisationList = null;
    private String selectedOrganisationTypeId;
    private LookupService service;

    @Override
    public String execute() throws Exception {


        if (selectedOrganisationTypeId != null && !selectedOrganisationTypeId.equals("")) {
            if ((getIsCHO() && ! "3".equals(selectedOrganisationTypeId)) || (getIsInsurer() && ! "2".equals(selectedOrganisationTypeId))) {
                throw new AccessDeniedException("You do not have the correct access role to view the requested data. You will now be logged out.");
            }
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
        return selectedOrganisationTypeId;
    }

    public void setSelectedOrganisationTypeId(String SelectedOrganisationTypeId) {
        this.selectedOrganisationTypeId = SelectedOrganisationTypeId;
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
