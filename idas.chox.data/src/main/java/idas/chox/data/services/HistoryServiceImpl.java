package idas.chox.data.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.BreRules;
import idas.chox.core.model.Claim;
import idas.chox.core.services.HistoryService;

public class HistoryServiceImpl extends SecureDataService implements HistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryServiceImpl.class);

    @Override
    public List<BreRules> getBreRuleFailuresByClaim(Claim claim) {
        List<BreRules> breRules;

        Set<String> breRuleFailureIds = new HashSet<>();
        claim.getHistories().stream().filter((history) -> ("ERROR".equals(history.getType()))).forEachOrdered((history) -> {
            breRuleFailureIds.add(history.getRuleId());
        });

        if (!breRuleFailureIds.isEmpty()) {
            DetachedCriteria criteria = DetachedCriteria.forClass(BreRules.class);
            criteria.add(Restrictions.in("ruleName", breRuleFailureIds.toArray()));
            criteria.addOrder(Order.asc("ruleName"));

            breRules = findByCriteria(criteria);
        } else {
            breRules = new ArrayList<>(0);
        }

        return breRules;
    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    @Override
    public void markHistoryAsOldByClaim(Claim claim) {
        if (claim.getId() != null && claim.getHistories() != null) {
            claim.getHistories().stream().filter((history) -> (!history.getIsOld())).forEachOrdered((history) -> {
                history.setIsOld(true);
            });
        }
    }
}
