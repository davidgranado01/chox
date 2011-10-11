package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * Rajareddy Dodda
 */
public class HasCorrectSupplierAdministrationFee implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(HasCorrectSupplierAdministrationFee.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(claim.isTpiClaim());

        if (claim.getBreBand().isSupplierAdminstrationFee()) {

            LOG.debug("Currect Supplier Administration Fee Check is active");

            boolean success = true;

            BigDecimal adminFeeCeiling = claim.getBreBand().getAdminFeeCeiling();
            BigDecimal supplierAdminFee = claim.getInvoice().getAdminFee();

            LOG.debug(" 'adminFeeCeiling'  {}. ", adminFeeCeiling);
            LOG.debug(" supplierAdminFee  {} ", supplierAdminFee);
            
            if (supplierAdminFee != null && supplierAdminFee.compareTo(adminFeeCeiling) > 0) {
                success = false;
                narrative = "CHO is charging £"+ supplierAdminFee +" for the Admin Fee, the allowed Admin Fee is £"+ adminFeeCeiling +".";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

        } else {
            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);
        }
         LOG.debug("Currect Supplier Administration Fee Check  is End");
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
    public String getStatusAfterFailure(boolean isTpiClaim) {

        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;

    }
}
