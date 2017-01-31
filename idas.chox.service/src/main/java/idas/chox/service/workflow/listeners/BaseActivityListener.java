package idas.chox.service.workflow.listeners;

import com.google.common.eventbus.Subscribe;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.data.services.SecureDataService;
import idas.chox.events.BaseActivityEvent;

/**
 *
 * @author john
 */
@Listener
public class BaseActivityListener extends SecureDataService {
    
    private static final Logger LOG = LoggerFactory.getLogger(BaseActivityListener.class);
    private boolean storeEvents;

    public void setStoreEvents(boolean storeEvents) {
        this.storeEvents = storeEvents;
    }

    @Handler
    @Subscribe
    public void handle(BaseActivityEvent event) {
        LOG.info("Activity Message received: {}", event);
        if (storeEvents) {
            try {
                save(event);
            } catch (Exception ex) {
                LOG.error("Exception thrown saving event: {}", ex.getMessage(), ex);
            }
        }
    }

}
