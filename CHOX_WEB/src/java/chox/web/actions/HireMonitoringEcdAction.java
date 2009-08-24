/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.HireMonitoringEcd;
import chox.model.ReasonOfDelay;
import chox.services.HireMonitoringEcdService;
import chox.services.LookupService;
import chox.services.ReasonOfDelayService;
import chox.web.security.ApplicationAccessibility;
import chox.web.security.TabAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdAction extends BaseModelAction implements ModelDriven<HireMonitoringEcd>, Preparable {

    private HireMonitoringEcd model;
    private ReasonOfDelayService reasonOfDelayService;
    private HireMonitoringEcdService service;
    private Integer iECDFormAccessRight;
    private static double ecdDurationAllowPercentage = 0.5;
    private List reasonOfDelay;
    private LookupService lookupService;
    private int reasonOfDelayId = -1;

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
    
    public void setHireMonitoringEcdService(HireMonitoringEcdService service)
    {
        this.service = service;
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
                
                Claim claim = claimService.getClaim(claimId);
                claim.setIsAnomalies(getAnomaliesValidation(claim, model));

                model.setClaim(claim);
                model.setReason(reasonOfDelayObject.getName());
                
                this.service.updateObject(model);
                claimService.updateClaim(claim);
                
            }else{
                this.actionResult = "";
            }
            
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
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
     * Edited Date: 20081222
     * Description: Check new added ECD and latest ECD, calculate %
     */
    public Boolean getAnomaliesValidation(Claim claim, HireMonitoringEcd monitoringecd) {
        
        Boolean bFlag = false;
        
        Date policyHolderDate = claim.getPolicyHolderContactDate();
        List<HireMonitoringEcd> ecds = service.getHireMonitoringEcdsByClaimIdFilter(claim.getId(), true, "createdDate");
        
        if(claim.getCustomer().getInitialECD()!=null){
            
            Date FirstEstimateECD = claim.getCustomer().getInitialECD();
            bFlag = isClaimAnomalies(policyHolderDate, monitoringecd.getEcdDate(), FirstEstimateECD);
            return bFlag;                
         
        }else{
            
            if(ecds.size()>0){

                HireMonitoringEcd thisECD = ecds.get(0);
                Date FirstEstimateECD = thisECD.getEcdDate();
                bFlag = isClaimAnomalies(policyHolderDate, monitoringecd.getEcdDate(), FirstEstimateECD);
                return bFlag;
            }
            
        }
        
        return false;
    }
    
    private Boolean isClaimAnomalies(Date policyHolderDate, Date newECDDate, Date firstECD){
        
        Long iTotalDelayDays = DateHelper.daysBetween(firstECD, newECDDate);
        Long iMD = DateHelper.daysBetween(policyHolderDate, firstECD);
        
        if(iMD>0){
            
            int iMDRate = (int)java.lang.Math.round(iMD * ecdDurationAllowPercentage);
            
            if((iTotalDelayDays > iMDRate)){
                return true;
            }
            
        }
        
        return false;
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
        
        /*
        Claim claim = claimService.getClaim(claimId);
        
        if(this.getIsCHO()
            && 
            (
            claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_CAR_HIRE_INFO)
            || claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)
            )
        ){
            bFlag = true;
        }
        */
        return bFlag;
 
    }
    
    
    public void setReasonOfDelayService(ReasonOfDelayService reasonOfDelayService) { this.reasonOfDelayService = reasonOfDelayService; }    
    public void setLookupService(LookupService lookupService) { this.lookupService = lookupService; }    
}
