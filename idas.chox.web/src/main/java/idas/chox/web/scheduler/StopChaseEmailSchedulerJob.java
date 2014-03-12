package idas.chox.web.scheduler;

import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.ClaimService;

/**
 *
 * @author John
 */
public class StopChaseEmailSchedulerJob extends EmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(StopChaseEmailSchedulerJob.class);
    private static final String JOB_NAME = "TOTALLOSS_STOP_CHASE_TASK";
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }


    @Secured({"ROLE_CHO", "ROLE_CHOX_ADMIN"})
    private String doJob(Message message, String sender)  throws MessagingException {        
        // Subject is:  IMS TL Stop Chase Request: <ERAC FNOL reference number>
        //   - extract the cho ref number
        String choRef = message.getSubject().substring(26).trim();
        
        Claim claim = claimService.getClaimByCHOReferenceNumber(choRef);
        claim.setTotalLossChase(false);
        claimService.save(claim);
        
        return choRef;
    }

    @Override
    public void processEmail(Message message, String emailSubject, String sender, String bccReceivers, boolean replyToSender) throws MessagingException {
            String choRef = doJob(message, sender);
            LOG.info("Chase task stopped for claim '{}'.", choRef);
            // No email for now - lets wait until we get the requirements
    }
    
    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}
