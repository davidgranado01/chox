package chox.web.actions;

import chox.services.BreBandService;
import java.util.List;

public class BreBandDropDownAction extends BaseAction{

    private List breBands = null;
    private Integer orgId;
    private BreBandService services;

    public void setBreBandService(BreBandService services) {
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
        this.breBands = services.getInsurerBreBand(getOrgId());
        return SUCCESS;     
    }    
}
