package idas.chox.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author john
 */
public class ExecutorContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutorContextListener.class);

    private ExecutorService executor;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        ServletContext context = arg0.getServletContext();
        int nr_executors = 1;
        ThreadFactory daemonFactory = new DaemonThreadFactory();
        try {
            nr_executors = Integer.parseInt(context.getInitParameter("nr-chox_executors"));
        } catch (NumberFormatException ignore) {
        }

        if (nr_executors <= 1) {
            executor = Executors.newSingleThreadExecutor(daemonFactory);
        } else {
            executor = Executors.newFixedThreadPool(nr_executors, daemonFactory);
        }
        context.setAttribute("CHOX_EXECUTOR", executor);
        LOG.info("CHOX executor initialised with {} threads", nr_executors);
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        LOG.debug("-------------> CONTEXT DESTROYED <-------------");
        if (executor != null) {
            executor.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        LOG.error("executorService did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                LOG.error("CHOX executor threw exception: {}", ie.getMessage());
                executor.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }

}
