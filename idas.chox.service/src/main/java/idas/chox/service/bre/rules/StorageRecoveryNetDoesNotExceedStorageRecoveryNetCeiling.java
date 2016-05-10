package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class StorageRecoveryNetDoesNotExceedStorageRecoveryNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Storage and Recovery billed %s exceeds the Storage Recovery Net ceiling of %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isStorageRecoveryNetCeilingCheck()) {

            BigDecimal storageRecoveryNet = claim.getInvoice().getStorageRecoveryNet();
            BigDecimal storageRecoveryNetCeiling = claim.getBreBand().getStorageRecoveryNetCeiling();
            boolean success = storageRecoveryNet.compareTo(storageRecoveryNetCeiling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (!success) {
                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(storageRecoveryNet.doubleValue()),
                        moneyFormat.format(storageRecoveryNetCeiling.doubleValue()));
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
        return "093";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
