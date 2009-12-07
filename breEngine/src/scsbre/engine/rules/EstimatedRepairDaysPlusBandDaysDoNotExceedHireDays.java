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
import scsbre.model.IBREBandInfo;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;
import scsbre.model.IEngineerReportInfo;

public class EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays implements IBusinessRule {
    
    private String narrative = "Number of hire days billed exceeds the allowable threshold (non total loss) with the inclusion of the Engineer's Esimtated Days Under Repair.";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getBreBand().isEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays()){

            ICustomerVehicleDamageInfo cvdamage = claim.getCustomerVehicleDamage();
            IBREBandInfo breBand = claim.getBreBand();
            IEngineerReportInfo eReport = claim.getEngineeringReport();
        
            if ( (claim.getHireDetail().getIsTotalLoss()) || (eReport.getEstimatedDaysUnderRepair() < 1)) {

                narrative = "Claim is a Total Loss or Estimated Days Under Repair is less than 1";
                res.setResult(RuleEvaluationResult.RuleSkipped);

            }else{

                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

                int hireDays = claim.getHireDetail().getDays();
                
                int takeVehicleToGarageDays = cvdamage.getIsUsable()
                        ? breBand.getTakeVehicleToGarageDaysMobile()
                        : breBand.getTakeVehicleToGarageDaysNonMobile();

                int maxDays = eReport.getEstimatedDaysUnderRepair();

                maxDays += takeVehicleToGarageDays;

                // Basecamp : S8019
                // maxDays += breBand.getWeekendBufferDays();
                maxDays += cCalc.getWeekendBuffer();
                maxDays += breBand.getTakeVehicleOutDays();
                maxDays += breBand.getEngineerInspectionDelayDays();
                
                boolean success = hireDays <= maxDays;

                if(success){
                    narrative = "";
                    res.setResult(RuleEvaluationResult.RulePassed);

                }else{

                    res.setResult(RuleEvaluationResult.RuleFailed);

                }

            }

        }else{

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

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
