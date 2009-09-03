package chox.web.data;

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
    
    @Override
    public String execute() throws Exception {
        
        PermissionedUser user = getAuthenticatedUser();
        
        workgroups = new ArrayList<LookupItem>();
        
        if(!user.getIsCHOXAdmin() && user.getIsINS() ){
            user = getAuthenticatedUser();
            workgroups = service.getAllWorkgroupsByInsurerId(user.getUser().getInsurer().getId());
        }else{
            workgroups = service.getAllWorkgroupsByInsurerId(getOrgId());
        }
        
        return SUCCESS;     
    }  
    
}
