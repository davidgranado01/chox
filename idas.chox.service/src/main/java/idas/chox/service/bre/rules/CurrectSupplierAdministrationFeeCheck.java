/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.math.BigDecimal;
import java.util.Date;

/*
 * Rajareddy Dodda
 */
public class CurrectSupplierAdministrationFeeCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isSupplierAdminstrationFee()) {

            boolean success = true;

            BigDecimal adminFeeCeiling = claim.getBreBand().getAdminFeeCeiling();
            BigDecimal supplierAdminFee = claim.getInvoice().getAdminFee();

            
            
            if (supplierAdminFee.compareTo(adminFeeCeiling) > 0) {

                narrative = "";

            } else {

                success = false;
                //CHO is charging [£x] for the Admin Fee, the allowed Admin Fee is [£y].
                narrative = "CHO is charging [£"+ supplierAdminFee +"] for the Admin Fee, the allowed Admin Fee is [£"+ adminFeeCeiling +"].";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;


    }

    @Override
    public String getRuleId() {
        return "067";


    }

    @Override
    public String getStatusAfterFailure() {

        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;

    }
}
