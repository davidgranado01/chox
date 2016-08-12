package idas.chox.web.scheduler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.SchedulerJob;
import idas.chox.keoghs.Keoghs;

public class KeoghsSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(KeoghsSchedulerJob.class);
    public static final String JOB_NAME = "KEOGHS";
    private int maxPendingRequests;
    private int maxQueuedRequests;
    @Autowired
    private Keoghs keoghs;

    public void setKeoghs(Keoghs keoghs) {
        this.keoghs = keoghs;
    }

    public void setMaxPendingRequests(int maxPendingRequests) {
        this.maxPendingRequests = maxPendingRequests;
    }

    public void setMaxQueuedRequests(int maxQueuedRequests) {
        this.maxQueuedRequests = maxQueuedRequests;
    }

    @Override
    public final Map<Integer, List<String>> doJob() {
        int noRequestsToQueue = maxQueuedRequests;
        try {
            LOG.debug("Checking status of submitted requests...");
            int noPendingRequests = keoghs.check();
            LOG.debug("Total of {} claims pending at Keoghs (maxQueuedRequests={})", noPendingRequests, maxQueuedRequests);
            // Start new transaction?
//                releaseHibernateSessionConditionally(); this.handleHibernateTransactionIntricacies();
            if ((maxQueuedRequests > 0 && noPendingRequests + maxQueuedRequests > maxPendingRequests)
                    || maxQueuedRequests < 0) {
                noRequestsToQueue = maxPendingRequests - noPendingRequests;
            }
            
            if (noRequestsToQueue > 0) {
                LOG.debug("Submitting maximum of {} new requests (maxPending={})", noRequestsToQueue, maxPendingRequests);
                keoghs.submit(noRequestsToQueue);
            } else {
                LOG.debug("Not submitting any new requests as there are {} already pending (noRequestsToQueue={})", maxPendingRequests, noRequestsToQueue);
            }
                        
        } catch (Exception ex) {
            LOG.error("Exception thrown checking Keoghs jobs: {}", ex.getMessage(), ex);
        }

        return null;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}
