package idas.chox.data.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BreRules;
import idas.chox.core.model.Claim;
import idas.chox.core.model.History;
import idas.chox.core.services.HistoryService;

public class HistoryServiceImpl extends SecureDataService implements HistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(HistoryServiceImpl.class);

    /*
     * isShowAll : true > SHOW ALL RECORDS WITH TYPE IS ERROR AND INFO
     * isShowAll : false > SHOW ALL RECORDS WITH TYPE IS ERROR ONLY
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */
    @Override
    public List<History> getHistoryByClaim(int claimId, Boolean isShowAll, Boolean isPublic) {

        List<History> histories;

        DetachedCriteria criteria = DetachedCriteria.forClass(History.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));

        // if (!isShowAll) {
        criteria.add(Restrictions.eq("type", "ERROR"));
        // }

        // SHOW TRUE RECORD ONLY IF IT IS NOT PUBLIC
        if (isPublic) {
            criteria.add(Restrictions.eq("isPublic", true));
        }

        criteria.addOrder(Order.desc("processDate"));
        criteria.addOrder(Order.asc("ruleId"));

        histories = findByCriteria(criteria);

        return histories;
    }

    @Override
    public List<BreRules> getBreRuleFailuresByClaimId(int claimId) {
        List<BreRules> breRules;
        List<History> breRuleFailures = getHistoryByClaim(claimId, false, false);

        Set<String> breRuleFailureIds = new HashSet<>(breRuleFailures.size());
        for (History history : breRuleFailures) {
            breRuleFailureIds.add(history.getRuleId());
        }

        DetachedCriteria criteria = DetachedCriteria.forClass(BreRules.class);
        criteria.add(Restrictions.in("ruleName", breRuleFailureIds.toArray()));
        criteria.addOrder(Order.asc("ruleName"));

        breRules = findByCriteria(criteria);
        return breRules;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void markHistoryAsOldByClaim(Claim claim) {
        List<History> histories = getHistoryByClaim(claim.getId(), true, false);
        
        for(History history : histories) {
            if (!history.getIsOld()) {
                history.setIsOld(true);
                save(history);
            }
        }
    }
}
