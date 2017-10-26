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
import idas.chox.core.services.NotificationService;
import idas.chox.core.services.HireMonitoringEcdService;
import idas.chox.data.notifications.EcdUpdatedNotification;
import idas.chox.data.notifications.NotificationType;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdServiceImpl extends SecureDataService implements HireMonitoringEcdService {
    private static final Logger LOG = LoggerFactory.getLogger(HireMonitoringEcdServiceImpl.class);

    private NotificationService notificationService;

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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveHireMonitoringEcd(HireMonitoringEcd object) {
        save(object);
    }

    @Override
    public Date getLatestHireMonitoringECDDate(Claim claim) {

        Date returnECD;
        List<HireMonitoringEcd> hireMonitoringEcds;
        
        Date originalEcd = claim.getCustomer().getInitialECD();
        try {
            hireMonitoringEcds = getHireMonitoringEcdsByClaimIdFilter(claim.getId(), false, "createdDate");
        } catch (Exception ex) {
            LOG.error("Exception thrown getting HireMonitoringEcdsByClaimIdFilter on claim {} (id={}): {}",
                    new Object[]{claim.getChorganisation(), claim.getId(), ex.getMessage()});
            hireMonitoringEcds = claim.getHireMonitoringEcds();
        }
        
        if (hireMonitoringEcds != null && hireMonitoringEcds.size() > 0) {
            returnECD = hireMonitoringEcds.get(0).getEcdDate();
        } else {
            returnECD = originalEcd;
        }

        return returnECD;
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void addNewHireMonitoringEcd(Claim claim, HireMonitoringEcd ecd) {
        
        claim.addHireMonitoringEcd(ecd);
        try {
            LOG.debug("Checking for ECD anomalies...");
            notificationService.checkForAnomalies(claim, NotificationType.EcdAnomalousNotification.getType());
        } catch (Exception ex) {
            LOG.error("Exception thrown adding notifications of type '{}' to claim={} [v{}]: {}", new Object[]{
                        NotificationType.EcdAnomalousNotification.getType(), claim.getId(), claim.getVersion(), ex.getMessage(), ex});
        }
        
        if (ecd.isUpdateInsurer() || claim.getInsurer().isAllowDefaultHMUpdates()) {
            try {
                notificationService.addNotification(claim, new EcdUpdatedNotification());
            } catch (Exception ex) {
                LOG.error("Exception thrown adding EcdUpdatedNotification to claim={} [v{}]: {}", new Object[]{
                            claim.getId(), claim.getVersion(), ex.getMessage(), ex});
            }
        }
    }
}
