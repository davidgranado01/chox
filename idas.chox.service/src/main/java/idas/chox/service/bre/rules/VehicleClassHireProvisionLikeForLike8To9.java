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
public class VehicleClassHireProvisionLikeForLike8To9 implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassHireProvisionLikeForLike8To9.class);
    private String narrative = "The CHO's customer's vehicle is [x] years old and vehicle class [y], the replacement vehicle class of [z] is not acceptable as the replacement vehicle class should be two classes less than the CHO's customer's vehicle based on the age of the vehicle and the agreement in place.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        LOG.debug("Applying rule 'VehicleClassHireProvisionLikeForLike8To9' to claim {}.", claim.getChoReference());

        if (claim.getBreBand().isVehicleClassHireProvisionLikeForLike8To9()) {
            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {
                Date firstRegistration = claim.getCustomer().getHpiFirstRegistration();
                if (firstRegistration == null) {
                    LOG.debug("Rule skipped: no first registration date available.");
                    narrative = "Customer vehicle registration date not available.";
                    res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                } else {

                    if (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) {
                        Date hireStart = claim.getVehicleHire().getHireStart();
                        double difference = DateHelper.differenceInYears(hireStart, firstRegistration);
                        LOG.debug("Difference in years between {} and {} is " + Double.toString(difference), hireStart, firstRegistration);
                        if (difference < 8.0 || difference > 9.0) {
                            LOG.debug("Rule skipped: Registration period was {} years ago", difference);
                            narrative = "Customer vehicle registration is " + (new Double(difference)).intValue() + " years before hire start.";
                            res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                        } else {
                            VehicleClass customerVehicleClass = claim.getCustomer().getVehicleClass();
                            VehicleClass hireVehicleClass = claim.getVehicleHire().getVehicleClass();
                            try {
                                int classDifference = VehicleClass.classPDifference(customerVehicleClass, hireVehicleClass);
                                if (classDifference <= -1) {
                                    res.setResult(RuleEvaluationResult.RULE_PASSED);
                                    LOG.debug("Rule passed: Vehicle class allocated for hire ok for customer vehicle between 6 and 8 years old.");
                                    narrative = "";
                                } else {
                                    res.setResult(RuleEvaluationResult.RULE_FAILED);
                                    narrative = "The CHO's customer's vehicle is " + (int)difference + " years old and vehicle class "
                                            + customerVehicleClass.getName() + ", the replacement vehicle class of "
                                            + hireVehicleClass.getName() + " is not acceptable as the replacement vehicle class should be two classes less than the CHO's customer's vehicle based on the age of the vehicle and the agreement in place.";
                                    LOG.debug("Rule failed: {}", narrative);
                                }
                            } catch (Exception ex) {
                                LOG.debug("Rule skipped: cannot compare vehicle classes: {}", ex.getMessage());
                                narrative = ex.getMessage();
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
        return "049";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
