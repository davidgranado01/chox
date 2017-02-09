package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.ClaimMatchingBandService;

/**
 *
 * @author john
 */
public class ClaimMatchingBandServiceImpl extends SecureDataService implements ClaimMatchingBandService {

    @Override
    public List<ClaimMatchingBand> getClaimMatchingBands(int breBandId) {
        DetachedCriteria brePenaltyBandCriteria = DetachedCriteria.forClass(ClaimMatchingBand.class);
        brePenaltyBandCriteria.add(Restrictions.eq("breBand.id", breBandId));
        brePenaltyBandCriteria.addOrder(Order.asc("claimType"));
        return findByCriteria(brePenaltyBandCriteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveClaimMatchingBand(ClaimMatchingBand claimMatchingBand) {
        save(claimMatchingBand);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteClaimMatchingBand(ClaimMatchingBand claimMatchingBand) {
        delete(claimMatchingBand);
    }

    @Override
    public ClaimMatchingBand getClaimMatchingBand(int claimMatchingBandId) {
        return (ClaimMatchingBand) get(ClaimMatchingBand.class, claimMatchingBandId);
    }

    @Override
    public ClaimMatchingBand getClaimMatchingBand(int breBandId, ClaimType claimType, String vehicleClass) {
        DetachedCriteria claimMatchingBandCriteria = DetachedCriteria.forClass(ClaimMatchingBand.class);
        claimMatchingBandCriteria.add(Restrictions.eq("breBand.id", breBandId));
        claimMatchingBandCriteria.add(Restrictions.eq("claimType", claimType));
        if (vehicleClass.startsWith("CM")){claimMatchingBandCriteria.add(Restrictions.eq("cmClass", true));}
        else if (vehicleClass.startsWith("CP")){claimMatchingBandCriteria.add(Restrictions.eq("cpClass", true));}
        else if (vehicleClass.startsWith("CS")){claimMatchingBandCriteria.add(Restrictions.eq("csClass", true));}
        else if (vehicleClass.startsWith("CV")){claimMatchingBandCriteria.add(Restrictions.eq("cvClass", true));}
        else if (vehicleClass.startsWith("NT")){claimMatchingBandCriteria.add(Restrictions.eq("ntClass", true));}
        else if (vehicleClass.startsWith("PT")){claimMatchingBandCriteria.add(Restrictions.eq("ptClass", true));}
        else if (vehicleClass.startsWith("PV")){claimMatchingBandCriteria.add(Restrictions.eq("pvClass", true));}
        else if (vehicleClass.startsWith("RV")){claimMatchingBandCriteria.add(Restrictions.eq("rvClass", true));}
        else if (vehicleClass.startsWith("SP")){claimMatchingBandCriteria.add(Restrictions.eq("spClass", true));}
        else if (vehicleClass.startsWith("B")){claimMatchingBandCriteria.add(Restrictions.eq("bClass", true));}
        else if (vehicleClass.startsWith("F")){claimMatchingBandCriteria.add(Restrictions.eq("fClass", true));}
        else if (vehicleClass.startsWith("M")){claimMatchingBandCriteria.add(Restrictions.eq("mClass", true));}
        else if (vehicleClass.startsWith("P")){claimMatchingBandCriteria.add(Restrictions.eq("pClass", true));}
        else if (vehicleClass.startsWith("S")){claimMatchingBandCriteria.add(Restrictions.eq("sClass", true));}
        else if (vehicleClass.startsWith("T")){claimMatchingBandCriteria.add(Restrictions.eq("tClass", true));}
        else if (vehicleClass.startsWith("UNATTACHED")){claimMatchingBandCriteria.add(Restrictions.eq("uClass", true));}
        claimMatchingBandCriteria.addOrder(Order.desc("liabilityPercentage"));
        List<ClaimMatchingBand> results = findByCriteria(claimMatchingBandCriteria);
        return results.isEmpty()? null : (ClaimMatchingBand)results.get(0);
    }
    
    @Override
    public List<ClaimMatchingBand> getClaimMatchingBands(int breBandId, ClaimType claimType, String vehicleClass) {
        DetachedCriteria claimMatchingBandCriteria = DetachedCriteria.forClass(ClaimMatchingBand.class);
        claimMatchingBandCriteria.add(Restrictions.eq("breBand.id", breBandId));
        claimMatchingBandCriteria.add(Restrictions.eq("claimType", claimType));
        if (vehicleClass.startsWith("CM")){claimMatchingBandCriteria.add(Restrictions.eq("cmClass", true));}
        else if (vehicleClass.startsWith("CP")){claimMatchingBandCriteria.add(Restrictions.eq("cpClass", true));}
        else if (vehicleClass.startsWith("CS")){claimMatchingBandCriteria.add(Restrictions.eq("csClass", true));}
        else if (vehicleClass.startsWith("CV")){claimMatchingBandCriteria.add(Restrictions.eq("cvClass", true));}
        else if (vehicleClass.startsWith("NT")){claimMatchingBandCriteria.add(Restrictions.eq("ntClass", true));}
        else if (vehicleClass.startsWith("PT")){claimMatchingBandCriteria.add(Restrictions.eq("ptClass", true));}
        else if (vehicleClass.startsWith("PV")){claimMatchingBandCriteria.add(Restrictions.eq("pvClass", true));}
        else if (vehicleClass.startsWith("RV")){claimMatchingBandCriteria.add(Restrictions.eq("rvClass", true));}
        else if (vehicleClass.startsWith("SP")){claimMatchingBandCriteria.add(Restrictions.eq("spClass", true));}
        else if (vehicleClass.startsWith("B")){claimMatchingBandCriteria.add(Restrictions.eq("bClass", true));}
        else if (vehicleClass.startsWith("F")){claimMatchingBandCriteria.add(Restrictions.eq("fClass", true));}
        else if (vehicleClass.startsWith("M")){claimMatchingBandCriteria.add(Restrictions.eq("mClass", true));}
        else if (vehicleClass.startsWith("P")){claimMatchingBandCriteria.add(Restrictions.eq("pClass", true));}
        else if (vehicleClass.startsWith("S")){claimMatchingBandCriteria.add(Restrictions.eq("sClass", true));}
        else if (vehicleClass.startsWith("T")){claimMatchingBandCriteria.add(Restrictions.eq("tClass", true));}
        else if (vehicleClass.startsWith("UNATTACHED")){claimMatchingBandCriteria.add(Restrictions.eq("uClass", true));}
        claimMatchingBandCriteria.addOrder(Order.desc("liabilityPercentage"));
        return findByCriteria(claimMatchingBandCriteria);
    }
    
}
