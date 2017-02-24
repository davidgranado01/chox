package idas.chox.service.workflow.listeners;

import com.google.common.eventbus.Subscribe;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.data.services.SecureDataService;
import idas.chox.events.BaseActivityEvent;

/**
 *
 * @author john
 */
@Listener
public class BaseActivityListener extends SecureDataService {
    
    private static final Logger LOG = LoggerFactory.getLogger(BaseActivityListener.class);
    @Autowired
    private boolean storeEvents;

    public void setStoreEvents(boolean storeEvents) {
        this.storeEvents = storeEvents;
    }

    @Handler
    @Subscribe
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void handle(BaseActivityEvent event) {
        LOG.debug("Activity Message received: {}", event);
        if (storeEvents) {
            try {
                save(event);
            } catch (Exception ex) {
                LOG.error("Exception thrown saving event '{}': {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
            }
        }
    }

}
