package idas.chox.service.bre.rules;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

/*
 * Rajareddy Dodda
 */
public class RepairsBookInDateFollowingHireStartCheckForNonMobileVehicles implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(RepairsBookInDateFollowingHireStartCheckForNonMobileVehicles.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isDateRepairBookInDateChkForNonMobileVehicle() && claim.getCustomer().getIsUsable() != null && !claim.getCustomer().getIsUsable()
                && claim.getHireMonitoringDetail() != null && claim.getVehicleHire() != null) {

            LOG.debug("Repairs Book In Date Following HireStart Check For NonMobile Vehicles  is active");

            boolean success = true;

            int maxDays = claim.getBreBand().getHireDaysPriorToDateRepairBookInDateNonMobileVehicles();
            Date repairBookInDate = claim.getHireMonitoringDetail().getRepairBookInDate();
            Date hireStartDate = claim.getVehicleHire().getHireStart();
            int noOfDays = 0;

            if (repairBookInDate != null && hireStartDate != null) {
                noOfDays = (int) ((repairBookInDate.getTime() - hireStartDate.getTime()) / (1000 * 60 * 60 * 24));
            }
            else {
                LOG.debug("Cannot fail rule as repairBookInDate={} and hireStartDate={}", repairBookInDate, hireStartDate);
            }

            LOG.debug("'HireDaysPriorToDateRepairBookInDateMobileVehicles'  {}. ", maxDays);
            LOG.debug("Number of Days between repair book in date and hire start  {} ", noOfDays);

            if (noOfDays > maxDays) {
                success = false;
                narrative = "The hire commenced " + noOfDays + " days prior to the repair book in date, the allowable number of days is " + maxDays + " day(s) for un-driveable vehicles.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }
        LOG.debug("RepairsBookInDateFollowingHireStartCheckForMobileVehicles is End");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;


    }

    @Override
    public String getRuleId() {
        return "065";


    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}
