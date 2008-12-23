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
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        ICustomerVehicleDamageInfo Icust = claim.getCustomerVehicleDamage();
        Boolean success = Icust.isVehicleRegistrationExist();
        
        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RuleFailed : RuleEvaluationResult.RulePassed);
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);   
        return res;

    }

    public String getNarrative() {
        return "The Vehicle Registration Number supplied already exists in the system.";
    }

    public String getRuleId() {
        return "021";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceVehicleRegistrationNotUnique;
    }

}
