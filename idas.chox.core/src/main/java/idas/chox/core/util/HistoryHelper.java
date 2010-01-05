/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.util;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.History;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author Carlson
 */
public class HistoryHelper {

    public static History createHistory(Claim claim, RuleEvaluation rv) {

        String sType = "INFO";
        if (rv.getResult() == RuleEvaluationResult.RuleFailed) {
            sType = "ERROR";
        }

        IBusinessRule rBusinessRule = rv.getRelatedRule();

        History history = new History();

        history.setProcessDate(DateHelper.getCurrentDateTime());
        history.setClaim(claim);
        history.setIsPublic(rv.getIsVisibleToCHO());
        history.setNarrative(rv.toString());
        history.setType(sType);
        history.setRuleId(rBusinessRule.getRuleId());
        history.setIsSystem(true);

        return history;

    }
}
