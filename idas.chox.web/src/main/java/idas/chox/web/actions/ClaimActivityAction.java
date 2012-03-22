
package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import org.springframework.security.access.AccessDeniedException;
import net.sf.json.JSONObject;

public class ClaimActivityAction extends BaseAction implements ModelDriven<Activity>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimActivityAction.class);
    private ActivityFactory activityFactory;
    private ClaimService claimService;
    private BreBandService breBandService;
    private Activity activity;
    private Claim claim;
    private String name;
    private int currentVersion;
    private List<Integer> selectedClaimIdList;
    private Boolean paymentLogged = false;
    private String jsonData;
    private boolean showMessage = false;
    private String message = null;
    
    @Override
    public Activity getModel() {
        return activity;
    }

    @Override
    public boolean getInsurerIsWorkgroupEnabled() {
        return claim.getInsurer().isWorkgroupEnable();
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
        if (message != null && !message.isEmpty())
            showMessage = true;
    }

    public boolean isShowMessage() {
        return showMessage;
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
    
    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getJsonData() {
        return jsonData;
    }

    @Override
    public void prepare() throws Exception {

        if (getSession().containsKey("claimDetailPageClaimId")) {
            Integer claimId = (Integer) getSession().get("claimDetailPageClaimId");
            LOG.debug("Getting claim from session claimId={}", claimId);
            claim = claimService.getClaim(claimId);
            setCurrentVersion((Integer) getSession().get("claimDetailPageClaimVersion"));
        } else if (selectedClaimIdList == null || selectedClaimIdList.isEmpty()) {
            LOG.error("No claimId in session");
        }

        // Make sure we have a BRE Band (for non-batch requests)
        if ((selectedClaimIdList == null ||  selectedClaimIdList.isEmpty())&& claim != null && claim.getBreBand() == null) {
            BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
        }

        // Skip version checking if we are processing multiple claims
        if (selectedClaimIdList == null || selectedClaimIdList.isEmpty())
            checkVersion();
        LOG.debug("Claim Activity Action " + name);
        activity = activityFactory.getActivity(name);

    }

    
    public String processMultipleClaims() {
        LOG.debug("processMultipleClaims");
        if (activity != null && selectedClaimIdList.size() > 0) {
            try {
                for (Integer selectedClaimId : selectedClaimIdList) {
                    LOG.debug("Processing claim with id={} and activity={}", selectedClaimId, activity.getClass());
                    claim = claimService.getClaim(selectedClaimId);

                    // Make sure we have a BRE Band
                    if (claim.getBreBand() == null) {
                        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                        claim.setBreBand(choBand);
                    }

                    if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                            || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                        throw new AccessDeniedException("Attempt to access a claim that you do not own.");
                    }
                    activity.process(claim);
                }
            } catch(AccessDeniedException ex) {
                throw(ex);
            } catch (Exception ex) {
                LOG.error("Error processing multiple claims: {}", ex);
                handleException(ex);
                return ERROR;
            }
            return SUCCESS;
        }

        return ERROR;
    }

    
    @Override
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String execute() {
        JSONObject jsonObject = new JSONObject();
        LOG.debug("Activity " + name + " class " + activity.getClass().getName());
        if (activity != null) {
            try {
                LOG.debug("Executing ClaimActivity: claimId={}, currentVerion={}", claim.getId(), currentVersion);
                /*
                 * If moving to payment received from a status that is not 'PaymentLogged',
                 * then first move to payment logged status
                 * (Note this flag is set from the more actions drop-down in p_update_payment_received.jsp,
                 * this value is hidden and got it from claim action)
                 */
                if (paymentLogged == true) {
                    LOG.debug("Moving claim to InvoicePaymentLogged (before setting to payment received).");
                    activityFactory.getActivity("moveToInvoicePaymentLogged").process(claim);
                }
                activity.process(claim);
                setMessage(activity.getMessage());
            } catch(AccessDeniedException ex) {
                throw(ex);
            } catch (Exception ex) {
                LOG.warn("Error processing claim activity: {}",ex.getMessage());
                jsonObject.put("success", Boolean.FALSE);
                jsonObject.put("errors", ex.getMessage());
                setJsonData(jsonObject.toString());
                handleException(ex);
                return ERROR;
            }
            LOG.debug("claim activity returning success");
            jsonObject.put("success", Boolean.TRUE);
            jsonObject.put("message", "claim processed successfully.");
            setJsonData(jsonObject.toString());
            
            return SUCCESS;
        } else {
            LOG.warn("Cannot process activity: activity is empty (null)");
            jsonObject.put("success", Boolean.FALSE);
            jsonObject.put("errors", "Sorry - No activity implemented for the requested activity action.");
            setJsonData(jsonObject.toString());
            
        }

        return ERROR;
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

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    // </editor-fold>

    private void checkVersion() {
        if (claim.getVersion() == null || claim.getVersion().intValue() != currentVersion) {
            LOG.warn("Claim version mismatch: currentVersion={}, claimVersion={}", currentVersion, claim.getVersion());
            StaleObjectStateException ex = new StaleObjectStateException(claim.getClass().getName(), claim.getId());
            this.handleException(ex);
            throw ex;
        }
    }

    public Integer getId() {
        if (claim != null)
            return claim.getId();
        
        return null;
    }
    
    public Integer getVersion() {
        LOG.debug("getVersion returning claimVersion={} (currentVersion={})", claim.getVersion(), currentVersion);
        return claim.getVersion();
    }

    public void setCurrentVersion(int currentVersion) {
        LOG.debug("currentVersion set: {} (claimId={})", currentVersion, claim.getId());
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
        } else {
            LOG.debug(" ClaimActivityAction validation is not done as claim is null");
        }
    }
}
