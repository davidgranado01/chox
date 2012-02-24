package idas.chox.web.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Entity;
import idas.chox.core.services.ClaimService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.Map;
//import java.util.Set;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

public abstract class ClaimModelAction<T extends Entity> extends BaseAction implements ModelDriven<T>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimModelAction.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    public static final String READ_ONLY = "r";
    public static final String EDITABLE = "w";
    public static final String DECLINE = "decline";
    protected int claimId = 0;
    protected ClaimService claimService;
    protected BaseDataService baseDataService;
    protected ApplicationAccessibility applicationAccessibility;
    protected Claim claim;
    protected T model;
    // </editor-fold>
    private Map session;

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
            if (getSession().containsKey("claimDetailPageClaimId") && getSession().get("claimDetailPageClaimId") != null) {
                LOG.info("claim id is not provided and got claim id from session claim id is {}", (Integer) getSession().get("claimDetailPageClaimId"));
                claim = claimService.getClaim((Integer) getSession().get("claimDetailPageClaimId"));
                getSession().put("claimDetailPageClaimVersion", claim.getVersion());
            }
        } else {
            claim = claimService.getClaim(claimId);
        }
        if (claim == null) {
            throw new Exception("An attempt to retrieve claim by id failed due to invalid id provided.");
        }

        model = loadModel();
    }

    protected abstract T loadModel();

    @Override
    public String execute() {
        // Verify User has access to claim
        if ((getUserOrganisationType() == 3 && getUserOrganisationId() != claim.getChorganisation().getId().intValue())
                || (getUserOrganisationType() == 2 && getUserOrganisationId() != claim.getInsurer().getId().intValue())) {
            throw new AccessDeniedException("Illegal claim access detected.");
        }
        //Set roles = getAuthenticatedUser().getRoles();
        String tabName = getTabName();
        short accessRight = applicationAccessibility.checkTabAccessibility(tabName, super.getAuthenticatedUser(), claim);

        String result = accessRight > 1 ? EDITABLE : READ_ONLY;
        LOG.debug("Returning accessibility={} for tab.status={}", result, tabName + '.' + claim.getStatus());
        if (model != null) {
            LOG.debug("Settingt model version in session: {}={}", model.getClass().getName(), model.getVersion());
            session = ActionContext.getContext().getSession();
            session.put(model.getClass().getName(), model.getVersion());
            if (model instanceof Claim) {
                getSession().put("claimDetailPageClaimVersion", model.getVersion());
            } else {
                getSession().put("claimDetailPageClaimVersion", claim.getVersion());
            }
        }

        return result;
    }

    public String updateModel() {
        LOG.debug("Updating claim");
        try {
            checkVersion(model);
            this.claimService.updateClaim(claim);
            this.setActionResult("Your changes have been saved.");
            LOG.debug("claim is saved");
            // Now update the model version in the session
            claim = this.claimService.getClaim(claimId);
            session = ActionContext.getContext().getSession();
            session.put(model.getClass().getName(), model.getVersion());
            if (model instanceof Claim) {
                getSession().put("claimDetailPageClaimVersion", model.getVersion());
            } else {
                getSession().put("claimDetailPageClaimVersion", claim.getVersion());
            }
            LOG.debug("Model Version added to session: {}={}", model.getClass().getName(), model.getVersion());
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        LOG.debug("claim is saved and returning success");
        return SUCCESS;
    }

    public void updateSessionModel() {
        if (!(model.getVersion().equals((Integer) session.get(model.getClass().getName())))) {
            LOG.debug("Setting model version in session: {}={}", model.getClass().getName(), model.getVersion());
            session.put(model.getClass().getName(), model.getVersion());
            LOG.debug("Setting is done for model version in session: {}={}", model.getClass().getName(), model.getVersion());
        }
    }

    @Override
    public T getModel() {
        return model;
    }

    void checkVersion(T model) throws Exception {
        session = ActionContext.getContext().getSession();
        Integer sessionModelVersion = (Integer) session.get(model.getClass().getName());
        LOG.debug("Checking version with currentVersion={}, modelVersion={}", sessionModelVersion, model.getVersion());
        LOG.debug("Session model is: {}={}", model.getClass().getName(), sessionModelVersion);
        if (sessionModelVersion != null && model.getVersion() != null && !model.getVersion().equals(sessionModelVersion)) {
            StaleObjectStateException ex = new StaleObjectStateException(model.getClass().getName(), model.getId());
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
    // </editor-fold>
}
