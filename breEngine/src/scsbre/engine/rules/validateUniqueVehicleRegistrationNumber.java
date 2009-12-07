/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;

public class validateUniqueVehicleRegistrationNumber implements IBusinessRule {
    
    private String narrative = "";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        if(claim.getBreBand().isValidateUniqueVehicleRegistrationNumber()){
            
            ICustomerVehicleDamageInfo Icust = claim.getCustomerVehicleDamage();
            
            Boolean isVehicleRegistrationExist = Icust.isVehicleRegistrationExist();
            
            res.setResult(isVehicleRegistrationExist ? RuleEvaluationResult.RuleFailed : RuleEvaluationResult.RulePassed);

            if(isVehicleRegistrationExist){
                narrative = "The Customer's Vehicle Registration Number supplied already exists in the system.";
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
        return "021";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceVehicleRegistrationNotUnique;
    }

}
