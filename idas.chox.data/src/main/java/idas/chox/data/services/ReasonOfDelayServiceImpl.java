package idas.chox.data.services;

import idas.chox.core.model.ReasonOfDelay;
import idas.chox.core.services.ReasonOfDelayService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ReasonOfDelayServiceImpl extends SecureDataService implements ReasonOfDelayService {

    @Override
    public ReasonOfDelay getReasonOfDelay(int id) {
        return (ReasonOfDelay) get(ReasonOfDelay.class, id);
    }

    @Override
    public List<ReasonOfDelay> getReasonOfDelay() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfDelay.class);
        criteria.add(Restrictions.eq("status", true));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public List<ReasonOfDelay> getAllReasonOfDelay() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReasonOfDelay.class);
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }
}
