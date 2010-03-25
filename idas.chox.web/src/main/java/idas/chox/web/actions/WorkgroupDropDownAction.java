package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.LookupService;
import java.util.ArrayList;
import java.util.List;

public class WorkgroupDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(WorkgroupDropDownAction.class);

    private List<Workgroup> workgroups = null;
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
        LOG.debug("orgID set: {}", orgId);
        this.orgId = orgId;
    }

    public void setLookupService(LookupService service) {
        this.service = service;
    }

    public List getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List workgroups) {
        LOG.debug("Workgroups set: {}", workgroups.size());
        this.workgroups = workgroups;
    }

    public String getJsonData() {
        LOG.debug("Returning json data from workgroups: {}", workgroups);

        JSONArray jsonArray;
        try {
            List<LookupItem> luItems = new ArrayList<LookupItem>(workgroups.size());
            for (Workgroup workgroup : workgroups) {
                LOG.debug("Adding Workgroup to Lookup: {}, {}", workgroup.getId().toString(), workgroup.getName());
                luItems.add(new LookupItem(workgroup.getId().toString(), workgroup.getName()));
            }
            jsonArray = JSONArray.fromObject(luItems);
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return null;
        }
        LOG.debug("Returning json data: {}", jsonArray.toString());
        return "{totalCount:" + workgroups.size() + ",results:" + jsonArray.toString() + "}";
    }

    public String ClaimSearch() throws Exception {
        LOG.debug("ClaimSearchCombo action called.");
        workgroups = service.getWorkgroupsByInsurerId(getOrgId(), false);
        LOG.debug("Workgroups retrieved: {}", workgroups.size());
        return SUCCESS;
    }

    public String getInsurerWorkgroup() throws Exception {
//        workgroups = new ArrayList<LookupItem>();
        workgroups = service.getWorkgroupsByInsurerId(getOrgId(), true);
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        LOG.debug("execute called in WorkgroupSropDownAction.");
        workgroups = service.getWorkgroups(getAuthenticatedUser(), true);
        return SUCCESS;
    }
}
