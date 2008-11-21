package scsbre.engine;

import java.util.*;

import scsbre.model.ClaimStatus;

public class RulesEngineResponse {

    private List<RuleEvaluationResult> results = new ArrayList<RuleEvaluationResult>();
    private ClaimStatus status = null;

    public void addRuleEvaulation(RuleEvaluationResult res) {
        results.add(res);
    }
    
    public List<RuleEvaluationResult> getResults(){
        
        return results;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setClaimStatus(ClaimStatus s) {
        status = s;
    }
}
