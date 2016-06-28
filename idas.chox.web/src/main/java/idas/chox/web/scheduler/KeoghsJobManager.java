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
    private static final int CHECK_PERIOD = 5;

    private ScheduledExecutorService scheduler;
    @Autowired
    private KeoghsCheckJob keoghsCheckJob;

    public void setKeoghsCheckJob(KeoghsCheckJob keoghsCheckJob) {
        this.keoghsCheckJob = keoghsCheckJob;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (keoghsCheckJob == null) {
            LOG.warn("No keoghsCheckJob injected, attempting to force injection....");
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();

//        scheduler.scheduleAtFixedRate(new SomeDailyJob(), 0, 1, TimeUnit.DAYS);
//        scheduler.scheduleAtFixedRate(new SomeHourlyJob(), 0, 1, TimeUnit.HOURS);
        if (keoghsCheckJob != null) {
            scheduler.scheduleAtFixedRate(keoghsCheckJob, 2, CHECK_PERIOD, TimeUnit.MINUTES);
            LOG.info("KeoghsCheckJob scheduled to run every {} minutes", CHECK_PERIOD);
        } else {
            LOG.error("No keoghsCheckJob to schedule.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
        LOG.info("Scheduler terminated.");
    }

}
