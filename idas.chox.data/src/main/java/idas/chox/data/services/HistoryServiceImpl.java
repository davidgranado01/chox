package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.History;
import idas.chox.core.services.HistoryService;

public class HistoryServiceImpl extends SecureDataService implements HistoryService {

    /*
     * isShowAll : true > SHOW ALL RECORDS WITH TYPE IS ERROR AND INFO
     * isShowAll : false > SHOW ALL RECORDS WITH TYPE IS ERROR ONLY
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */
    @Override
    public List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic) {

        List<History> histories;

        DetachedCriteria criteria = DetachedCriteria.forClass(History.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));

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


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void markHistoryAsOldByClaim(Claim claim) {
        List<History> histories = getHistoryByClaim(claim, true, false);
        
        for(History history : histories) {
            if (!history.getIsOld()) {
                history.setIsOld(true);
                save(history);
            }
        }
    }
}
