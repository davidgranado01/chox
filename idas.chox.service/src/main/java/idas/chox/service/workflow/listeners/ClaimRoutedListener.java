package idas.chox.service.workflow.listeners;

import com.google.common.eventbus.Subscribe;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.events.ClaimRoutedEvent;

/**
 *
 * @author john
 */
@Listener
public class ClaimRoutedListener {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRoutedListener.class);
    
    @Handler
    @Subscribe
    public void handle(ClaimRoutedEvent event){
        LOG.info("ClaimRoutedEvent Message received: {}", event);
    } 

}
