package idas.chox.core.bre;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.ClaimStatus;

public class RulesEngineResponse {
    private static final Logger LOG = LoggerFactory.getLogger(RulesEngineResponse.class);

    private List<RuleEvaluation> results = new ArrayList<RuleEvaluation>();
    private String status = null;

    public void addRuleEvaulation(RuleEvaluation res) {
        results.add(res);
    }

    public List<RuleEvaluation> getResults() {
        return results;
    }

    public String getStatus(boolean isEngineersEnabled) {
        if (status == null) {

            boolean foundInvoiceDataCalculationIncorrect = false;
            boolean foundInvoiceInvoiceEscalated = false;
            boolean foundFailedRule = false;

            for (int i = 0; i < results.size(); i++) {
                RuleEvaluation rev = results.get(i);
                LOG.debug("Processing response from: {}", rev.toString());
                if (rev.getResult() == RuleEvaluationResult.RULE_FAILED) {
                    foundFailedRule = true;
                    LOG.debug("Rule failed: related rule=[]], statusAfterFailure={}", rev.getRelatedRule().getRuleId(), rev.getRelatedRule().getStatusAfterFailure(rev.getClaimType()));
                    if (ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT.equals(rev.getRelatedRule().getStatusAfterFailure(rev.getClaimType()))) {
                        foundInvoiceDataCalculationIncorrect = true;
                    }

                    if (ClaimStatus.INVOICE_ESCALATED.equals(rev.getRelatedRule().getStatusAfterFailure(rev.getClaimType()))) {
                        foundInvoiceInvoiceEscalated = true;
                    }
                }
            }

            if (foundFailedRule) {

                if (foundInvoiceDataCalculationIncorrect) {

                    status = ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;

                } else {

                    status = ClaimStatus.INVOICE_ESCALATED_TO_CH;

                    if (foundInvoiceInvoiceEscalated && isEngineersEnabled) {
                        status = ClaimStatus.INVOICE_ESCALATED;
                    }

                }

            } else {
                status = ClaimStatus.INVOICE_APPROVED_BY_BRE;
            }
        }
        LOG.debug("Returning status: {}", status);
        return status;
    }
}
