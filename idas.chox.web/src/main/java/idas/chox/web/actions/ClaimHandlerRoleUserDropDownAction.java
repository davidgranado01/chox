package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UserService;


public class ClaimHandlerRoleUserDropDownAction extends BaseAction {
    private List<IdLookupItem> claimhandlers = null;
    private Set<Integer> workgroupId = new HashSet<>();
    private Set<Integer> insurerId = new HashSet<>();
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
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(claimhandlers);
        } catch (JsonProcessingException ex) {
        }
        return "{totalCount:" + claimhandlers.size() + ",results:" + jsonString + "}";
    }

    @Override
    public String execute() throws Exception {

        claimhandlers = new ArrayList<>();

        if (getIsInsurer()) {
            insurerId.clear();
            insurerId.add(getAuthenticatedUser().getInsurer().getId());
        }

        if (insurerId != null) {
            List<WebUser> users = new ArrayList<>();
            for (Integer insId : insurerId) {
                Insurer insurer = insurerService.getInsurer(insId);
                if (insurer != null) {
                    users.addAll(userService.getActiveClaimHandlersByInsurerWorkgroup(insId, workgroupId, insurer.isWorkgroupEnable()));
                }
            }
            for (WebUser user : users) {
                claimhandlers.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }
        }

        return SUCCESS;
    }

    
    public String getAllClaimHandlers() throws Exception {

        claimhandlers = new ArrayList<>();

        if (getIsInsurer()) {
            insurerId.clear();
            insurerId.add(getAuthenticatedUser().getInsurer().getId());
        }

        if (insurerId != null) {
            List<WebUser> users = new ArrayList<>();
            for (Integer insId : insurerId) {
                Insurer insurer = insurerService.getInsurer(insId);
                if (insurer != null) {
                    users.addAll(userService.getAllClaimHandlersByInsurerWorkgroup(insId, workgroupId, insurer.isWorkgroupEnable()));
                }
            }
            for (WebUser user : users) {
                claimhandlers.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }
        }

        return SUCCESS;
    }
}
