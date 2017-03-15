package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import idas.chox.service.workflow.activities.SaveOrSubmitClaimAuditReview;

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

    private ApplicationAccessibility applicationAccessibility;

    @Override
    public Activity getModel() {
        return activity;
    }

    @Override
    public boolean getInsurerIsWorkgroupEnabled() {
        return claim.getInsurer().isWorkgroupEnable();
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

        LOG.trace("Claim Activity Action " + name);
        activity = activityFactory.getActivity(name);
        // TO-DO Refactor the Activity to have a claim property and all the activities 
        // should access the 'claim' from activities claim property instead passing claim around in the method param.
        // e.g activity.process(claim) should be refactored to activity.process()

        // activity.setClaim(claim);
        // Once the above refactor done remove the below code and access the ClaimAuditReview object from Claim object directly in the activities.
        if (claim != null && name.equals("saveOrSubmitClaimAuditReview")) {
            ((SaveOrSubmitClaimAuditReview) activity).setClaimAuditReview(claim.getClaimAuditReview());
        }

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

                        if ((getIsInsurer() && claim.getInsurer().getId() != getAuthenticatedUser().getInsurer().getId().intValue())
                                || (getIsCHO() && claim.getChorganisation().getId() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                            throw new AccessDeniedException("Attempt to access a claim that you do not own.");
                        }
                        activity.process(claim);
                    }
                }

                if (updatedByAnotherTransaction == true) {
                    getActionResponse().AddError(actionError.toString());
                    return ERROR;
                }

            } catch (AccessDeniedException ex) {
                LOG.error("AccessDenied exception thrown in batch update with activity '{}' on claim with id={}, cho_reference='{}' in status {}: {}",
                        new Object[]{name, claim.getId(), claim.getChoReference(), claim.getStatus(), ex.getMessage()});
                throw (ex);
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
        if (activity != null) {
            LOG.debug("Executing Activity '{}' (with class {}) on claim with id={}, cho_reference='{}'",
                new Object[]{name, activity.getClass().getSimpleName(), claim.getId(), claim.getChoReference()});
            try {
                checkVersion(Arrays.asList(claim));
                activity.process(claim);
                updateModelInSession(Arrays.asList(claim));
                setMessage(activity.getMessage());
            } catch (AccessDeniedException ex) {
                LOG.error("AccessDenied exception thrown with activity '{}' on claim with id={}, cho_reference='{}' in status {}: {}",
                        new Object[]{name, claim.getId(), claim.getChoReference(), claim.getStatus(), ex.getMessage()});
// Lets return an error for now rather than re-throwing the exception
// Once the reason for this happening so often is determined, the code should be reverted to re-throw the exception
//                throw (ex);
                jsonObject.put("success", Boolean.FALSE);
                jsonObject.put("errors", ex.getMessage());
                setJsonData(jsonObject.toString());
                handleException(ex);
                updateRedirectionParamInSession();
                return ERROR;
            } catch (Exception ex) {
                LOG.warn("Error processing claim activity: {}", ex.getMessage(),ex);
                jsonObject.put("success", Boolean.FALSE);
                jsonObject.put("errors", ex.getMessage());
                setJsonData(jsonObject.toString());
                handleException(ex);
                updateRedirectionParamInSession();
                return ERROR;
            }
            LOG.trace("claim activity returning success");
            jsonObject.put("success", Boolean.TRUE);
            if (getMessage() != null) {
                jsonObject.put("message", getMessage());
                this.getActionResponse().AssignMessageResult(getMessage());
            }
            setJsonData(jsonObject.toString());
            removeRedirectionParamInSession();
//            updateRedirectionParamInSession();
            return SUCCESS;
        } else {
            LOG.error("Cannot process null activity for claim '{}'", claim);
            jsonObject.put("success", Boolean.FALSE);
            jsonObject.put("errors", "An Internal Error Occurred - please try again. If this problem persists, please contact CHOX Support.");
            setJsonData(jsonObject.toString());
            this.getActionResponse().AddError("An Internal Error Occurred - please try again. If this problem persists, please contact CHOX Support.");
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
        selectedClaimIdList = new ArrayList<>();
        for (String s : list) {
            Integer selectedId = Integer.parseInt(s.trim());
            selectedClaimIdList.add(selectedId);
        }
        LOG.trace("selectedClaimIdList set: '{}'", ids);
    }

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("ClaimActivityAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.trace("ClaimActivityAction validate success");
        } else {
            LOG.debug(" ClaimActivityAction validation is not done as claim is null");
        }
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }
}
