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

    public String buildMessage(String subject, Map<Integer, List<String>> xlsDataMap) {
        return null;
    }

    public abstract Map<Integer, List<String>> doJob();

    @Override
    protected void process(String emailSubject, SchedulerJob schedulerJob) {
        Map<Integer, List<String>> resultMap = doJob();
        String emailMessage = buildMessage(emailSubject, resultMap);
        if (emailMessage != null) {
            sendMail(schedulerJob.getPrivilegedUsers(), schedulerJob.getBccReceivers(), emailSubject, emailMessage);
            LOG.info("{} with subject '{}' job finished.", getClass().getSimpleName(), emailSubject);
        }
    }
}
