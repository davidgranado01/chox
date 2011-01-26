package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;

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
    private AuditTrailService auditTrailService;

    public Activity getModel() {
        return activity;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
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
    public String execute() {
        LOG.debug("execute");
        LOG.debug("Activity " + name + " class " + activity.getClass().getName());
        Map mp = ActionContext.getContext().getParameters();
        /*
        for (Iterator<String> it = mp.keySet().iterator(); it.hasNext();) {
        String key = it.next();
        try{
        if ( mp.get(key) instanceof String[] ){
        LOG.debug("key = " + key + " value []= "+((String[])mp.get(key))[0].toString());
        }else if ( mp.get(key)instanceof String){
        LOG.debug("key = " + key + " value = "+((String)mp.get(key)).toString());
        }
        }catch(Exception e){
        LOG.error(e.getMessage(),e);
        }
        }
         */
        if (activity != null) {
            try {
                LOG.debug("Executing ClaimActivity: claimId={}, currentVerion={}", id, currentVersion);
                if (paymentLogged == true) {
                    if (!setClaimStatusPaymentLogged()) {
                        LOG.debug("Payment Logged is not setup in the claim ");

                        return ERROR;
                    }
                }

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

    public boolean setClaimStatusPaymentLogged() {

        try {
            if (!claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
                if (!claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                        claim.setPreviousStatus(claim.getStatus());
                        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
                        if (auditTrailService.logAuditLogForce(claim.getStatus(), claim.getPreviousStatus(), claim)) {
                            LOG.debug(" AWAITING_INVOICE_PAYMENT : AuditTrail has been updated");
                        } else {
                            LOG.debug("AWAITING_INVOICE_PAYMENT : AuditTrail has not been updated");
                        }
                        this.claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
                    }

                claim.setPreviousStatus(claim.getStatus());
                claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
                if (auditTrailService.logAuditLogForce(claim.getStatus(), claim.getPreviousStatus(), claim)) {
                    LOG.debug("  AuditTrail has been updated");
                } else {
                    LOG.debug(" AuditTrail has not been updated");
                }

                this.claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
                LOG.debug("Payment Logged is setup in the claim ");
                return true;
            } else {
                return true;
            }
        } catch (Exception ex) {
            handleException(ex);
            return false;
        }

    }
}
