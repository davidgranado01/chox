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
import idas.chox.core.model.VehicleClass;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.util.VehicleClassHelper;

/**
 *
 * @author John
 */
public class VehicleClassHireProvisionLikeForLikeOver9SP implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassHireProvisionLikeForLikeOver9SP.class);
    private String narrative = "The CHO's customer's vehicle is [x] years old and vehicle class [y], the replacement vehicle class of [z] is not acceptable as the replacement vehicle class should be one class less than the CHO's customer's vehicle based on the age of the vehicle and the agreement in place.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        LOG.debug("Applying rule 'VehicleClassHireProvisionLikeForLikeOver9' to claim {}.", claim.getChoReference());

        if (claim.getBreBand().isVehicleClassHireProvisionLikeForLikeOver9SP()) {
            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {
                Date firstRegistration = claim.getCustomer().getHpiFirstRegistration();
                if (firstRegistration == null) {
                    LOG.debug("Rule skipped: no first registration date available.");
                    narrative = "Customer vehicle registration date not available.";
                    res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                } else {

                    if (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) {
                        Date hireStart = claim.getVehicleHire().getHireStart();
                        int difference = DateHelper.differenceInYearsAsInt(hireStart, firstRegistration);
                        LOG.debug("Difference in years between {} and {} is {}", new Object[]{hireStart, firstRegistration, Integer.toString(difference)});
                        if (difference < 9) {
                            LOG.debug("Rule skipped: Registration period was {} years ago", difference);
                            narrative = "Customer vehicle registration is " + Integer.toString(difference) + " years before hire start.";
                            res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                        } else {
                            VehicleClass customerVehicleClass = claim.getCustomer().getVehicleClass();
                            VehicleClass hireVehicleClass = claim.getVehicleHire().getVehicleClass();
                            if (customerVehicleClass.getName().substring(0,2).equals("SP") ) {
                               narrative = "The CHO's customer's vehicle is " + difference + " years old and vehicle class "
                                        + customerVehicleClass.getName() + ", please review the replacement vehicle class of "
                                        + hireVehicleClass.getName() + " on an individual basis as per the agreement in place.";
                                LOG.debug("Rule failed: {}", narrative);
                                res.setResult(RuleEvaluationResult.RULE_FAILED);
                            } else {
                                LOG.debug("Rule skipped: cusomers vehicle class is not Sports Performance.");
                                narrative = "Cannot compare non-sports performance vehicle.";
                                res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                            }
                        }
                    } else {
                        LOG.debug("Rule skipped: no hire start date available.");
                        narrative = "Customer hire start date not available.";
                        res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                    }
                }
            } else {
                LOG.debug("Rule skipped: Customer vehicle class is not specified.");
                narrative = "Customer vehicle class is not specified.";
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);
            }
        } else {
            LOG.debug("Rule not switched on.");
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
        return "086";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
