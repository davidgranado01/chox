package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.util.VehicleClassHelper;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepairNetDoesNotExceedVehicleClassRepairNetCeiling implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(RepairNetDoesNotExceedVehicleClassRepairNetCeiling.class);

    String narrative = "";
    String narrativeTemplate = "The Repair Net billed %s exceeds the Repair Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));
        LOG.debug("Applying rule 'RepairNetDoesNotExceedVehicleClassRepairNetCeiling' to claim {}.", claim.getChoReference());

        if (!ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isRepairNetDoesNotExceedVehicleClassRepairNetCeiling() && claim.getInvoice().getRepairNet() != null) {

            BigDecimal repairNet = claim.getInvoice().getRepairNet();
            BigDecimal repairNetCeiling = claim.getBreBand().getMaxRepairNetCeiling();
            LOG.debug("Comparing repair net: {} to max repair net ceiling: {}", repairNet, repairNetCeiling);
            boolean success = repairNet.compareTo(repairNetCeiling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (!success) {

                String cusVehicleClassName = "";
                if (VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {
                    cusVehicleClassName = claim.getCustomer().getVehicleClass().getName();
                }

                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(repairNet.doubleValue()),
                        moneyFormat.format(repairNetCeiling.doubleValue()),
                        cusVehicleClassName);
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
        return "023";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        if (isTpiClaim)
            return ClaimStatus.INVOICE_ESCALATED_TO_CH;

        return ClaimStatus.INVOICE_ESCALATED;
    }
}
