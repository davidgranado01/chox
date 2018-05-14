package idas.chox.web.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.SchedulerJob;

/**
 *
 * @author Seeni
 */
public abstract class DbSchedulerJob extends SchedulerJobBase {

    private static final Logger LOG = LoggerFactory.getLogger(DbSchedulerJob.class);

    public abstract boolean doJob();
    public String buildMessage() {
        return null;
    }

    @Override
    protected void process(SchedulerJob schedulerJob) {
        if (doJob()) {
            String results = buildMessage();
            LOG.debug("{} job finished with results:\n", getClass().getSimpleName(), results);
        } else {
            LOG.debug("{} job finished.", getClass().getSimpleName());
        }
    }
}
