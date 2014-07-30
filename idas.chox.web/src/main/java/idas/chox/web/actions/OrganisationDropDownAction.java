package idas.chox.web.actions;

import idas.chox.core.services.LookupService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

public class OrganisationDropDownAction extends BaseAction {

    private List organisationList = new ArrayList();
    private String selectedOrganisationTypeId;
    private LookupService service;
    private static final Logger LOG = LoggerFactory.getLogger(OrganisationDropDownAction.class);
    
    @Override
    public String execute() throws Exception {


        if (selectedOrganisationTypeId != null && !selectedOrganisationTypeId.equals("")) {
            if ((getIsCHO() && ! "3".equals(selectedOrganisationTypeId)) || (getIsInsurer() && ! "2".equals(selectedOrganisationTypeId))) {
                LOG.warn("Provided organisationTypeId '{}' is not matching with the current user org id '{}'. Possible hack attempt!!!.", selectedOrganisationTypeId, getUserOrganisationType());
                throw new AccessDeniedException("You do not have the correct access role to view the requested data. You will now be logged out.");
            }
            getOrganisationList(getSelectedOrganisationTypeId());
            return SUCCESS;
        } else {
            LOG.warn("OrganisationTypeId is not present in the request. Returning empty organisationList.", selectedOrganisationTypeId);
            return SUCCESS;
        }
    }

    private void getOrganisationList(String id) {

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
