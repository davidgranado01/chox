package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.security.access.AccessDeniedException;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONObject;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.workflow.Activity;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.web.ChoxEvent;

public class ClaimActivityAction extends BaseAction implements ModelDriven<Activity>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimActivityAction.class);
    private ActivityFactory activityFactory;
    private ClaimService claimService;
    private BreBandService breBandService;
    private Activity activity;
    private Claim claim;
    private String name;
    private List<Integer> selectedClaimIdList;
    private String jsonData;
    private boolean showMessage = false;
    private String message = null;
    private ApplicationAccessibility applicationAccessibility;
    private ChoxEvent choxEventService;
    
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
        if (message != null && !message.isEmpty()) {
            showMessage = true;
        }
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setChoxEventService(ChoxEvent choxEventService) {
        this.choxEventService = choxEventService;
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

        if (getModelIdFromSession(Claim.class) != null) {
            claim = claimService.getClaim(getModelIdFromSession(Claim.class));
            addModelToSession(Arrays.asList(claim));
        } else if (selectedClaimIdList == null || selectedClaimIdList.isEmpty()) {
            LOG.error("No claimId in session");
        }

        // Make sure we have a BRE Band (for non-batch requests)
        if ((selectedClaimIdList == null || selectedClaimIdList.isEmpty()) && claim != null && claim.getBreBand() == null) {
            BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
        }

        LOG.debug("Claim Activity Action " + name);
        activity = activityFactory.getActivity(name);

    }

    
    public String processMultipleClaims() {
        LOG.debug("processMultipleClaims");
        if (activity != null && selectedClaimIdList.size() > 0) {
            boolean updatedByAnotherTransaction = false;
            StringBuilder actionError = new StringBuilder();
            actionError.append("Following claims were not updated as they were updated by another transaction/user:<br />");
            String activityName = AopUtils.getTargetClass(activity).getSimpleName();
            try {
                for (Integer selectedClaimId : selectedClaimIdList) {
                    LOG.debug("Processing claim with id={} and activity={}", selectedClaimId, activity.getClass());
                    claim = claimService.getClaim(selectedClaimId);

                    if (applicationAccessibility.checkActivityAccessibility(activityName, getAuthenticatedUser(), claim) < 1) {
                        LOG.warn("No access to batchUpdate activity '{}' for claim '{}' of type {} in status '{}'",
                                new Object[]{activityName, claim.getChoReference(), claim.getClaimType().name(), claim.getStatus()});
                        updatedByAnotherTransaction = true;
                        actionError.append(claim.getChoReference()).append("<br />");
                    } else {
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
                        choxEventService.send(claim, activityName, "Activity " + activityName + " processed at " + new Date().toString());
                    }
                }

                if (updatedByAnotherTransaction == true) {
                    getActionResponse().AddError(actionError.toString());
                    return ERROR;
                }

            } catch (AccessDeniedException ex) {
                throw (ex);
            } catch (JMSException ex) {
                LOG.error("JMS error sending event: ", ex.getMessage());
            } catch (Exception ex) {
                LOG.error("Error processing batch update. Error on cho-ref: {} : ", claim.getChoReference(), ex);
                handleException(ex);
                return ERROR;
            }
            return SUCCESS;
        }

        return ERROR;
    }

    @Override
    public String execute() {
        JSONObject jsonObject = new JSONObject();
        LOG.debug("Activity " + name + " class " + activity.getClass().getSimpleName());
        if (activity != null) {
            try {
                checkVersion(Arrays.asList(claim));
                activity.process(claim);
                updateModelInSession(Arrays.asList(claim));
                setMessage(activity.getMessage());
                choxEventService.send(claim, name, "Activity " + name + " processed at " + new Date().toString());
            } catch (AccessDeniedException ex) {
                throw (ex);
            } catch (JMSException ex) {
                LOG.error("JMS error sending event: ", ex.getMessage());
            } catch (Exception ex) {
                LOG.warn("Error processing claim activity: {}", ex.getMessage());
                jsonObject.put("success", Boolean.FALSE);
                jsonObject.put("errors", ex.getMessage());
                setJsonData(jsonObject.toString());
                handleException(ex);
                return ERROR;
            }
            LOG.debug("claim activity returning success");
            jsonObject.put("success", Boolean.TRUE);
            if (getMessage() != null) {
                jsonObject.put("message", getMessage());
            }
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

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public Integer getId() {
        if (claim != null) {
            return claim.getId();
        }
        
        return null;
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

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }
}
