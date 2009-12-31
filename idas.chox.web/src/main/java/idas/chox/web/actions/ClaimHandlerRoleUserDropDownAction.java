package idas.chox.web.actions;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UserService;
import java.util.ArrayList;
import java.util.List;

public class ClaimHandlerRoleUserDropDownAction extends BaseAction {

    private List claimhandlers = null;
    private Integer workgroupId;
    private Integer insurerId;
    private UserService userService;
    private InsurerService insurerService;

    public Integer getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Integer workgroupId) {
        this.workgroupId = workgroupId;
    }

    public Integer getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
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

    public String ClaimSearch() throws Exception {

        claimhandlers = new ArrayList<IdLookupItem>();

        if (getIsInsurer()) {

            insurerId = getAuthenticatedUser().getInsurer().getId();
        }

        if (insurerId > 1) {

            Insurer insurer = insurerService.getInsurer(insurerId);
            List<WebUser> users = userService.getClaimHanldersByInsurerWorkgroup(insurerId, workgroupId, insurer.isWorkgroupEnable());

            List items = new ArrayList<IdLookupItem>();

            for (WebUser user : users) {
                items.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }

            claimhandlers = items;

        }

        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {

        claimhandlers = new ArrayList<IdLookupItem>();

        if (insurerId > 1) {

            Insurer insurer = insurerService.getInsurer(insurerId);

            List<WebUser> users = userService.getClaimHanldersByInsurerWorkgroup(insurerId, workgroupId, insurer.isWorkgroupEnable());
            List items = new ArrayList<IdLookupItem>();

            for (WebUser user : users) {
                items.add(new IdLookupItem(user.getId(), user.getDisplayName()));
            }

            claimhandlers = items;

        }

        return SUCCESS;
    }
}
