package chox.web.actions;

import chox.model.LookupItem;
import chox.services.LookupService;
import chox.web.security.PermissionedUser;
import java.util.ArrayList;
import java.util.List;

public class LineOfBusinessDropDownAction extends BaseAction{

    private List lineofbusinesses = null;
    private Integer orgId;
    private LookupService service;

    public void setLookupService(LookupService service) {
        this.service = service;
    }
    
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public List getLineofbusinesses() {
        return lineofbusinesses;
    }
    
    @Override
    public String execute() throws Exception {
        
        PermissionedUser user = getAuthenticatedUser();
        
        lineofbusinesses = new ArrayList<LookupItem>();
        
        if(!user.getIsCHOXAdmin() && user.getIsINS() ){
            user = getAuthenticatedUser();
            // lineofbusinesses = service.getLineOfBusinessesByInsurerId(user.getUser().getInsurer().getId());
            
        }else{
            // lineofbusinesses = service.getLineOfBusinessesByInsurerId(getOrgId());
        }
        
        return SUCCESS;     
    }    
}
