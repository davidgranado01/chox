package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.services.ClaimService;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import org.springframework.security.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ClaimActivityAction extends BaseAction implements ModelDriven<Activity>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimActivityAction.class);
    private ActivityFactory activityFactory;
    private ClaimService claimService;
    private Activity activity;
    private Claim claim;
    private String name;
    private int id;
    private Integer currentVersion;
    private List<Integer> selectedClaimIdList;
    private Boolean paymentLogged = false;

    @Override
    public Activity getModel() {
        return activity;
    }

    @Override
    public boolean getInsurerIsWorkgroupEnabled() {
        return claim.getInsurer().isWorkgroupEnable();
    }

    public void setPaymentLogged(Boolean paymentReceived) {
        this.paymentLogged = paymentReceived;
    }

    @Override
    public boolean getInsurerIsClaimOwnershipEnabled() {
        return claim.getInsurer().isClaimOwnershipEnable();
    }

    @Override
    public boolean getInsurerIsFnolEnabled() {
        return claim.getInsurer().isFnolEnable();
    }

    @Override
    public boolean getInsurerIsEngineersEnabled() {
        return claim.getInsurer().isEngineersEnable();
    }

    @Override
    public void prepare() throws Exception {

        if (id > 0) {
            claim = claimService.getClaim(id);
            this.setCurrentVersion(claim.getVersion());
            checkVersion();
        }
        LOG.debug("Claim Activity Action " + name);
        activity = activityFactory.getActivity(name);

    }

    public String processMultipleClaims() {
        LOG.debug("processMultipleClaims");
        if (activity != null && selectedClaimIdList.size() > 0) {
            try {

                for (Integer selectedClaimId : selectedClaimIdList) {

                    claim = claimService.getClaim(selectedClaimId);
//                    this.setCurrentVersion(claim.getVersion());
                    checkVersion();
                    if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                            || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                        throw new AccessDeniedException("Attempt to access a claim that you do not own.");
                    }
                    activity.process(claim);
                }

            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                handleException(ex);
                return ERROR;
            }
            return SUCCESS;
        }

        return ERROR;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String execute() {
        LOG.debug("Activity " + name + " class " + activity.getClass().getName());
        if (activity != null) {
            try {
                LOG.debug("Executing ClaimActivity: claimId={}, currentVerion={}", id, currentVersion);
                /*
                 * If moving to payment received from a status that is not 'PaymentLogged',
                 * then first move to payment logged status
                 * (Note this flag is set from the more actions drop-down in p_update_payment_received.jsp , this value is hidden and got it from claim action)
                 */
                if (paymentLogged == true) {
                    activityFactory.getActivity("moveToInvoicePaymentLogged").process(claim);
                    LOG.info("moving claim to payment logged (before setting to payment received).");
                }
//                checkVersion();
                activity.process(claim);
            } catch (Exception ex) {
                handleException(ex);
                return ERROR;
            }
            LOG.debug("claim activity returning success");
            return SUCCESS;
        } else {
            LOG.debug("activity is null");
        }

        return ERROR;
    }

    // <editor-fold defaultstate="collapsed" desc="Parameters">
    public void setId(int id) {
        LOG.debug("claimId set: {} (currentVersion={})", id, currentVersion);
        if (claim != null && claim.getVersion() != currentVersion) {
            LOG.debug("currentVersion different from claim.version when setting id - updating to {}", claim.getVersion());
            this.setCurrentVersion(claim.getVersion());
        }
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Services">
    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    // </editor-fold>

    private void checkVersion() {
        if (currentVersion != null && !claim.getVersion().equals(currentVersion)) {
            LOG.warn("Claim version mismatch: currentVersion={}, claimVersion={}", currentVersion, claim.getVersion());
            StaleObjectStateException ex = new StaleObjectStateException(claim.getClass().getName(), claim.getId());
            this.handleException(ex);
            throw ex;
        }
    }

    public Integer getVersion() {
        LOG.debug("getVersion returning claimVersion={} (currentVersion={})", claim.getVersion(), currentVersion);
        return claim.getVersion();
    }

    public void setCurrentVersion(Integer currentVersion) {
        LOG.debug("currentVersion set: {} (claimId={})", currentVersion, id);
        this.currentVersion = currentVersion;
    }

    public void setSelectedClaimIds(String ids) {
        String[] list = ids.split(",");
        selectedClaimIdList = new ArrayList<Integer>();
        for (String s : list) {
            Integer selectedId = Integer.parseInt(s.trim());
            selectedClaimIdList.add(selectedId);
        }
        LOG.debug("selectedClaimIdList set: '{}'", ids);
    }

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("ClaimActivityAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("ClaimActivityAction validate success");
        }
        else {
            LOG.debug(" ClaimActivityAction validation is not done as claim is null");
        }
    }
}
