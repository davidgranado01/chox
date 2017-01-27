package idas.chox.service.workflow.listeners;

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
            LOG.error("Dead message received: {}", ((BaseActivityEvent)message.getMessage()));
        } 
 
}
