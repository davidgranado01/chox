package idas.chox.service.workflow.listeners;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import net.engio.mbassy.bus.common.DeadMessage;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.events.BaseActivityEvent;

/**
 *
 * @author john
 */
@Listener
public class DeadMessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DeadMessageHandler.class);

        @Handler 
        public void handle(DeadMessage message) { 
            LOG.error("MBassador Dead message received: {}", ((BaseActivityEvent)message.getMessage()));
        } 
        @Subscribe
        public void handle(DeadEvent message) { 
            LOG.error("Guava Dead message received: {}", ((BaseActivityEvent)message.getEvent()));
        } 
 
}
