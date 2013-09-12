package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimHandlerRoleUserDropDownAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimHandlerRoleUserDropDownAction.class);

    private List<IdLookupItem> claimhandlers = null;
    private Set<Integer> workgroupId = new HashSet<Integer>();
    private Set<Integer> insurerId = new HashSet<Integer>();
    private UserService userService;
    private InsurerService insurerService;

    public Set<Integer> getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Set<Integer> workgroupIds) {
        if (workgroupIds.contains(null))
            this.workgroupId.add(-1);
        else
            this.workgroupId = workgroupIds;
    }

    public Set<Integer> getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Set<Integer> insurerId) {
        if (insurerId.contains(null))
            this.insurerId.add(-1);
        else
            this.insurerId = insurerId;
    }
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public List getClaimhandlers() {
        return claimhandlers;
    }

    public void setClaimhandlers(List claimhandlers) {
        this.claimhandlers = claimhandlers;
    }

    public String getJsonData() {
        LOG.debug("Returning json data from claimhandlers: {}", claimhandlers);

        JSONArray jsonArray;
        try {
            jsonArray = JSONArray.fromObject(claimhandlers);
        } catch (Exception ex) {
            LOG.error("Exception creating jsonArray: {}", ex.getMessage());
            return null;
        }
        LOG.debug("Returning json data: {}", jsonArray.toString());
        return "{totalCount:" + claimhandlers.size() + ",results:" + jsonArray.toString() + "}";
    }

    @Override
    public String execute() throws Exception {

        claimhandlers = new ArrayList<IdLookupItem>();

        if (getIsInsurer()) {
            insurerId.clear();
            insurerId.add(getAuthenticatedUser().getInsurer().getId());
        }

        if (insurerId != null) {
            List<WebUser> users = new ArrayList<WebUser>();
            for (Integer insId : insurerId) {
                Insurer insurer = insurerService.getInsurer(insId);
                if (insurer != null) {
                    if (workgroupId != null) {
                        for (Integer id : this.workgroupId) {
                            users.addAll(userService.getActiveClaimHandlersByInsurerWorkgroup(insId, id, insurer.isWorkgroupEnable()));
                        }
                    }
                }
            }
            for (WebUser user : users) {
                claimhandlers.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }
        }

        return SUCCESS;
    }

    
    public String getAllClaimHandlers() throws Exception {

        claimhandlers = new ArrayList<IdLookupItem>();

        if (getIsInsurer()) {
            insurerId.clear();
            insurerId.add(getAuthenticatedUser().getInsurer().getId());
        }

        if (insurerId != null) {
            List<WebUser> users = new ArrayList<WebUser>();
            for (Integer insId : insurerId) {
                Insurer insurer = insurerService.getInsurer(insId);
                if (insurer != null) {
                    if (workgroupId != null) {
                        for (Integer id : this.workgroupId) {
                            users.addAll(userService.getAllClaimHandlersByInsurerWorkgroup(insId, id, insurer.isWorkgroupEnable()));
                        }
                    }
                }
            }
            for (WebUser user : users) {
                claimhandlers.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }
        }

        return SUCCESS;
    }
}
