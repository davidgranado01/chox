package chox.web.dropdown;

import chox.model.IdLookupItem;
import chox.model.Insurer;
import chox.model.WebUser;
import chox.services.UserService;
import chox.services.InsurerService;
import chox.web.actions.BaseAction;
import java.util.ArrayList;
import java.util.List;

public class ClaimHandlerRoleUserDropDownAction extends BaseAction{

    private List claimhandlers = null;
    private Integer workgroupId;
    private Integer insurerId;
    private UserService service;
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

    public void setUserService(UserService service) {
        this.service = service;
    }

    public void setInsurerService(InsurerService insurerService){
        this.insurerService = insurerService;
    }

    public List getClaimhandlers() {
        return claimhandlers;
    }

    public void setClaimhandlers(List claimhandlers) {
        this.claimhandlers = claimhandlers;
    }
   
    @Override
    public String execute() throws Exception {

        Insurer insurer = insurerService.getObject(insurerId);
        
        List<WebUser> users = service.getClaimHanldersByInsurerWorkgroup(insurerId, workgroupId, insurer.isWorkgroupEnable());
        List items = new ArrayList<IdLookupItem>();

        for(WebUser user:users){
            items.add(new IdLookupItem(user.getId(), user.getDisplayName()));
        }

        claimhandlers = items;
        
        return SUCCESS;
    }

}
