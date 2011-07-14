package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HasCorrectAdminFee implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HasCorrectAdminFee.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(claim.isTpiClaim());

        if (claim.getBreBand().isCorrentAdminFee() && claim.getVehicleHire() != null) {
          try {
            boolean success = true;
            BigDecimal adminFee = null;
            boolean managingRepair = claim.getManagingRepair();
            boolean coverNoteRequired = (claim.getInvoice().getCoverNoteRequired() == null ? false : claim.getInvoice().getCoverNoteRequired());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date firstJuly2011 = formatter.parse("2011/07/01");
            Date hireStart = claim.getVehicleHire().getHireStart();
            
            if (firstJuly2011.compareTo(hireStart) > 0) {
                if (managingRepair && coverNoteRequired)
                    adminFee = new BigDecimal("60.00");
                else if (managingRepair && !coverNoteRequired)
                    adminFee = new BigDecimal("50.00");
                else if (!managingRepair && coverNoteRequired)
                    adminFee = new BigDecimal("40.00");
                else if (!managingRepair && !coverNoteRequired)
                    adminFee = new BigDecimal("30.00");
            } else { // after 1/7/2011
                if (managingRepair && coverNoteRequired)
                    adminFee = new BigDecimal("61.00");
                else if (managingRepair && !coverNoteRequired)
                    adminFee = new BigDecimal("51.00");
                else if (!managingRepair && coverNoteRequired)
                    adminFee = new BigDecimal("41.00");
                else if (!managingRepair && !coverNoteRequired)
                    adminFee = new BigDecimal("31.00");
            }
            
            if (claim.getInvoice().getAdminFee().compareTo(adminFee) > 0) {
                success = false;
                narrative = "The Admin Fee billed is incorrect. The allowed Admin Fee is £" + adminFee + ".";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
          } catch (Exception ex) {
              LOG.error("Exception thrown: {}", ex.getMessage());
              narrative="An error occurred applying this rule and the rule was skipped.";
              res.setResult(RuleEvaluationResult.RuleSkipped);
          }
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
        return "025";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
