/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.Util.DateHelper;
import chox.model.History;
import chox.model.Claim;
import scsbre.engine.*;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

public class HistoryServiceImpl extends DataService implements HistoryService {

    /*
     * isShowAll : true > SHOW ALL RECORDS WITH TYPE IS ERROR AND INFO
     * isShowAll : false > SHOW ALL RECORDS WITH TYPE IS ERROR ONLY
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */
    public List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic) {

        List histories = new ArrayList<History>();

        Criteria criteria = getCurrentSession().createCriteria(History.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));

        if (!isShowAll) {
            criteria.add(Restrictions.eq("type", "ERROR"));
        }

        // SHOW TRUE RECORD ONLY IF IT IS NOT PUBLIC
        if (isPublic) {
            criteria.add(Restrictions.eq("isPublic", true));
        }

        criteria.addOrder(Order.asc("claim.id"));
        criteria.addOrder(Order.asc("ruleId"));

        histories = criteria.list();

        return histories;
    }

    public List<History> getHistoryByClaimSortByDate(Claim claim, Boolean isShowAll, Boolean isPublic) {

        List histories = new ArrayList<History>();

        Criteria criteria = getCurrentSession().createCriteria(History.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));

        if (!isShowAll) {
            criteria.add(Restrictions.eq("type", "ERROR"));
        }

        // SHOW TRUE RECORD ONLY IF IT IS NOT PUBLIC
        if (isPublic) {
            criteria.add(Restrictions.eq("isPublic", true));
        }

        criteria.addOrder(Order.asc("createdDate"));

        histories = criteria.list();

        return histories;
    }

    public void logInvoiceValidationErrorMsg(RulesEngineResponse reponse, Claim claim) {

        List<RuleEvaluation> results = reponse.getResults();

        for (int iCount = 0; iCount < results.size(); iCount++) {

            RuleEvaluation rv = results.get(iCount);
            IBusinessRule rBusinessRule = rv.getRelatedRule();

            String sType = "INFO";
            if (rv.getResult() == RuleEvaluationResult.RuleFailed) {
                sType = "ERROR";
            }

            History history = new History();
            history.setProcessDate(DateHelper.getCurrentTimeStamp());
            history.setClaim(claim);
            history.setIsPublic(rv.getIsVisibleToCHO());
            history.setNarrative(rv.toString() + ':' + rv.getResult());
            history.setType(sType);
            history.setRuleId(rBusinessRule.getRuleId());
            history.setIsSystem(true);
            saveHistory(history);
        }
    }

    public Boolean saveHistory(History history) {

        Boolean bFlag = true;

        getCurrentSession().beginTransaction();

        try {
            getCurrentSession().saveOrUpdate(history);
            getCurrentSession().getTransaction().commit();
        } catch (Exception e) {
            getCurrentSession().getTransaction().rollback();
        }

        return bFlag;
    }
}
