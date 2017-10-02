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
import idas.chox.core.services.HireMonitoringEcdService;

/**
 *
 * @author john
 */
public class EcdVsRepairCompletionDate implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(EcdVsRepairCompletionDate.class);
    private String narrative = "";
    private HireMonitoringEcdService hireMonitoringEcdService;
    
    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'EcdVsRepairCompletionDate' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        // Get latest ECD
        Date latestECD = hireMonitoringEcdService.getLatestHireMonitoringECDDate(claim);
        Date repairCompletionDate = claim.getHireMonitoringDetail() != null ? claim.getHireMonitoringDetail().getRepairCompletionDate() : null;
        
        if (claim.getBreBand().isEcdVsRepairCompletionDateCheck() && latestECD != null && repairCompletionDate != null) {
            boolean success = true;
            if (repairCompletionDate.after(latestECD)) {
                success = false;
                narrative = "The Repair Completion Date is after the latest ECD as updated by the CHO, review discrepancy.";
            }
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
        } else {
            LOG.debug("'EcdVsRepairCompletionDate' Business Rule Skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);
        }

        LOG.debug("'EcdVsRepairCompletionDate' Business Rule Finished.");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "094";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }


}
