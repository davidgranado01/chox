package idas.chox.web.actions;

import idas.chox.core.services.BreBandService;
import java.util.List;
import org.springframework.security.AccessDeniedException;

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
        if ((getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId()) || getUserOrganisationType() == 3) {
            throw new AccessDeniedException("Illegal access detected.");
        }
        
        try {
            
            this.breBands = services.getInsurerBreBandsByInsurer(this.insurerId);
            
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        
        return SUCCESS;
    }
}
