package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;
import java.util.Date;

/**
 *
 * @author John
 */
public class VehicleClassHireProvisionLikeForLike6To8 implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleClassHireProvisionLikeForLike6To8.class);
    private String narrative = "The CHO's customer's vehicle is [x] years old and vehicle class [y], the replacement vehicle class of [z] is not acceptable as the replacement vehicle class should be one class less than the CHO's customer's vehicle based on the age of the vehicle and the agreement in place.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        LOG.debug("Applying rule 'VehicleClassHireProvisionLikeForLike6To8' to claim {}.", claim.getChoReference());

        if (claim.getBreBand().isVehicleClassHireProvisionLikeForLike6To8()) {

            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {
                Date firstRegistration = claim.getCustomer().getHpiFirstRegistration();
                if (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) {
                    VehicleClass customerVehicleClass = claim.getCustomer().getVehicleClass();
                    if (VehicleClass.isPClass(customerVehicleClass.getName())) {
                        VehicleClass hireVehicleClass = claim.getVehicleHire().getVehicleClass();
                        if (VehicleClass.isPOrSClass(hireVehicleClass.getName())) {
                            Date hireStart = claim.getVehicleHire().getHireStart();
                            if (firstRegistration != null) {
                                double difference = DateHelper.DifferenceInYears(hireStart, firstRegistration);
                                if (difference >= 6.0 && difference < 8.0) {

                                    if (VehicleClass.classPDifference(customerVehicleClass, hireVehicleClass) <= -1) {
                                        res.setResult(RuleEvaluationResult.RulePassed);
                                        LOG.debug("Rule passed: Vehicle class allocated for hire ok for customer vehicle between 6 and 8 years old.");
                                        narrative = "";
                                    } else {
                                        res.setResult(RuleEvaluationResult.RuleFailed);
                                        narrative = "The CHO's customer's vehicle is " + difference + "years old and vehicle class "
                                                + customerVehicleClass.getName() + ", the replacement vehicle class of "
                                                + hireVehicleClass.getName() + " is not acceptable as the replacement vehicle class should be one class less than the CHO's customer's vehicle based on the age of the vehicle and the agreement in place.";
                                        LOG.debug("Rule failed: {}", narrative);
                                    }
                                } else {
                                    LOG.debug("Rule skipped: Registration period was {} years ago");
                                    narrative = "Customer vehicle registration date not available.";
                                    res.setResult(RuleEvaluationResult.RuleSkipped);
                                }
                            } else {
                                LOG.debug("Rule skipped: no first registration date available.");
                                narrative = "Customer vehicle registration date not available.";
                                res.setResult(RuleEvaluationResult.RuleSkipped);
                            }
                        } else {
                            LOG.debug("Rule skipped: hire vehicle class is not prestige (p-class) or standard (s-class).");
                            narrative = "Customer vehicle not prestige.";
                            res.setResult(RuleEvaluationResult.RuleSkipped);
                        }
                    } else {
                        LOG.debug("Rule skipped: customer vehicle class is not prestige (p-class).");
                        narrative = "Customer vehicle not prestige.";
                        res.setResult(RuleEvaluationResult.RuleSkipped);
                    }
                } else {
                    LOG.debug("Rule skipped: no hire start date available.");
                    narrative = "Customer hire start date not available.";
                    res.setResult(RuleEvaluationResult.RuleSkipped);
                }
            } else {
                LOG.debug("Rule skipped: Customer vehicle class is not specified.");
                narrative = "Customer vehicle class is not specified.";
                res.setResult(RuleEvaluationResult.RuleSkipped);
            }

        } else {
            LOG.debug("Rule not switched on.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);
        }

        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "045";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
