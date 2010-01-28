package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Entity;
import idas.chox.core.services.ClaimService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.Set;
import org.hibernate.StaleObjectStateException;


public abstract class ClaimModelAction<T extends Entity> extends BaseAction implements ModelDriven<T>, Preparable {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    public static final String READ_ONLY = "r";
    public static final String EDITABLE = "w";
    public static final String DECLINE = "decline";
    protected int claimId = 0;
    private Integer currentVersion;
    protected ClaimService claimService;
    protected BaseDataService baseDataService;
    protected ApplicationAccessibility applicationAccessibility;
    protected Claim claim;
    protected T model;
    // </editor-fold>

    abstract String getTabName();

    public Claim getClaim() {
        return claim;
    }

    public String getClaimStatus() {
        return claim.getStatus();
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public void prepare() throws Exception {
        
        claim = this.claimService.getClaim(claimId);

        if (claim == null) {
            throw new Exception("An attempt to retrieve claim by id failed due to invalid id provided.");
        }

        model = loadModel();

        checkVersion(model);
    }

    protected abstract T loadModel();

    @Override
    public String execute() {
        Set roles = getAuthenticatedUser().getRoles();
        String tabName = getTabName();
        short accessRight = applicationAccessibility.checkTabAccessibility(tabName, super.getAuthenticatedUser(), claim);

        String result = accessRight > 1 ? EDITABLE : READ_ONLY;

        return result;
    }

    public String updateModel() {

        try {
            this.claimService.updateClaim(claim);
            this.setActionResult("Your changes have been saved.");
        } catch (Exception ex) {
            handleException(ex);
        }

        return SUCCESS;
    }

    public T getModel() {
        return model;
    }

    private void checkVersion(T model) {
        if (currentVersion != null && !model.getVersion().equals(currentVersion)) {
            StaleObjectStateException ex = new StaleObjectStateException(model.getClass().getName(), model.getId());
            this.handleException(ex);
            throw ex;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Services">
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    /**
     * @param currentModelVersion the currentModelVersion to set
     */
    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }
    // </editor-fold>
}
