package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;

import static com.opensymphony.xwork2.Action.SUCCESS;

import net.sf.json.JSONArray;

import idas.chox.core.model.Claim;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.WorkgroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkgroupDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(WorkgroupDropDownAction.class);

    private List<Workgroup> workgroups = new ArrayList<Workgroup>();
    private Set<Integer> orgId = new HashSet<Integer>();
    private LookupService service;
    private int claimId;
    private WorkgroupService workgroupService;
    private ClaimService claimService;
    private int insurerId = -1;

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

    // Get the active workgroups + the current claims in-active workgroup.
    public String getInsurerWorkgroupIncludsClaimsInactiveWG() throws Exception {
        LOG.debug("ClaimSearchCombo action called.");
        if (getAuthenticatedUser().isCHOXAdmin() && claimId != 0) {
            workgroups = service.getWorkgroupsByClaimId(claimId, true);
            addCurrentClaimInactiveWorkgroup();
        } else if (getOrgId() != null) {
            for (Integer insId : getOrgId()) {
                if (insId > 0) {
                    workgroups.addAll(service.getWorkgroupsByInsurerId(insId, true));
                    addCurrentClaimInactiveWorkgroup();
                }
            }
        }
        return SUCCESS;
    }
    
    // Get all the workgroups(including in-active workgroup)
    public String getAllInsurerWorkgroups() throws Exception {
        LOG.debug("ClaimSearchCombo action called.");
        if (getAuthenticatedUser().isCHOXAdmin() && claimId != 0) { 
            workgroups = service.getWorkgroupsByClaimId(claimId, false);            
        } else if (getOrgId() != null) {
            workgroups.clear();
            for (Integer insId : getOrgId()) {
               if (insId > 0) {
                   workgroups.addAll(service.getWorkgroupsByInsurerId(insId, false));
               }
            }
        } 
        return SUCCESS;
    }
    
    public String getAvailableAutoRoutingWorkgroups() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }
        try {
            workgroups = workgroupService.getAvailableAutoRoutingWorkgroupsByInsurer(insurerId, true);
        } catch (Exception ex) {
            LOG.error("Exception occurred while getting availableWorkgroups: ", ex);
        }
        return SUCCESS;
    }

    /* Get the active workgroups + the current claims in-active workgroup.
       Only return assigned workgroup to the current user if the user role is workgroup related. */
    @Override
    public String execute() throws Exception {
        LOG.debug("execute called in WorkgroupDropDownAction.");
        if (getAuthenticatedUser().isCHOXAdmin()) {
            // Select workgroups from the insurer of the claim we are viewing
            LOG.debug("Need to get workgroups for current claim id={}.", claimId);
            workgroups = service.getWorkgroupsByClaimId(claimId, true);
            addCurrentClaimInactiveWorkgroup();
        } else {
            workgroups = service.getWorkgroups(getAuthenticatedUser(), true);
            addCurrentClaimInactiveWorkgroup();
        }
        return SUCCESS;
    }
    
    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }
    
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    private void addCurrentClaimInactiveWorkgroup() {
        Workgroup inActiveWorkgroup = getClaimsInactiveWorkgroup();
        if (inActiveWorkgroup != null) {
            workgroups.add(inActiveWorkgroup);
        }
    }

    // If the claim is blongs to in-active workgroup then return that workgroup.
    private Workgroup getClaimsInactiveWorkgroup() {
        if (claimId > 0) {
            Claim claim = claimService.getClaim(claimId);
            if (claim != null && claim.getWorkgroup() != null && !claim.getWorkgroup().isStatus()) {
                return claim.getWorkgroup();
            }
        }
        return null;
    }
}
