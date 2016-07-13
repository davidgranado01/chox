package idas.chox.web.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 *
 * @author john
 */
public class KeoghsJobManager implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(KeoghsJobManager.class);
    private static int checkPeriod = 5;
    private ScheduledExecutorService scheduler;
    @Autowired
    private KeoghsCheckJob keoghsCheckJob;

    public void setCheckPeriod(int checkPeriod) {
        KeoghsJobManager.checkPeriod = checkPeriod;
    }

    public void setKeoghsCheckJob(KeoghsCheckJob keoghsCheckJob) {
        this.keoghsCheckJob = keoghsCheckJob;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (keoghsCheckJob == null) {
            LOG.debug("No keoghsCheckJob injected, attempting to force injection....(with checkPeriod={})", checkPeriod);
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        }

        if (keoghsCheckJob != null && checkPeriod > 0) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(keoghsCheckJob, 2, checkPeriod, TimeUnit.MINUTES);
            LOG.debug("KeoghsCheckJob scheduled to run every {} minutes", checkPeriod);
        } else {
            LOG.error("No keoghsCheckJob to schedule.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (scheduler != null) {
            scheduler.shutdownNow();
            LOG.debug("Scheduler terminated.");
        }
    }

}
