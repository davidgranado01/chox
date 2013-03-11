package idas.chox.data.notifications;

import java.util.Date;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Notification;
import idas.chox.core.notifications.AnomalousCheck;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author emmanuel
 */
public class EcdAnomalousNotificationChecker implements AnomalousCheck {

    @Override
    public boolean check(Claim claim) {
        return isClaimAnomalies(claim);
    }

    private Boolean isClaimAnomalies(Claim c) {
        Date policyHolderDate = c.getPolicyHolderContactDate();
        Date firstECD = c.getCustomer().getInitialECD();
        Date lastECD = c.getCustomer().getInitialECD();
        double ecdDurationAllowPercentage = c.getInsurer().getEcdIncreaseTriggerPercentage().doubleValue()/100.0;

        int numberOfEcd = c.getHireMonitoringEcds().size();

        if (firstECD == null && numberOfEcd > 0) {
            firstECD = c.getHireMonitoringEcds().get(0).getEcdDate();
        }

        if (numberOfEcd > 0) {
            lastECD = c.getHireMonitoringEcds().get(numberOfEcd - 1).getEcdDate();
        }

        return isClaimAnomalies(policyHolderDate, firstECD, lastECD, ecdDurationAllowPercentage);
    }

    private Boolean isClaimAnomalies(Date policyHolderDate, Date firstECD, Date lastECD, double ecdDurationAllowPercentage) {

        if (policyHolderDate != null && lastECD != null && firstECD != null) {
            Long iTotalDelayDays = DateHelper.getNumberOf24HourPeriodsBetween(firstECD, lastECD);
            Long iMD = DateHelper.getNumberOf24HourPeriodsBetween(policyHolderDate, firstECD);

            if (iMD > 0) {
                int iMDRate = (int) java.lang.Math.round(iMD * ecdDurationAllowPercentage);

                if ((iTotalDelayDays > iMDRate)) {
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public Notification buildNotification() {
        return new EcdAnomalousNotification();
    }

    @Override
    public boolean isRefreshRequired() {
        return false;
    }
}
