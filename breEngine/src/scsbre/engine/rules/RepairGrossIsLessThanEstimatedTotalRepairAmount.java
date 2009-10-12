/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IEngineerReportInfo;
import scsbre.model.IInvoiceInfo;

/**
 *
 * @author Derm
 * 
 * rule 8, order 17
 */
public class RepairGrossIsLessThanEstimatedTotalRepairAmount implements IBusinessRule {
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        IEngineerReportInfo eReport = claim.getEngineeringReport();    
        IInvoiceInfo invoice = claim.getInvoice();
        
        boolean success;
        if (eReport.getEstimatedTotalRepairAmount().compareTo(BigDecimal.ZERO) > 0) {
            success =  invoice.getRepairGross().compareTo(eReport.getEstimatedTotalRepairAmount()) <= 0;
        } else {
            success = CalcHelper.EqualTo(invoice.getRepairGross(), BigDecimal.ZERO);
        }

        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;

    }     

    public String getNarrative() {
        return "Repair Gross is higher than the Estimated Total Repair Amount.";
    }

    public String getRuleId() {
        return "008";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }    

}
