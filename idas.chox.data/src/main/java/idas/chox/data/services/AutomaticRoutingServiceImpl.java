package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.AutomaticRoutingCho;
import idas.chox.core.model.AutomaticRoutingPolicy;
import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.core.services.AutomaticRoutingService;

public class AutomaticRoutingServiceImpl extends SecureDataService implements AutomaticRoutingService {
    static final Logger LOG = LoggerFactory.getLogger(AutomaticRoutingServiceImpl.class);

    
    @Override
    public List<AutomaticRoutingPolicy> getAutomaticRoutingsByPolicy(int insurerId, int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPolicy.class);
        if (insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }
        if (workgroupId > 0) {
            criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        }
        return findByCriteria(criteria);
    }

    
    @Override
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

    
    @Override
    public List<AutomaticRoutingCho> getAutomaticRoutingsByCho(int insurerId, int choId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingCho.class);
        if (choId > 0) {
            criteria.add(Restrictions.eq("chorganisation.id", choId));
        }
        if (insurerId > 0) {
            criteria.createAlias("workgroup", "workgroup");
            criteria.createAlias("workgroup.insurer", "insurer");
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }
        return findByCriteria(criteria);
    }

    
    @Override
    public List<AutomaticRoutingPolicy> getAutomaticRoutingsByPolicy(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPolicy.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        return findByCriteria(criteria);
    }

    
    @Override
    public List<AutomaticRoutingPrice> getAutomaticRoutingsByPrice(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPrice.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("price"));
        return findByCriteria(criteria);


    }

    
    @Override
    public boolean isWorkgroupInUseByAutomaticRouting(int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPolicy.class);
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        return (findByCriteria(criteria)).size() > 0;
    }

    
    @Override
    public AutomaticRoutingPolicy getAutomaticRouting(int insurerId, int workgroupId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPolicy.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.add(Restrictions.eq("workgroup.id", workgroupId));
        return (AutomaticRoutingPolicy) getByCriteria(criteria);
    }

    
    @Override
    public AutomaticRoutingPolicy getAutomaticRouting(int automaticRoutingId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPolicy.class);
        criteria.add(Restrictions.eq("id", automaticRoutingId));
        return (AutomaticRoutingPolicy) getByCriteria(criteria);
    }

    
    @Override
    public AutomaticRoutingPrice getAutomaticRoutingByPrice(int automaticRoutingId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(AutomaticRoutingPrice.class);
        criteria.add(Restrictions.eq("id", automaticRoutingId));
        criteria.addOrder(Order.asc("price"));
        return (AutomaticRoutingPrice) getByCriteria(criteria);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveAutomaticRouting(AutomaticRoutingPolicy automaticRouting) {
        save(automaticRouting);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting) {
        save(automaticRouting);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteAutomaticRouting(AutomaticRoutingPolicy automaticRouting) {
        delete(automaticRouting);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteAutomaticRoutingByPrice(AutomaticRoutingPrice automaticRouting) {
        
        delete(automaticRouting);
    }
}
