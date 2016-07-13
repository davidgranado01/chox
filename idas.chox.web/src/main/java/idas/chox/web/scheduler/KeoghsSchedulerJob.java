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
    @Autowired
    private Keoghs keoghs;

    public void setKeoghs(Keoghs keoghs) {
        this.keoghs = keoghs;
    }

    @Override
    public final Map<Integer, List<String>> doJob() {
        try {
            LOG.debug("Checking status of submitted requests...");
            keoghs.check();
            // Start new transaction?
//                releaseHibernateSessionConditionally(); this.handleHibernateTransactionIntricacies();
            LOG.debug("Submitting new requests");
            keoghs.submit();
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
