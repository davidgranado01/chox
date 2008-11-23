package scsbre.engine;

public class RuleEvaluation {

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
            String rtnVal = "Applying rule " + relatedRule.getRuleId() + " : ";
            if(result == RuleEvaluationResult.RuleFailed){
                rtnVal+= "FAILED : " + relatedRule.getNarrative();
            }
            else if(result == RuleEvaluationResult.RuleSkipped){
                rtnVal+= "SKIPPED : " + relatedRule.getNarrative(); 
            }
            else{
                rtnVal+= "PASSED.";
            }
            return rtnVal;
        }
        return "result is null";
    }
}


