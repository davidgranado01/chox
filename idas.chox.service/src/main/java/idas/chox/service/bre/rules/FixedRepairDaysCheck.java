package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

import idas.chox.core.model.ClaimType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * Rajareddy Dodda
 */
public class FixedRepairDaysCheck implements IBusinessRule {
    
    private static final Logger LOG = LoggerFactory.getLogger(FixedRepairDaysCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (!ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isAutoRestoreOneDayRepairCheck() && claim.getCustomer().getIsUsable()
                && claim.getHireMonitoringDetail() != null && claim.getVehicleHire() != null) {

            LOG.debug("Auto restore One Day Repair Check  is active");

            boolean success = true;
            String name = claim.getHireMonitoringDetail().getNameOfRepairer();
            String nameOfRepairer = claim.getBreBand().getNameOfRepairer();
            int noDaysAllowed = claim.getBreBand().getNumberOfDays();
            int noDaysTaken = claim.getVehicleHire().getDays();

            LOG.debug("'Repairer Name given in HireMonitoring Detail Panel'  {}. ", name);
            LOG.debug("Number of days given in Admin BreBand panel  {} ", noDaysAllowed);
            LOG.debug("'Name of repairer assigned in BreBand Panel'  {}. ", nameOfRepairer);
            LOG.debug("Number days taken for hire  {} ", noDaysTaken);

            if (nameOfRepairer != null && name != null && nameOfRepairer.toLowerCase().equals(name.toLowerCase()) && noDaysTaken > noDaysAllowed) {
                success = false;
                narrative = "The number of hire days billed (" + noDaysTaken
                        + " days) exceeds the allowable number of hire days for '"
                        + nameOfRepairer + "' repairs (" + noDaysAllowed + " days)";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }
        LOG.debug(" Auto restore One Day Repair Check  is End");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;


    }

    @Override
    public String getRuleId() {
        return "068";


    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}
