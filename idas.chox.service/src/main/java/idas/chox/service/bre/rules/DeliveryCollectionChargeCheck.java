package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

import java.math.BigDecimal;

/**
 * Where switched on, the rule shall be applied when the following conditions are met:
 * Where vehicle is deemed usable and collection/delivery value is > 0
 * <p>
 * Claim type is a GTA Claim types, including manual types (i.e. not for Collaboration, Subscriber or Fixed-Fee claims).
 * <p>
 * Invoice will be flagged if the CHO is charging for this extra and the vehicle is marked by the CHO as driveable/usable/mobile
 * <p>
 * Failure statuses:
 * InvoiceEscalatedToHandler/ManualInvoiceBREFailed
 * <p>
 * Rule Failure Message:
 * The CHO is claiming for delivery and collection and the vehicle is marked as usable
 */
public class DeliveryCollectionChargeCheck implements IBusinessRule {

    public static final String FAILURE_MESSAGE = "The CHO is claiming for delivery and collection and the vehicle is marked as usable.";
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isDeliveryCollectionChargeCheck() &&
                (ClaimType.isGTA_WideDef(claim.getClaimType()) || ClaimType.isInsurerUpload(claim.getClaimType()))) {

            boolean success = true;

            if (claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable() &&
                    claim.getInvoice().getDeliveryCollectionFee().compareTo(BigDecimal.ZERO) > 0) {
                success = false;
                narrative = FAILURE_MESSAGE;
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

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
        return "109";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isInsurerUpload(claimType)) {
            return ClaimStatus.MANUAL_INVOICE_REJECTED;
        }

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}

