package idas.chox.core.bre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.ClaimType;

public class RuleEvaluation {
    private static final Logger LOG = LoggerFactory.getLogger(RuleEvaluation.class);

    private RuleEvaluationResult result;
    private Boolean isVisibleToCHO;
    private ClaimType claimType;
    private IBusinessRule relatedRule;


    public boolean getIsVisibleToCHO() {
        return isVisibleToCHO;
    }

    public void setIsVisibleToCHO(boolean vtcho) {
        isVisibleToCHO = vtcho;
    }

    public RuleEvaluationResult getResult() {
        return result;
    }

    public void setResult(RuleEvaluationResult result) {
        this.result = result;
    }

    public IBusinessRule getRelatedRule() {
        return relatedRule;
    }

    public void setRelatedRule(IBusinessRule relatedRule) {
        this.relatedRule = relatedRule;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    @Override
    public String toString(){
        if(result != null && relatedRule != null){
            
            String rtnVal = "";
            if(result == RuleEvaluationResult.RULE_FAILED){
                rtnVal+= "BRE Rule Failed - " + relatedRule.getNarrative();
            }
            else if(result == RuleEvaluationResult.RULE_SKIPPED){
                rtnVal+= "BRE Rule Skipped - " + relatedRule.getNarrative();
            }
            else{
                rtnVal+= "BRE Rule Passed";
            }
            return rtnVal;
        }
        return "result is null";
    }
}


