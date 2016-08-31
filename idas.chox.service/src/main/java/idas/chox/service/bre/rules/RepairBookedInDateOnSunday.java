package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.util.DateHelper;


public class RepairBookedInDateOnSunday implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        boolean isExcluded = false;
        /*
         * Phase 6 Sprint 9 todo item 6.9.6 excludes commercial, private hire and taxi vehicles
         */
        if (claim.getCustomer() != null && claim.getCustomer().getVehicleClass() != null) {
            isExcluded = VehicleClass.isCommercialPrivateOrTaxi(claim.getCustomer().getVehicleClass().getName());
        }

        if(!isExcluded && !ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && !ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isRepairBookedInDateOnSunday() && claim.getHireMonitoringDetail() != null){
            boolean success = true;
            if(claim.getHireMonitoringDetail().getRepairBookInDate()!=null && claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()){

                if(DateHelper.getDayOfWeek(claim.getHireMonitoringDetail().getRepairBookInDate())==1){
                    success = false;
                    narrative = "Repair booked in on Sunday and the CHO's Customer's vehicle was driveable.";
                }

            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        }else{
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
        return "039";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH; 
    }

}