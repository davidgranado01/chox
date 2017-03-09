package idas.chox.core.bre;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;

public class RulesEngine {
    public static final Logger LOG = LoggerFactory.getLogger(RulesEngine.class);

    private List<IBusinessRule> businessRules;

    public RulesEngineResponse validate(Claim claim) {
        LOG.trace("Validating claim '{}'", claim.getChoReference());
        LOG.trace("Applying {} rules to claim", businessRules.size());
        RulesEngineResponse response = new RulesEngineResponse();
        RuleEvaluation ev;
        for (IBusinessRule businessRule : businessRules) {
            LOG.trace("Applying rule '{}' = {}", businessRule.getRuleId(), businessRule);
            try {
                ev = businessRule.applyToClaim(claim);
            } catch (Exception ex) {
                LOG.error("Error applying BRE rule with id={} : {}\n", new Object[]{businessRule.getRuleId(), ex.getMessage(), ex});
                continue;
            }
            response.addRuleEvaulation(ev);
            LOG.debug(businessRule.getNarrative() + " - " + ev.getResult() + " - " + businessRule.getRuleId());
        }
        return response;
    }

    public List<IBusinessRule> getBusinessRules() {
        return businessRules;
    }

    public void setBusinessRules(List<IBusinessRule> businessRules) {
        this.businessRules = businessRules;
    }
}
