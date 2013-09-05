package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;
import idas.chox.service.bre.util.VehicleClassHelper;

public class RepairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(RepairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling.class);

    String narrative = "";
    String narrativeTemplate = "The Repair Net billed %s exceeds the Protocol Repair Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");
    private ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService;

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        LOG.debug("Applying rule 'RepairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling' to claim {}.", claim.getChoReference());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isRepairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling() && claim.getInvoice().getRepairNet() != null) {

            BigDecimal repairNet = claim.getInvoice().getRepairNet();
            
            ProtocolVehicleClassCeiling pvcc = protocolVehicleClassCeilingService.getProtocolVechileClassCeilingForClaim(claim);
            BigDecimal protocolRepairNetCeiling = BigDecimal.ZERO;
            if (pvcc != null) {
                protocolRepairNetCeiling = pvcc.getRepairNetCeiling();
            }
            
            LOG.debug("Comparing repair net: {} to max repair net ceiling: {}", repairNet, protocolRepairNetCeiling);
            boolean success = repairNet.compareTo(protocolRepairNetCeiling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (!success) {

                String replacementVehicleClassName = "";
                if (VehicleClassHelper.isVehicleClassValid(claim.getVehicleHire().getVehicleClass())) {
                    replacementVehicleClassName = claim.getVehicleHire().getVehicleClass().getName();
                }

                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(repairNet.doubleValue()),
                        moneyFormat.format(protocolRepairNetCeiling.doubleValue()),
                        replacementVehicleClassName);
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
        return "080";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

    public void setProtocolVehicleClassCeilingService(ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService) {
        this.protocolVehicleClassCeilingService = protocolVehicleClassCeilingService;
    }
}
