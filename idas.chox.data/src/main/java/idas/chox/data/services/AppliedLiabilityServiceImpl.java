package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BreAppliedLiability;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.AppliedLiabilityService;

/**
 *
 * @author john
 */
public class AppliedLiabilityServiceImpl extends SecureDataService implements AppliedLiabilityService {

    @Override
    public List<BreAppliedLiability> getAppliedLiabilities(int breBandId) {
        DetachedCriteria brePenaltyBandCriteria = DetachedCriteria.forClass(BreAppliedLiability.class);
        brePenaltyBandCriteria.add(Restrictions.eq("breBand.id", breBandId));
        brePenaltyBandCriteria.addOrder(Order.asc("claimType"));
        return findByCriteria(brePenaltyBandCriteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveAppliedLiability(BreAppliedLiability appliedLiability) {
        save(appliedLiability);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteAppliedLiability(BreAppliedLiability appliedLiability) {
        delete(appliedLiability);
    }

    @Override
    public BreAppliedLiability getAppliedLiability(int appliedLiabilityId) {
        return (BreAppliedLiability) get(BreAppliedLiability.class, appliedLiabilityId);
    }

    @Override
    public BreAppliedLiability getAppliedLiability(int breBandId, ClaimType claimType) {
        if (claimType == ClaimType.INSURER_CLAIM) {
            claimType = ClaimType.INSURER_UPLOAD;
        }
        DetachedCriteria appliedLiabilityCriteria = DetachedCriteria.forClass(BreAppliedLiability.class);
        appliedLiabilityCriteria.add(Restrictions.eq("breBand.id", breBandId));
        appliedLiabilityCriteria.add(Restrictions.eq("claimType", claimType));
        List<BreAppliedLiability> results = findByCriteria(appliedLiabilityCriteria);
        return results.isEmpty()? null : (BreAppliedLiability)results.get(0);
    }    
}
