package idas.chox.service.notifications;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;
import idas.chox.core.notifications.AnomalousCheck;
import java.util.ArrayList;
import java.util.List;

public class ClaimAnomalousChecker {

    private List<AnomalousCheck> anomalousChecks;

    public List<Notification> getAnomalousNotifications(Claim c) {

        List<Notification> notifications = new ArrayList<Notification>();

        for (AnomalousCheck anomalousCheck : anomalousChecks) {

            if (anomalousCheck.check(c)) {
                notifications.add(anomalousCheck.BuildNotification());
            }
        }

        return notifications;
    }

    public List<AnomalousCheck> getAnomalousChecks() {
        return anomalousChecks;
    }

    public void setAnomalousChecks(List<AnomalousCheck> anomalousChecks) {
        this.anomalousChecks = anomalousChecks;
    }
}
