package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.util.ClaimCalcHelper;

public class ActualHireDaysDoesNotExceedAllowableHireDays implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(ActualHireDaysDoesNotExceedAllowableHireDays.class);

    private String narrative = "Number of hire days billed by the CHO exceeds the allowable days threshold (non total loss), taking into account the ECD(s) provided by the CHO and the additional days allowed through delay variables.";
    
    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        LOG.debug("Applying rule 'ActualHireDaysDoesNotExceedAllowableHireDays' to claim {}.", claim.getChoReference());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isActualHireDaysDoesNotExceedAllowableHireDays()  && claim.getVehicleHire() != null){
            LOG.debug("Engineer Report: {}", claim.getEngineerReport());
            LOG.debug("Total loss: {}", claim.getVehicleHire().getIsTotalLoss());
            if (!claim.getVehicleHire().getIsTotalLoss() && (claim.getEngineerReport() == null || claim.getEngineerReport().getEstimatedDaysUnderRepair() < 1)) {
                LOG.debug("Applying rule with Engineer report={}", claim.getEngineerReport());
                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                boolean success = claim.getVehicleHire().getDays() <= cCalc.getAllowedDays();

                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
                
                if (success) {
                    LOG.debug("Rule passed.");
                    narrative = "";
                }else{
                    LOG.debug("Rule failed. vehicle hire days ({}) > allowed days ({})", claim.getVehicleHire().getDays(), cCalc.getAllowedDays());
                    narrative = "Number of hire days billed by the CHO exceeds the allowable days threshold (non total loss), taking into account the ECD(s) provided by the CHO and the additional days allowed through delay variables.";
                }

            } else {
               res.setResult(RuleEvaluationResult.RULE_SKIPPED);
               LOG.debug("Rule skipped: isTotalLoss: {}, EstimatedDaysUnderRepair: {}", claim.getVehicleHire().getIsTotalLoss(), claim.getEngineerReport().getEstimatedDaysUnderRepair());
               narrative = "Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair";
            }

        }else{
            LOG.debug("Rule de-activated or no vehicle hire available");
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
        return "006";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
    

}
