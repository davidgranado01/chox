package idas.chox.web.actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Entity;
import idas.chox.core.services.ClaimService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.security.ApplicationAccessibility;


public abstract class ClaimModelAction<T extends Entity> extends BaseAction implements ModelDriven<T>, Preparable {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    public static final String READ_ONLY = "r";
    public static final String EDITABLE = "w";
    public static final String DECLINE = "decline";
    private static final Logger LOG = LoggerFactory.getLogger(ClaimModelAction.class);
    protected int claimId = 0;
    protected ClaimService claimService;
    protected BaseDataService baseDataService;
    protected Claim claim;
    protected T model;
    private ApplicationAccessibility applicationAccessibility;
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

    @Override
    public void prepare() throws Exception {
        LOG.debug("Preparing...");
        if (claimId <= 0) {
            if (getModelIdFromSession(Claim.class) != null) {
                claim = claimService.getClaim(getModelIdFromSession(Claim.class));
            }
        } else {
            claim = claimService.getClaim(claimId);
        }
        if (claim == null) {
            throw new Exception("An attempt to retrieve claim by id failed due to invalid id provided.");
        }
        model = loadModel();
        addModelToSession(Arrays.asList(claim,model));
    }

    protected abstract T loadModel();

    @Override
    public String execute() {
        // Verify User has access to claim
        if ((getUserOrganisationType() == 3 && getUserOrganisationId() != claim.getChorganisation().getId().intValue())
                || (getUserOrganisationType() == 2 && getUserOrganisationId() != claim.getInsurer().getId().intValue())) {
            throw new AccessDeniedException("Illegal claim access detected.");
        }
        String tabName = getTabName();
        Short accessRight = applicationAccessibility.checkTabAccessibility(tabName, super.getAuthenticatedUser(), claim);

        String result = accessRight > 1 ? EDITABLE : READ_ONLY;
        LOG.debug("Returning accessibility={} for tab '{}' in status={}", new Object[]{result, tabName, claim.getStatus()});
        return result;
    }

    public String updateModel() {
        LOG.debug("Updating claim");
        try {
            checkVersion(Arrays.asList(claim,model));
            this.claimService.updateClaim(claim);
            this.setActionResult("Your changes have been saved.");
            updateModelInSession(Arrays.asList(claim,model));
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        LOG.debug("claim is saved and returning success");
        return SUCCESS;
    }

    @Override
    public T getModel() {
        return model;
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
    // </editor-fold>
}
