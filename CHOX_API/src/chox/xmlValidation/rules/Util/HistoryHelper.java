/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.xmlValidation.rules.Util;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.History;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;

/**
 *
 * @author Carlson
 */
public class HistoryHelper {
    
    public static History createHistory(Claim claim, RuleEvaluation rv){
        
        String sType = "INFO";
        if (rv.getResult() == RuleEvaluationResult.RuleFailed) {
            sType = "ERROR";
        }
        
        IBusinessRule rBusinessRule = rv.getRelatedRule();
        
        History history = new History();
        
        history.setProcessDate(DateHelper.getCurrentTimeStamp());
        history.setClaim(claim);
        history.setIsPublic(rv.getIsVisibleToCHO());
        history.setNarrative(rv.toString());
        history.setType(sType);
        history.setRuleId(rBusinessRule.getRuleId());
        history.setIsSystem(true);
            
        return history;
        
    }

}
