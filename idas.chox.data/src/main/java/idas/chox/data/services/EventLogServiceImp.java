package idas.chox.data.services;

import idas.chox.core.model.Claim;
import idas.chox.core.services.EventLogService;
import idas.chox.events.BaseActivityEvent;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class EventLogServiceImp extends SecureDataService implements EventLogService {
    public List<BaseActivityEvent> getEventLogByClaim(Claim claim) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BaseActivityEvent.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
        criteria.addOrder(Order.asc("createdDate"));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }
}
