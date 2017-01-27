package idas.chox.service.workflow.listeners;

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
    
    @Handler
    public void handle(BaseActivityEvent event){
        LOG.info("Activity Message received: {}", event);
        try {
            save(event);
        } catch (Exception ex) {
            LOG.error("Exception thrown saving event: {}", ex.getMessage(), ex);
        }
    } 

}
