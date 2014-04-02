package idas.chox.data.notifications;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;
import idas.chox.core.notifications.AnomalousCheck;

/**
 *
 * @author John
 */
public class TotalLossAnomalousNotificationChecker implements AnomalousCheck {

    @Override
    public boolean check(Claim claim) {
        boolean isAnomalous = false;
        
        if (claim.getCustomer()!= null && claim.getCustomer().getIsTotalLoss()) {
            isAnomalous = true;
        }
        return isAnomalous;

    }


    @Override
    public Notification buildNotification() {
        return new TotalLossAnomalousNotification();
    }

    
    @Override
    public boolean isRefreshRequired() {
        return true;
    }

}
