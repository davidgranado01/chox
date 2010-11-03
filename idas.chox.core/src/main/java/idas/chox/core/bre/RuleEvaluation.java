package idas.chox.core.bre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleEvaluation {
    private static final Logger LOG = LoggerFactory.getLogger(RuleEvaluation.class);

    private RuleEvaluationResult result;
    private Boolean isVisibleToCHO;
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
    
    @Override
    public String toString(){
        if(result != null && relatedRule != null){
            
            String rtnVal = "";
            if(result == RuleEvaluationResult.RuleFailed){
                rtnVal+= "BRE Rule Failed - " + relatedRule.getNarrative();
//                LOG.info("Rule failed : id={}, narrative={}", relatedRule.getRuleId(), relatedRule.getNarrative());
            }
            else if(result == RuleEvaluationResult.RuleSkipped){
                rtnVal+= "BRE Rule Skipped - " + relatedRule.getNarrative();
//                LOG.info("Rule Skipped : id={}, narrative={}", relatedRule.getRuleId(), relatedRule.getNarrative());
            }
            else{
                rtnVal+= "BRE Rule Passed";
//                LOG.info("Rule Passed : id={}, narrative={}", relatedRule.getRuleId(), relatedRule.getNarrative());
            }
            return rtnVal;
        }
        return "result is null";
    }
}


