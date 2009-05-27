package chox.web.actions;

import chox.services.ChoBandService;
import chox.services.LookupService;
import java.util.List;

public class ChoBandDropDownAction extends BaseAction{

    private List breBands = null;
    private Integer orgId;
    private ChoBandService services;

    public void setChoBandService(ChoBandService services) {
        this.services = services;
    }
    
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public List getBreBands() {
        return breBands;
    }
    
    @Override
    public String execute() throws Exception {
        this.breBands = services.getInsurerChoBand(getOrgId());
        return SUCCESS;
        
    }    
}
