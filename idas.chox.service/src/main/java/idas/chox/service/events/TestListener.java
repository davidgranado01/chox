package idas.chox.service.events;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author john
 */
public class TestListener {
    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);
    public String lastMessage = null;
 
    @Subscribe
    public void listen(TestEvent event) {
        lastMessage = event.getMessage();
        LOG.info("Message recived: {}", lastMessage);
    }
 
    public String getLastMessage() {
        return lastMessage;
    }    
}
