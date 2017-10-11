package idas.chox.web.scheduler;

import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;

/**
 *
 * @author John
 */
public class StopChaseEmailSchedulerJob extends EmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(StopChaseEmailSchedulerJob.class);
    private static final String JOB_NAME = "TOTALLOSS_STOP_CHASE_TASK";

    @Secured({"ROLE_CHO", "ROLE_CHOX_ADMIN"})
    private String doJob(Message message, String sender)  throws MessagingException {        
        // Subject is:  IMS TL Stop Chase Request: <ERAC FNOL reference number>
        //   - extract the cho ref number
        int i = message.getSubject().indexOf("Request:");
        if (i<1) { // lets try without the 
            i = message.getSubject().indexOf("Request")-1;
        }
        String choRef = message.getSubject().substring(i+8).trim();
        
        StringBuilder statusString = new StringBuilder();
        String result;
        
        Claim claim = validateClaimReferenceNumber(choRef, statusString);
        if (claim != null) {
            result = claimService.stopClaimChase(choRef);
        } else {
            result = statusString.toString();
        }
        
        return result;
    }

    @Override
    public void processEmail(Message message, String emailSubject, String sender, String bccReceivers, boolean replyToSender) throws MessagingException {
            String result = doJob(message, sender);
            if (result != null) {
                LOG.info("Stop Chase email processed: {}", result);
            }
            // No email for now - lets wait until we get the requirements
    }
    
    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}
