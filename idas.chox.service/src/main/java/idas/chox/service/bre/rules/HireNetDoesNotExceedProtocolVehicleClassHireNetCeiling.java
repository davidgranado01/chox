package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;
import idas.chox.service.bre.util.VehicleClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling.class);

    String narrative = "";
    String narrativeTemplate = "The Hire Net billed %s exceeds the Protocol Hire Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");
    private ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService;

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        LOG.debug("Applying rule 'HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling' to claim {}.", claim.getChoReference());
        
        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling() && claim.getInvoice().getHireNet() != null) {

            BigDecimal hireNet = claim.getInvoice().getHireNet();

            ProtocolVehicleClassCeiling pvcc = protocolVehicleClassCeilingService.getProtocolVechileClassCeilingForClaim(claim);
            BigDecimal protocolHireNetCeiling = BigDecimal.ZERO;
            if (pvcc != null) {
                protocolHireNetCeiling = pvcc.getHireNetCeiling();
            }
            
            boolean success = hireNet.compareTo(protocolHireNetCeiling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (!success) {

                String customerVehicleClassName = "";
                if (VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {
                    customerVehicleClassName = claim.getCustomer().getVehicleClass().getName();
                }
                
                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(hireNet.doubleValue()),
                        moneyFormat.format(protocolHireNetCeiling.doubleValue()),
                        customerVehicleClassName);
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
        return "079";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

    public void setProtocolVehicleClassCeilingService(ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService) {
        this.protocolVehicleClassCeilingService = protocolVehicleClassCeilingService;
    }
}
