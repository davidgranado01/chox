package idas.chox.data.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.data.events.ChoxEvent;
import idas.chox.data.events.EventGenerator;

/**
 *
 * @author John
 */
public class EventService {
    private static Logger LOG = LoggerFactory.getLogger(EventService.class);
    private EventGenerator eventGenerator;

    public void setEventGenerator(EventGenerator eventGenerator) {
        this.eventGenerator = eventGenerator;
    }

    // Utility function
    public void startEvent(Claim claim, String name, int id, boolean insurerOnly, boolean choOnly) throws Exception {
        eventGenerator.startEvent(claim, name, id, insurerOnly, choOnly);
    }
    public void startEvent(Claim claim, String name, int id) throws Exception {
        startEvent(claim, name, id, false, false);
    }
    public void addParameter(String name, Object value) {
        eventGenerator.addParameter(name, value);
    }
    
    public void completeEvent(Claim claim) throws Exception {
        eventGenerator.completeEvent(claim);
    }
    

    public void generate(final Claim claim, final Task task, ChoxEvent event) {
        try {
            event.build(this, claim, task);
        } catch (Exception ex) {
            LOG.error("Error generating events for event '{}' : {}", new Object[]{event, ex.getMessage(), ex});
            return;
        }

        try {
            eventGenerator.sendEvents();
        } catch (Exception ex) {
            LOG.error("Error sending generated events for activity '{}' : {}", new Object[]{event, ex.getMessage(), ex});
        }
    }

    public void generate(final Claim claim, String oldReference, ChoxEvent event) {
        try {
            event.build(this, claim, oldReference);
        } catch (Exception ex) {
            LOG.error("Error generating events for event '{}' : {}", new Object[]{event, ex.getMessage(), ex});
            return;
        }

        try {
            eventGenerator.sendEvents();
        } catch (Exception ex) {
            LOG.error("Error sending generated events for activity '{}' : {}", new Object[]{event, ex.getMessage(), ex});
        }
    }

}
