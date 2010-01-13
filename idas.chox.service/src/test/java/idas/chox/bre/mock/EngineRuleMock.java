/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.bre.mock;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.model.Claim;


public class EngineRuleMock implements IBusinessRule {

    protected String narrative = "";
    protected String ruleId = "";
    protected String statusAfterFailure;

    public EngineRuleMock(String ruleId, String statusAfterFailure){
        setRuleId(ruleId);
        setStatusAfterFailure(statusAfterFailure);
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public void setStatusAfterFailure(String statusAfterFailure) {
        this.statusAfterFailure = statusAfterFailure;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return ruleId;
    }  

    @Override
    public String getStatusAfterFailure() {
        return statusAfterFailure;
    }

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
