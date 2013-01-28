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
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.NotificationService;
import idas.chox.core.services.HireMonitoringEcdService;
import idas.chox.data.notifications.NotificationType;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdServiceImpl extends SecureDataService implements HireMonitoringEcdService {
    private static final Logger LOG = LoggerFactory.getLogger(HireMonitoringEcdServiceImpl.class);

    private ClaimService claimService;
    private NotificationService notificationService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimId(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(HireMonitoringEcd.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.asc("createdDate"));
        return findByCriteria(criteria);
    }

    @Override
    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimIdFilter(int claimId, boolean isAsc, String orderByField) {
        DetachedCriteria criteria = DetachedCriteria.forClass(HireMonitoringEcd.class);
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        if (isAsc) {
            criteria.addOrder(Order.asc(orderByField));
        } else {
            criteria.addOrder(Order.desc(orderByField));
        }
        return findByCriteria(criteria);
    }

    @Override
    public HireMonitoringEcd getHireMonitoringEcd(int id) {
        return (HireMonitoringEcd) get(HireMonitoringEcd.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveHireMonitoringEcd(HireMonitoringEcd object) {
        save(object);
    }

    @Override
    public Date getLatestHireMonitoringECDDate(Claim claim) {

        Date returnECD;

        Date originalEcd = claim.getCustomer().getInitialECD();
        List<HireMonitoringEcd> hireMonitoringEcds = getHireMonitoringEcdsByClaimIdFilter(claim.getId(), false, "createdDate");

        if (hireMonitoringEcds.size() > 0) {
            returnECD = hireMonitoringEcds.get(0).getEcdDate();
        } else {
            returnECD = originalEcd;
        }

        return returnECD;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void addNewHireMonitoringEcd(Claim claim, HireMonitoringEcd ecd) {
        
        claim.addHireMonitoringEcd(ecd);
        try {
            LOG.debug("Checking for ECD anomalies...");
            notificationService.checkForAnomalies(claim, NotificationType.EcdAnomalousNotification.getType());
        } catch (Exception ex) {
            LOG.error("Exception thrown adding notifications of type '{}' to claim={}: {}", new Object[]{
                        NotificationType.EcdAnomalousNotification.getType(), claim.getId(), ex.getMessage()});
        }
    }

}
