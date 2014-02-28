package idas.chox.web.scheduler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.SchedulerJob;

/**
 *
 * @author Seeni
 */
public abstract class DbSchedulerJob extends SchedulerJobBase {

    private static final Logger LOG = LoggerFactory.getLogger(DbSchedulerJob.class);

    protected abstract String buildMessage(String subject, Map<Integer, List<String>> xlsDataMap);

    public abstract Map<Integer, List<String>> doJob();

    @Override
    protected void process(String emailSubject, SchedulerJob schedulerJob) {
        Map<Integer, List<String>> resultMap = doJob();
        String emailMessage = buildMessage(emailSubject, resultMap);
        sendMail(schedulerJob.getPrivilegedUsers(), schedulerJob.getBccReceivers(), emailSubject, emailMessage);
        LOG.info("{} with subject '{}' job finished.", getClass().getSimpleName(), emailSubject);
    }
}
