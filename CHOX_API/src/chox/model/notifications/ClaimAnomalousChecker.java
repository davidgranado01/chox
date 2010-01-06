package chox.model.notifications;

import chox.model.*;
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
