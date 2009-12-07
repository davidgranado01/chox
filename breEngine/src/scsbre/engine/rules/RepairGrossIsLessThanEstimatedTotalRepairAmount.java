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

public class RepairGrossIsLessThanEstimatedTotalRepairAmount implements IBusinessRule {
    
    private String narrative = "Repair Gross is higher than the Estimated Total Repair Amount.";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getBreBand().isRepairGrossIsLessThanEstimatedTotalRepairAmount()){
        
            IEngineerReportInfo eReport = claim.getEngineeringReport();
            IInvoiceInfo invoice = claim.getInvoice();

            boolean success;
            
            if (eReport.getEstimatedTotalRepairAmount().compareTo(BigDecimal.ZERO) > 0) {
                
                success =  invoice.getRepairGross().compareTo(eReport.getEstimatedTotalRepairAmount()) <= 0;
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                
            } else {
                success = true;
                res.setResult(RuleEvaluationResult.RuleSkipped);
                // success = CalcHelper.EqualTo(invoice.getRepairGross(), BigDecimal.ZERO);
            }

            if(success){
                narrative = "";
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
        return "008";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }    
}
