package idas.chox.data.notifications;

import idas.chox.core.model.Notification;

/**
 *
 * @author John
 */
public class TotalLossAnomalousNotification extends Notification {
    
    public TotalLossAnomalousNotification() {
        super();
        setMessage("The CHO has indicated the claim is now a Total Loss");
    }

}
