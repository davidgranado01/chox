/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.ICHOBandInfo;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;
import scsbre.model.IEngineerReportInfo;

public class EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays implements IBusinessRule {
    
    private String narrative = "Number of hire days billed exceeds the allowable threshold (non total loss) with the inclusion of the Engineer's Esimtated Days Under Repair.";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        ICustomerVehicleDamageInfo cvdamage = claim.getCustomerVehicleDamage();  
        ICHOBandInfo choBand = claim.getChoBand();
        IEngineerReportInfo eReport = claim.getEngineeringReport();
        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        if ( (claim.getHireDetail().getIsTotalLoss()) || (eReport.getEstimatedDaysUnderRepair() < 1)) {
            
            narrative = "Claim is a Total Loss or Estimated Days Under Repair is less than 1";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }else{

            // EDITED by CARLSON @ 20091007
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
            
            int hireDays = claim.getHireDetail().getNumberOfHireDays();
            int takeVehicleToGarageDays = cvdamage.getIsUsable()
                    ? choBand.getTakeVehicleToGarageDaysMobile()
                    : choBand.getTakeVehicleToGarageDaysNonMobile();

            int maxDays = eReport.getEstimatedDaysUnderRepair();

            maxDays += takeVehicleToGarageDays;
            
            // TODO
            //maxDays += choBand.getWeekendBufferDays();
            maxDays += cCalc.getWeekendBuffer();
            maxDays += choBand.getTakeVehicleOutDays();
            maxDays += choBand.getEngineerInspectionDelayDays();
            boolean success = hireDays <= maxDays;
            
            if(success){
                
                narrative = "";
                res.setResult(RuleEvaluationResult.RulePassed);

            }else{
        
                res.setResult(RuleEvaluationResult.RuleFailed);
                
            }
            
        }
        
        return res;

    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "020";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }
    
    
    

}
