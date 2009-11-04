/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.HireMonitoringEcd;
import chox.model.ReasonOfDelay;
import chox.model.notifications.ClaimAnomalousChecker;
import chox.model.notifications.EcdUpdatedNotification;
import chox.services.LookupService;
import chox.services.ReasonOfDelayService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdAction extends BaseModelAction implements ModelDriven<HireMonitoringEcd>, Preparable {

    private HireMonitoringEcd model;
    private ReasonOfDelayService reasonOfDelayService;
    private Integer iECDFormAccessRight;
    private List reasonOfDelay;
    private LookupService lookupService;
    private int reasonOfDelayId = -1;
    
    private ClaimAnomalousChecker newECDAddedChecker;
    private boolean isUpdateInsurer;

    public Integer getIECDFormAccessRight() {
        return iECDFormAccessRight;
    }

    public void setIECDFormAccessRight(Integer iECDFormAccessRight) {
        this.iECDFormAccessRight = iECDFormAccessRight;
    }
    
    public int getReasonOfDelayId() {
        return reasonOfDelayId;
    }

    public void setReasonOfDelayId(int reasonOfDelayId) {
        this.reasonOfDelayId = reasonOfDelayId;
    }
     
    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HIRE_MONITORING;
    }

    public HireMonitoringEcd getModel() {
        return model;
    }

    public void prepare() throws Exception {
        model = new HireMonitoringEcd();
    }

    public String addNewHireMonitoringEcd() {
        
        try {
            
            if(reasonOfDelayId>0){
            
                ReasonOfDelay reasonOfDelayObject = reasonOfDelayService.getObject(reasonOfDelayId);
                model.setReason(reasonOfDelayObject.getName());
                
                Claim claim = claimService.getClaim(claimId);
                claim.addHireMonitoringEcd(model);

                List notifications = newECDAddedChecker.getAnomalousNotifications(claim);
                claim.AddNotifications(newECDAddedChecker.getAnomalousChecks(), notifications);
                
                if(isIsUpdateInsurer()){claim.AddNotification(new EcdUpdatedNotification());}
                
                claimService.updateClaim(claim);
                
            }
            
        } catch (Exception ex) {
           this.getActionResponse().AddError(ex.getMessage());
        }
        
        return SUCCESS;
    }
    
    public List getReasonOfDelay(){
        if (reasonOfDelay == null) {
            reasonOfDelay = this.lookupService.getReasonOfDelay();
        }
        return reasonOfDelay;
    }
    
    /* Edited by: Carlson
     * Edited Date: 20090708
     * Description: New Reason Type
     */    
    public List<String> getReasonTypes() {
        
        List<String> reasonTypes = new ArrayList<String>();  
        reasonTypes.add("Additional Damage");
        reasonTypes.add("Failed QC");
        reasonTypes.add("First ECD");
        reasonTypes.add("Gone To Dealers");
        reasonTypes.add("Parts Delay");
        reasonTypes.add("Repairs Taking Longer Than Expected");
        reasonTypes.add("Total Loss");
        reasonTypes.add("Other");
        return reasonTypes;

    }
    
    /* Edited by: Carlson
     * Edited Date: 20081216
     * Source: According to Emm, the NEW ECD only can be added by CHO 
     * and Where the claim status is either AwaitingInvoiceData OR AwaitingCarHireInfo
     */
    public Boolean getIsECDFormVisible() {

        Boolean bFlag = false;
        
        if(iECDFormAccessRight==2){
            bFlag = true;
        }
       
        return bFlag;
 
    }
    
    
    public void setReasonOfDelayService(ReasonOfDelayService reasonOfDelayService) { this.reasonOfDelayService = reasonOfDelayService; }    
    public void setLookupService(LookupService lookupService) { this.lookupService = lookupService; }

    /**
     * @param claimAnomalousChecker the claimAnomalousChecker to set
     */
    public void setNewECDAddedChecker(ClaimAnomalousChecker claimAnomalousChecker) {
        this.newECDAddedChecker = claimAnomalousChecker;
    }

    /**
     * @return the isUpdateInsurer
     */
    public boolean isIsUpdateInsurer() {
        return isUpdateInsurer;
    }

    /**
     * @param isUpdateInsurer the isUpdateInsurer to set
     */
    public void setIsUpdateInsurer(boolean isUpdateInsurer) {
        this.isUpdateInsurer = isUpdateInsurer;
    }
}
