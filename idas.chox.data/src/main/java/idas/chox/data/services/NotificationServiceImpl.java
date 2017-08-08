package idas.chox.data.services;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;
import idas.chox.data.notifications.NotificationType;
import idas.chox.core.notifications.AnomalousCheck;
import idas.chox.core.services.NotificationService;

/**
 *
 * @author John
 */
public class NotificationServiceImpl extends SecureDataService implements NotificationService, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void addNotification(Claim claim, Notification notification) {
        LOG.debug("Adding notification to claim with id={}", claim.getId(), notification.getType());
        try {
            notification.setClaim(claim);
            Notification n = new Notification(notification);
            claim.addNotification(n);
            this.save(n);
            LOG.debug("Added notification to claim '{}': {}", claim.getChoReference(), notification.getType());
        } catch (Exception ex) {
            LOG.error("Error thrown adding notification: {}", ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void checkForAnomalies(Claim claim, String type) {
        LOG.debug("Adding '{}' notifications to claim with id={}", type, claim.getId());
        // Remove any notifications previously added by the anomalousChecks

        AnomalousCheck anomalyChecker = NotificationType.getAnomalyCheckerForNotification(type);
        if (anomalyChecker == null) {
            LOG.error("Unknown anomaly type found for cliam '{}': {}", claim.getChoReference(), type);
            return;
        }


        if (anomalyChecker.isRefreshRequired()) {
            LOG.debug("    Removing existing unacknowledged notifications of same type on claim '{}': {}", claim.getChoReference(), type);
            try {
                removeUnacknowledgedNotificationByType(claim.getId(), type);
            } catch (Exception ex) {
                LOG.error("Exception thrown removing notification: {}", ex.getMessage());
            }
        }

        if (anomalyChecker.check(claim)) {
            Notification notification = anomalyChecker.buildNotification();
            LOG.debug("    Adding notification on claim '{}': {}", claim.getChoReference(), notification.getType());
            addNotification(claim, notification);
        } else {
            LOG.debug("No notifications to be added to claim '{}'.", claim.getChoReference());
        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void acknowledgeAllInsurerNotifications(Integer claimId) {
        LOG.debug("Acknowledging all insurer notifications on claim with id={}", claimId);
        List<Notification> notifications = getNotifications(claimId);
        for (Notification notification : notifications) {
            if (NotificationType.getNotificationType(notification.getType()).isInsurerType()) {
                LOG.debug("Acknowledging Insurer notification on claim={}: {}", claimId, notification.getType());
                notification.setAcknowledged(true);
                save(notification);
            }
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void acknowledgeAllCHONotifications(Integer claimId) {
        LOG.debug("Acknowledging all CHO notifications on claim with id={}", claimId);
        List<Notification> notifications = getNotifications(claimId);
        for (Notification notification : notifications) {
            if (!NotificationType.getNotificationType(notification.getType()).isInsurerType()) {
                LOG.debug("Acknowledging Insurer notification on claim={}: {}", claimId, notification.getType());
                notification.setAcknowledged(true);
                save(notification);
            }
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void acknowledgeNotificationById(Integer id) {
        LOG.debug("Acknowledging notification by id={}", id);
        Notification notification = getNotificationsById(id);
        try {
            if (notification != null) {
                LOG.debug("Acknowledging notification with id ={}: {}", id, notification.getType());
                notification.setAcknowledged(true);
                save(notification);
            } else {
                LOG.error("Error acknowleding notification by id - No such notification: {}", id);
            }
        } catch (Exception ex) {
            LOG.error("Error thrown acknowledging notification: {}", ex.getMessage(), ex);
        }
        notification = getNotificationsById(id);
        LOG.debug("Acknowledge status is now: {}", notification.isAcknowledged());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void removeAllInsurerNotifications(Integer claimId) {
        LOG.debug("Removing all Insurer notifications on claim with id={}", claimId);
        List<Notification> notifications = getNotifications(claimId);
        for (Notification notification : notifications) {
            if (NotificationType.getNotificationType(notification.getType()).isInsurerType()) {
                notification.setDeleted(true);
                save(notification);
                LOG.debug("Marked Insurer notification on claim={} as deleted: {}", claimId, notification.getType());
            }
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void removeAllCHONotifications(Integer claimId) {
        LOG.debug("Removing all CHO notifications on claim with id={}", claimId);
        List<Notification> notifications = getNotifications(claimId);
        for (Notification notification : notifications) {
            if (!NotificationType.getNotificationType(notification.getType()).isInsurerType()) {
                notification.setDeleted(true);
                save(notification);
                LOG.debug("Marked CHO notification on claim={} as deleted: {}", claimId, notification.getType());
            }
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void removeNotificationById(Integer id) {
        LOG.debug("Removing notification by id={}", id);
        Notification notification = getNotificationsById(id);
        if (notification != null) {
            notification.setDeleted(true);
            save(notification);
            LOG.debug("Marked notification with id ={} as deleted: {}", id, notification.getType());
        } else {
            LOG.error("Error removing notification by id - No such notification: {}", id);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void removeAllNotifications(Integer claimId) {
        LOG.debug("Marking all notification on claim with id={} as deleted", claimId);
        List<Notification> notifications = getNotifications(claimId);
        for (Notification notification : notifications) {
            notification.setDeleted(true);
            save(notification);
        }
    }

    @Override
    public List<Notification> getNotifications(Integer claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Notification.class);
        criteria.add(Restrictions.eq("deleted", false));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.asc("createdDate"));

        return findByCriteria(criteria);
    }

    private Notification getNotificationsById(int id) {
        return (Notification) get(Notification.class, id);
    }

    private void removeUnacknowledgedNotificationByType(Integer claimId, String type) {
        LOG.debug("Marking all unacknowledged notification on claim with id={} of type {} as deleted", claimId, type);
        List<Notification> notifications = getNotifications(claimId);

        for (Notification notification : notifications) {
            if (!notification.isAcknowledged() && type.equals(notification.getType())) {
                notification.setDeleted(true);
                save(notification);
            }
        }
    }
}
