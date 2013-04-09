package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

import idas.chox.core.model.LookupItem;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.LookupService;

public class WorkgroupDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(WorkgroupDropDownAction.class);

    private List<Workgroup> workgroups = new ArrayList<Workgroup>();
    private Set<Integer> orgId = new HashSet<Integer>();
    private LookupService service;
    private int claimId;

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public LookupService getService() {
        return service;
    }

    public void setService(LookupService service) {
        this.service = service;
    }

    public Set<Integer> getOrgId() {
        if (getIsInsurer()) {
            orgId.add(getAuthenticatedUser().getInsurer().getId());
        }
        return orgId;
    }

    public void setOrgId(Set<Integer> orgId) {
        if (orgId.contains(null)) { 
            this.orgId = null;
        }
        else {
            this.orgId = orgId;
        }
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

    public String getInsurerWorkgroup() throws Exception {
        LOG.debug("ClaimSearchCombo action called.");
        if (getAuthenticatedUser().isCHOXAdmin() && claimId != 0) {
            workgroups = service.getWorkgroupsByClaimId(claimId, true);            
        } else if (getOrgId() != null) { 
            for (Integer insId : getOrgId()) {
               workgroups.addAll(service.getWorkgroupsByInsurerId(insId, true)); 
            }
        }
        return SUCCESS;
    }

    public String getAllInsurerWorkgroups() throws Exception {
        LOG.debug("ClaimSearchCombo action called.");
        if (getAuthenticatedUser().isCHOXAdmin() && claimId != 0) { 
            workgroups = service.getWorkgroupsByClaimId(claimId, true);            
        } else if (getOrgId() != null) {
            for (Integer insId : getOrgId()) {
               workgroups.addAll(service.getWorkgroupsByInsurerId(insId, false)); 
            }
        } 
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        LOG.debug("execute called in WorkgroupDropDownAction.");
        if (getAuthenticatedUser().isCHOXAdmin()) {
            // Select workgroups from the insurer of the claim we are viewing
            LOG.debug("Need to get workgroups for current claim id={}.", claimId);
            workgroups = service.getWorkgroupsByClaimId(claimId, true);
        }
        else {
            workgroups = service.getWorkgroups(getAuthenticatedUser(), true);
        }
        return SUCCESS;
    }
}
