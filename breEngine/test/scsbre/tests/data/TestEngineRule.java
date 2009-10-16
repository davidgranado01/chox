/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.tests.data;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;


public class TestEngineRule implements IBusinessRule {

    protected String narrative = "";
    protected String ruleId = "";
    protected ClaimStatus statusAfterFailure;

    public TestEngineRule(String ruleId, ClaimStatus statusAfterFailure){
        setRuleId(ruleId);
        setStatusAfterFailure(statusAfterFailure);
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public void setStatusAfterFailure(ClaimStatus statusAfterFailure) {
        this.statusAfterFailure = statusAfterFailure;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return ruleId;
    }

    public ClaimStatus getStatusAfterFailure() {
        return statusAfterFailure;
    }

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
