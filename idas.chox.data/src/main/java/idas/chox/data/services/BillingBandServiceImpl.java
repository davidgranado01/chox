package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.ChoBillingBand;
import idas.chox.core.model.InsurerBillingBand;
import idas.chox.core.services.BillingBandService;

/**
 *
 * @author john
 */
public class BillingBandServiceImpl extends SecureDataService implements BillingBandService {

    @Override
    public List<InsurerBillingBand> getInsurerBillingBands() {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerBillingBand.class);
        
        return findByCriteria(criteria);
    }

    @Override
    public List<InsurerBillingBand> getInsurerBillingBands(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerBillingBand.class);
        criteria.add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("bandName"));
        
        return findByCriteria(criteria);
    }

    @Override
    public List<ChoBillingBand> getChoBillingBands() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChoBillingBand.class);
        
        return findByCriteria(criteria);
    }

    @Override
    public List<ChoBillingBand> getChoBillingBands(int choId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChoBillingBand.class);
        criteria.add(Restrictions.eq("chorganisation.id", choId));
        criteria.addOrder(Order.asc("bandName"));
        
        return findByCriteria(criteria);
    }

    @Override
    public InsurerBillingBand getInsurerBillingBand(int id) {
        return (InsurerBillingBand) get(InsurerBillingBand.class, id);
    }

    @Override
    public ChoBillingBand getChoBillingBand(int id) {
        return (ChoBillingBand) get(ChoBillingBand.class, id);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void deleteBillingBand(InsurerBillingBand band) {
        delete(band);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void deleteBillingBand(ChoBillingBand band) {
        delete(band);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveBillingBand(InsurerBillingBand band) {
        save(band);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveBillingBand(ChoBillingBand band) {
        save(band);
    }
    
}
