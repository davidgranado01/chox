package chox.web.dropdown;

import chox.Util.RoleHelper;
import chox.model.LookupItem;
import chox.services.LookupService;
import chox.web.actions.BaseAction;
import chox.web.security.PermissionedUser;
import java.util.ArrayList;
import java.util.List;

public class WorkgroupDropDownAction extends BaseAction{
    
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
        if(getIsInsurer()){
            orgId = getAuthenticatedUser().getUser().getInsurer().getId();
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
        PermissionedUser user = getAuthenticatedUser();
        workgroups = service.getWorkgroups(user.getUser(), true);
        return SUCCESS;
    }  
}