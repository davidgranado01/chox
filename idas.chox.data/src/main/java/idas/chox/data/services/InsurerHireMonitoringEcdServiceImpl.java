package idas.chox.data.services;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.InsurerHireMonitoringEcd;
import idas.chox.core.services.InsurerHireMonitoringEcdService;

/**
 *
 * @author Emmanuel
 */
public class InsurerHireMonitoringEcdServiceImpl extends SecureDataService implements InsurerHireMonitoringEcdService {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerHireMonitoringEcdServiceImpl.class);

    @Override
    public List<InsurerHireMonitoringEcd> getInsurerHireMonitoringEcdsByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerHireMonitoringEcd.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.asc("createdDate"));
        return findByCriteria(criteria);
    }

    @Override
    public List<InsurerHireMonitoringEcd> getInsurerHireMonitoringEcdsByClaimIdFilter(int claimId, boolean isAsc, String orderByField) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerHireMonitoringEcd.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        if (isAsc) {
            criteria.addOrder(Order.asc(orderByField));
        } else {
            criteria.addOrder(Order.desc(orderByField));
        }
        return findByCriteria(criteria);
    }

    @Override
    public InsurerHireMonitoringEcd getInsurerHireMonitoringEcd(int id) {
        return (InsurerHireMonitoringEcd) get(InsurerHireMonitoringEcd.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveInsurerHireMonitoringEcd(InsurerHireMonitoringEcd object) {
        save(object);
    }

    @Override
    public Date getLatestInsurerHireMonitoringECDDate(Claim claim) {

        Date returnECD;

        Date originalEcd = claim.getCustomer().getInitialECD();
        List<InsurerHireMonitoringEcd> hireMonitoringEcds = getInsurerHireMonitoringEcdsByClaimIdFilter(claim.getId(), false, "createdDate");

        if (hireMonitoringEcds.size() > 0) {
            returnECD = hireMonitoringEcds.get(0).getEcdDate();
        } else {
            returnECD = originalEcd;
        }

        return returnECD;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void addNewInsurerHireMonitoringEcd(Claim claim, InsurerHireMonitoringEcd ecd) {
        claim.addInsurerHireMonitoringEcd(ecd);
    }
}
