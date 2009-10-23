/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.notifications;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.Notification;
import java.util.Date;

/**
 *
 * @author emmanuel
 */
public class EcdAnomalousNotification extends Notification implements AnomalousCheck {

    private static double ecdDurationAllowPercentage = 0.5;

    public EcdAnomalousNotification()
    {
        super();
        setMessage("Significant Delay To Hire Duration Due To New ECD");
    }

    public boolean check(Claim c) {
        
        return isClaimAnomalies(c);

    }
    
    private Boolean isClaimAnomalies(Claim c)
    {
        Boolean result = false;
        Date policyHolderDate = c.getPolicyHolderContactDate();
        Date firstECD = c.getCustomer().getInitialECD();
        Date lastECD = c.getCustomer().getInitialECD();
        
        int numberOfEcd = c.getHireMonitoringEcds().size();
        
        if(firstECD == null && numberOfEcd > 0)
        {
            firstECD = c.getHireMonitoringEcds().get(0).getEcdDate();
        }
        
        if(numberOfEcd > 0)
        {
            lastECD = c.getHireMonitoringEcds().get(numberOfEcd - 1).getEcdDate();
        }
        
        return isClaimAnomalies(policyHolderDate,firstECD,lastECD);
    }

    private Boolean isClaimAnomalies(Date policyHolderDate, Date firstECD,Date lastECD){

        if(policyHolderDate != null && lastECD != null && firstECD != null)
        {
            Long iTotalDelayDays = DateHelper.daysBetween(firstECD, lastECD);
            Long iMD = DateHelper.daysBetween(policyHolderDate, firstECD);

            if (iMD > 0) {
                int iMDRate = (int) java.lang.Math.round(iMD * ecdDurationAllowPercentage);

                if ((iTotalDelayDays > iMDRate)) {
                    return true;
                }
            }
        }

        return false;
    }

}
