package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.services.BrePenaltyBandService;

/**
 *
 * @author John
 */
public class BrePenaltyBandServiceImpl extends SecureDataService implements BrePenaltyBandService {

    @Override
    public List<BrePenaltyBand> getBrePenaltyBands(int breBandId) {
        DetachedCriteria brePenaltyBandCriteria = DetachedCriteria.forClass(BrePenaltyBand.class);
        brePenaltyBandCriteria.add(Restrictions.eq("breBand.id", breBandId));
        brePenaltyBandCriteria.addOrder(Order.asc("claimType"));
        brePenaltyBandCriteria.addOrder(Order.asc("startDate"));
        return findByCriteria(brePenaltyBandCriteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveBrePenaltyBand(BrePenaltyBand brePenaltyBand) {
        save(brePenaltyBand);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteBrePenaltyBand(BrePenaltyBand brePenaltyBand) {
        delete(brePenaltyBand);
    }

    @Override
    public BrePenaltyBand getBrePenaltyBand(int brePenaltyBandId) {
        return (BrePenaltyBand) get(BrePenaltyBand.class, brePenaltyBandId);
    }

    
}
