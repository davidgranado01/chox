/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.History;
import idas.chox.core.services.HistoryService;
import idas.chox.core.util.HistoryHelper;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class HistoryServiceImpl extends SecureDataService implements HistoryService {

    /*
     * isShowAll : true > SHOW ALL RECORDS WITH TYPE IS ERROR AND INFO
     * isShowAll : false > SHOW ALL RECORDS WITH TYPE IS ERROR ONLY
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */
    public List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic) {

        List histories = new ArrayList<History>();

        DetachedCriteria criteria = DetachedCriteria.forClass(History.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));

        // if (!isShowAll) {
        criteria.add(Restrictions.eq("type", "ERROR"));
        // }

        // SHOW TRUE RECORD ONLY IF IT IS NOT PUBLIC
        if (isPublic) {
            criteria.add(Restrictions.eq("isPublic", true));
        }

        criteria.addOrder(Order.asc("claim.id"));
        criteria.addOrder(Order.asc("ruleId"));

        histories = findByCriteria(criteria);

        return histories;
    }

    public List<History> getHistoryByClaimSortByDate(Claim claim, Boolean isShowAll, Boolean isPublic) {

        List histories = new ArrayList<History>();

        DetachedCriteria criteria = DetachedCriteria.forClass(History.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));

        //if (!isShowAll) {
        criteria.add(Restrictions.eq("type", "ERROR"));
        //}

        // SHOW TRUE RECORD ONLY IF IT IS NOT PUBLIC
        if (isPublic) {
            criteria.add(Restrictions.eq("isPublic", true));
        }

        criteria.addOrder(Order.asc("createdDate"));

        histories = findByCriteria(criteria);

        return histories;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void logInvoiceValidationErrorMsg(RulesEngineResponse reponse, Claim claim) {

        List<RuleEvaluation> results = reponse.getResults();

        for (int iCount = 0; iCount < results.size(); iCount++) {

            RuleEvaluation rv = results.get(iCount);

            /*
            IBusinessRule rBusinessRule = rv.getRelatedRule();
            String sType = "INFO";
            if (rv.getResult() == RuleEvaluationResult.RuleFailed) {
            sType = "ERROR";
            }

            History history = new History();
            history.setProcessDate(DateHelper.getCurrentTimeStamp());
            history.setClaim(claim);
            history.setIsPublic(rv.getIsVisibleToCHO());
            history.setNarrative(rv.toString());
            history.setType(sType);
            history.setRuleId(rBusinessRule.getRuleId());
            history.setIsSystem(true);
             */

            History history = HistoryHelper.createHistory(claim, rv);
            saveHistory(history);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Boolean saveHistory(History history) {
        Boolean bFlag = true;

        save(history);
        bFlag = true;

        return bFlag;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveHistories(List<History> histories) {

        for (History history : histories) {
            saveHistory(history);
        }

    }
}
