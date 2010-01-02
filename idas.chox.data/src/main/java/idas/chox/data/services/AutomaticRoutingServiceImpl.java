package idas.chox.data.services;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.services.AutomaticRoutingService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class AutomaticRoutingServiceImpl extends SecureDataService implements AutomaticRoutingService {

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRouting.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        return findByCriteria(criteria);
    }
}
