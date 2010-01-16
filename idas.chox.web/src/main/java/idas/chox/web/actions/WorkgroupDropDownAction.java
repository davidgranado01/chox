package idas.chox.web.actions;

import idas.chox.core.model.LookupItem;
import idas.chox.core.services.LookupService;
import java.util.ArrayList;
import java.util.List;

public class WorkgroupDropDownAction extends BaseAction {

    private List workgroups = null;
    private Integer orgId;
    private LookupService service;

    public LookupService getService() {
        return service;
    }

    public void setService(LookupService service) {
        this.service = service;
    }

    public Integer getOrgId() {
        if (getIsInsurer()) {
            orgId = getAuthenticatedUser().getInsurer().getId();
        }
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public void setLookupService(LookupService service) {
        this.service = service;
    }

    public List getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List workgroups) {
        this.workgroups = workgroups;
    }

    public String ClaimSearch() throws Exception {
        workgroups = new ArrayList<LookupItem>();
        workgroups = service.getWorkgroupsByInsurerId(getOrgId(), false);
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        workgroups = service.getWorkgroups(getAuthenticatedUser(), true);
        return SUCCESS;
    }
}
