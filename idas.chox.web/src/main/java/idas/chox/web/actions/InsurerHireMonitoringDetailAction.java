package idas.chox.web.actions;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.InsurerHireMonitoringDetail;
import idas.chox.service.security.TabAccessibility;
import idas.chox.service.workflow.activities.ActivityEvent;

/**
 *
 * @author John
 */
public class InsurerHireMonitoringDetailAction extends ClaimModelAction<InsurerHireMonitoringDetail> {
    
    private static final Logger LOG = LoggerFactory.getLogger(InsurerHireMonitoringDetailAction.class);
    private String labourRate;
    private String labourHour;
    private String labourCost;
    private String claimantImpecunious;
    
    public String getLabourCost() {
        return labourCost;
    }
    
    public void setLabourCost(String labourCost) {
        this.labourCost = labourCost;
    }
    
    public String getLabourHour() {
        return labourHour;
    }
    
    public void setLabourHour(String labourHour) {
        this.labourHour = labourHour;
    }
    
    public String getLabourRate() {
        return labourRate;
    }
    
    public void setLabourRate(String labourRate) {
        this.labourRate = labourRate;
    }
    
    public void setClaimantImpecunious(String claimantImpecunious) {
        this.claimantImpecunious = claimantImpecunious;
    }

    @Override
    public InsurerHireMonitoringDetail loadModel() {
        
        InsurerHireMonitoringDetail insurerHireMonitoringDetail = claim.getInsurerHireMonitoringDetail();
        if (insurerHireMonitoringDetail == null) {
            insurerHireMonitoringDetail = new InsurerHireMonitoringDetail();
        }
        return insurerHireMonitoringDetail;
    }
    
    @Override
    public String updateModel() {
        try {
            checkVersion(Arrays.asList(claim,model));
            
            /*
             * labourCost , labourHour, labourRate is defined here as String to
             * accept null value. Struts is not setting null value for those
             * Bigdecimal fields in model class. see bug#1018 for more details.
             */
            if (this.labourCost.trim().isEmpty()) {
                model.setLabourCost(null);
            }
            if (this.labourHour.trim().isEmpty()) {
                model.setLabourHour(null);
            }
            if (this.labourRate.trim().isEmpty()) {
                model.setLabourRate(null);
            }
            if (this.claimantImpecunious.trim().isEmpty()) {
                model.setClaimantImpecunious(null);
            }

            claim.setInsurerHireMonitoringDetail(model);


            LOG.debug("HireMonitoringDetail to be updated: claim version={}, hmd version={}", claim.getVersion(), model.getVersion());

            String result = super.updateModel();
            
            activityEventGenerator.generate(claim, ActivityEvent.INSURER_HIRE_MONITORING_UPDATED_EVENT);

            return result;
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
    }

    @Override
    public void validate() {
        if (claim != null) {
            if (!(getIsInsurer() && claim.getInsurer().getId().intValue() == getAuthenticatedUser().getInsurer().getId().intValue())) {
                LOG.error("HireMonitoringDetailAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
        } else {
            LOG.debug(" InsurerHireMonitoringDetailAction validation is not done as claim is null");
        }
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_INSURER_HIRE_MONITORING;
    }
        
}
