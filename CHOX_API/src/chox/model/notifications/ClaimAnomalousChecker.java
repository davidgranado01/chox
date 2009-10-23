/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model.notifications;

import chox.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emmanuel
 */
public class ClaimAnomalousChecker {

    private List<AnomalousCheck> anomalousChecks;

    public List<Notification> getAnomalousNotifications(Claim c) {

        List<Notification> notifications = new ArrayList<Notification>();

        for (AnomalousCheck anomalousCheck : anomalousChecks) {
            if (anomalousCheck.check(c)) {
                Notification n = (Notification)anomalousCheck;
                notifications.add(n);
            }
        }

       
        
        return notifications;
    }

    /**
     * @return the anomalousChecks
     */
    public List<AnomalousCheck> getAnomalousChecks() {
        return anomalousChecks;
    }

    /**
     * @param anomalousChecks the anomalousChecks to set
     */
    public void setAnomalousChecks(List<AnomalousCheck> anomalousChecks) {
        this.anomalousChecks = anomalousChecks;
    }
}
