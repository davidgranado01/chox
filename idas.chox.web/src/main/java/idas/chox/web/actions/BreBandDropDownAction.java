package idas.chox.web.actions;

import idas.chox.core.services.BreBandService;
import java.util.List;

public class BreBandDropDownAction extends BaseAction {

    private List breBands;
    private Integer insurerId;
    private BreBandService services;

    public void setBreBandService(BreBandService services) {
        this.services = services;
    }

    public Integer getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public List getBreBands() {
        return breBands;
    }

    @Override
    public String execute() throws Exception {
        
        try {
            
            this.breBands = services.getInsurerBreBandsByInsurer(this.insurerId);
            
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        
        return SUCCESS;
    }
}
