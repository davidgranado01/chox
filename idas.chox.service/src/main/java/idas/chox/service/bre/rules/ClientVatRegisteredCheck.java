package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class ClientVatRegisteredCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(ClientVatRegisteredCheck.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'clientVatRegisteredCheck' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isClientVatRegisteredCheck() && claim.getHireMonitoringDetail() != null
                && claim.getHireMonitoringDetail().getClientVatRegistered() != null
                && claim.getHireMonitoringDetail().getClientVatRegistered()) {


            LOG.debug("Applying 'clientVatRegisteredCheck' Business Rule to claim {}.", claim.getChoReference());

            boolean chargingHireVat = false;
            boolean chargingRepairVat = false;
            boolean chargingStorageRecoveryVat = false;
            
            if (claim.getInvoice().getHireVat() != null && claim.getInvoice().getHireVat().compareTo(BigDecimal.ZERO) > 0) {
                chargingHireVat = true;
            }
            if (claim.getInvoice().getRepairVat() != null && claim.getInvoice().getRepairVat().compareTo(BigDecimal.ZERO) > 0) {
                chargingRepairVat = true;
            }
            if (claim.getInvoice().getStorageRecoveryVat()!= null && claim.getInvoice().getStorageRecoveryVat().compareTo(BigDecimal.ZERO) > 0) {
                chargingStorageRecoveryVat = true;
            }
            
            boolean success = !(chargingHireVat || chargingRepairVat || chargingStorageRecoveryVat);

 
            if (!success) {
                String vatCharges = null;
                if (chargingHireVat) {
                    vatCharges = "hire";
                }
                if (chargingRepairVat && vatCharges == null) {
                    vatCharges = "repair";
                } else if (chargingRepairVat) {
                    vatCharges = "hire, repair";
                }
                if (chargingStorageRecoveryVat && vatCharges == null) {
                    vatCharges = "storage recovery";
                } else if (chargingRepairVat) {
                    vatCharges = vatCharges + ", storage recovery";
                }
                
                narrative = "The CHO is charging VAT for the " + vatCharges + " when the CHO has indicated that their customer is VAT registered, please review.";

            } else {
                narrative = "";
                LOG.debug("'clientVatRegisteredCheck' Business Rule Passed.");
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {
            LOG.debug("'clientVatRegisteredCheck' Business Rule Skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);
        }

        LOG.debug("'clientVatRegisteredCheck' Business Rule Finished.");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "089";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
