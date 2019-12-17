package idas.chox.data.services;

import com.google.common.collect.Maps;
import idas.chox.core.model.Claim;
import idas.chox.core.services.EventLogService;
import idas.chox.events.BaseActivityEvent;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventLogServiceImp extends SecureDataService implements EventLogService {
    public List<BaseActivityEvent> getEventLogByClaim(Claim claim) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BaseActivityEvent.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claim.getId()));
        criteria.addOrder(Order.desc("createdDate"));
        List<BaseActivityEvent> list = findByCriteria(criteria);
        // FIXME: improve the query, so that it only query once and get distinct results back
        list = list.stream().distinct().collect(Collectors.toList());
        return list;
    }

    public Map<String, String> getEventLogAttributesById(int eventLogId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(BaseActivityEvent.class);
        criteria.add(Restrictions.eq("id", eventLogId));
        List list = findByCriteria(criteria);

        if (!list.isEmpty()) {
            return ((BaseActivityEvent)list.get(0)).getAttributes();
        } else {
            return Maps.newHashMap();
        }
    }
}
