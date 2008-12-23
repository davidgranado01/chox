/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.HireMonitoringEcd;
import chox.services.HireMonitoringEcdService;
import chox.web.security.ApplicationAccessibility;
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
    private HireMonitoringEcdService service;
    private Boolean isECDFormVisible = false;
    private static double ecdDurationAllowPercentage = 0.5;
    
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
            
            Claim claim = claimService.getClaim(claimId);
            claim.setIsAnomalies(getAnomaliesValidation(claim, model));
            
            
            model.setClaim(claim);
            this.service.updateObject(model);
            
            
            
            claimService.updateClaim(claim);
            
            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }
    
    public List<String> getReasonTypes() {
        List<String> reasonTypes = new ArrayList<String>();        
        reasonTypes.add("Parts Delay");
        reasonTypes.add("Incorrect Parts");
        reasonTypes.add("Parts Damaged");
        reasonTypes.add("Delayed Insurer Approval");
        reasonTypes.add("Customer Delay");
        reasonTypes.add("Resource Inefficiencies");
        reasonTypes.add("Inspection Delay");
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
        
        // System.out.println("***** policyHolderDate: "+policyHolderDate);
        // System.out.println("***** newECDDate: "+newECDDate);
        // System.out.println("***** firstECD: "+firstECD);
        
        Long iTotalDelayDays = DateHelper.daysBetween(firstECD, newECDDate);
        Long iMD = DateHelper.daysBetween(policyHolderDate, firstECD);
        
        // System.out.println("***** iTotalDelayDays: "+iTotalDelayDays);
        // System.out.println("***** iMD: "+iMD);
        
        if(iMD>0){
            
            int iMDRate = (int)java.lang.Math.round(iMD * ecdDurationAllowPercentage);
            
            // System.out.println("***** iMDRate: "+iMDRate);
            // System.out.println("***** VALUE: "+(iTotalDelayDays/iMDRate));
            
            if((iTotalDelayDays > iMDRate)){
                return true;
            }
            
            /*
            // int iInitialEstimateDurationDayAllow = (int) (initialEstimateDays * ecdDurationAllowRate);
            // Long newEstimateDurationDays = DateHelper.daysBetween(LastEstimateECD, newECDDate);
            // System.out.println("initialEstimateDays: "+initialEstimateDays);
            // System.out.println("iInitialEstimateDurationDayAllow: "+iInitialEstimateDurationDayAllow);
            // System.out.println("newEstimateDurationDays: "+newEstimateDurationDays);
            if(newEstimateDurationDays>0 && (newEstimateDurationDays>iInitialEstimateDurationDayAllow)){
                return true;
            }
            */
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
        Claim claim = claimService.getClaim(claimId);        
        if(this.getIsCHO() 
            && (claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_CAR_HIRE_INFO)
            || claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA))
        ){
            bFlag = true;
        }
        return bFlag;
    }    
    
}
