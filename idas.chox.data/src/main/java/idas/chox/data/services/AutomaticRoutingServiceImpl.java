package idas.chox.data.services;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.core.services.AutomaticRoutingService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomaticRoutingServiceImpl extends SecureDataService implements AutomaticRoutingService {

    static final Logger LOG = LoggerFactory.getLogger(AutomaticRoutingServiceImpl.class);

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId, int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRouting.class);
        if (insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }
        if (workgroupId > 0) {
            criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        }
        return findByCriteria(criteria);
    }

    public List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId, int workgroupId) {

       
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPrice.class);
        if (insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }
        if (workgroupId > 0) {
            criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        }
        criteria.addOrder(Order.asc("price"));
        return findByCriteria(criteria);
    }

    public List<AutomaticRouting> getAutomaticRoutings(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRouting.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        return findByCriteria(criteria);
    }

    public List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId) {

        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPrice.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("price"));
        return findByCriteria(criteria);


    }

    public boolean isWorkgroupInUseByAutomaticRouting(int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRouting.class);
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        if ((findByCriteria(criteria)).size() > 0) {
            return true;
        }
        return false;
    }

    public AutomaticRouting getAutomaticRouting(int insurerId, int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRouting.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        return (AutomaticRouting) getByCriteria(criteria);
    }

    public AutomaticRouting getAutomaticRouting(int automaticRoutingId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRouting.class);
        criteria.add(Restrictions.eq("id", automaticRoutingId));
        return (AutomaticRouting) getByCriteria(criteria);
    }

    public AutomaticRoutingPrice getAutomaticRoutingByPrice(int automaticRoutingId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPrice.class);
        criteria.add(Restrictions.eq("id", automaticRoutingId));
        criteria.addOrder(Order.asc("price"));
        return (AutomaticRoutingPrice) getByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveAutomaticRouting(AutomaticRouting automaticRouting) {
        save(automaticRouting);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting) {
        save(automaticRouting);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAutomaticRouting(AutomaticRouting automaticRouting) {
        delete(automaticRouting);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting) {
        
        delete(automaticRouting);
    }
}
