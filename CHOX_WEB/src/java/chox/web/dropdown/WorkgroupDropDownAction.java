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

    public Integer getOrgId() {
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

        PermissionedUser user = getAuthenticatedUser();

        workgroups = new ArrayList<LookupItem>();

        if(RoleHelper.isCreditHireUser(user.getUser())){
            workgroups = service.getWorkgroupsByInsurerId(getOrgId(), false);
        }else{
            workgroups = service.getWorkgroups(user.getUser(), false);
        }
        
        return SUCCESS;
    }
    
    @Override
    public String execute() throws Exception {
        
        PermissionedUser user = getAuthenticatedUser();
        workgroups = service.getWorkgroups(user.getUser(), true);
        return SUCCESS;     
    }  
    
}
