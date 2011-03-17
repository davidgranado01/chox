/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.util.CalcHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * Rajareddy Dodda
 */
public class AutorestoreOneDayRepairCheck implements IBusinessRule {
    
    private static final Logger LOG = LoggerFactory.getLogger(AutorestoreOneDayRepairCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isAutoRestoreOneDayRepairCheck() && claim.getCustomer().getIsUsable()) {

            LOG.debug("Auto restore One Day Repair Check  is active");

            boolean success = true;
            String name = claim.getHireMonitoringDetail().getNameOfRepairer();
            int noDays = claim.getBreBand().getNumberOfDays();
            String nameOfRepairer = claim.getBreBand().getNameOfRepairer();
            Integer dayDif = (CalcHelper.getDaysBetweenDates(claim.getVehicleHire().getRentalStart(), claim.getVehicleHire().getRentalEnd()) + 1);

            LOG.debug(" 'Repairer Name given in HireMonitoring Detail Panel'  {}. ", name);
            LOG.debug(" Number of days given in Admin BreBand panel  {} ", noDays);
            LOG.debug(" 'Name of repairer assigned in BreBand Panel'  {}. ", nameOfRepairer);
            LOG.debug(" Number days between the Rental start and Rental End  {} ", dayDif);


            if (nameOfRepairer.equals(name) && noDays == dayDif) {

                narrative = "";

            } else {

                success = false;
                narrative = "The number of hire days billed " + dayDif + " days exceeds the allowable number of hire days for Autorestore repairs (4 days)";
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
    public String getStatusAfterFailure() {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}
