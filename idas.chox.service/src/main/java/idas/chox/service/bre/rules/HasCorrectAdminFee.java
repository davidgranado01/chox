package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.AdminFeeService;

public class HasCorrectAdminFee implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HasCorrectAdminFee.class);
    private AdminFeeService adminFeeService;
    private String narrative = "";

    public void setAdminFeeService(AdminFeeService adminFeeService) {
        this.adminFeeService = adminFeeService;
    }

    
    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (!ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isCorrentAdminFee()) {
          try {
            boolean success = true;
            Date hireStart;
            // If no hire start available, use the date invoiced
            if (claim.getVehicleHire() == null || claim.getVehicleHire().getHireStart() == null) {
                LOG.debug("No hire start available - useing date invoiced.");
                hireStart = claim.getInvoice().getDateInvoiced();
            }
            else {
                  hireStart = claim.getVehicleHire().getHireStart();
              }
            boolean coverNoteRequired = (claim.getInvoice().getCoverNoteRequired() == null ? false : claim.getInvoice().getCoverNoteRequired());
            BigDecimal adminFee = adminFeeService.getAdminFee(hireStart, coverNoteRequired, claim.getManagingRepair());
            
            if (claim.getInvoice().getAdminFee().compareTo(adminFee) > 0) {
                success = false;
                narrative = "The Admin Fee billed is incorrect. The allowed Admin Fee is £" + adminFee + ".";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
          } catch (Exception ex) {
              LOG.error("Exception thrown applying BRE rule to claim with id={}: {}", claim.getId(), ex.getMessage());
              narrative="An error occurred applying this rule and the rule was skipped.";
              res.setResult(RuleEvaluationResult.RULE_SKIPPED);
          }
        } else {
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);
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
