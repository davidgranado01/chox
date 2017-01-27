package idas.chox.events.old;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class ChoxEventRegister implements EventRegister {

    private static final Logger LOG = LoggerFactory.getLogger(ChoxEventRegister.class);
    
    private List<Event> events;
    private Event openEvent;
    private ChoxJmsEventSender choxJmsEventSender;
    private boolean active;

    public void setActive(boolean active) {
        this.active = active;
    }
    

    public void setChoxJmsEventSender(ChoxJmsEventSender choxJmsEventSender) {
        this.choxJmsEventSender = choxJmsEventSender;
    }

    @Override
    public void startEvent(String name, int id, int insurerId, int choId, int claimId, int claimType) throws Exception {
        if (openEvent != null) {
            throw new Exception("Event already started");
        }

        if (active) {
            openEvent = new Event(name, id, insurerId, choId, claimId, claimType);
        }
    }

    @Override
    public void addParameter(String paramName, Object paramValue) {
        if (active) {
            openEvent.addParameter(paramName, paramValue);
        }
    }

    @Override
    public void completeEvent() throws Exception {
        if (active) {
            if (openEvent == null) {
                throw new Exception("No event has been started");
            }
            if (events == null) {
                events = new ArrayList(3);
            }

            events.add(openEvent);
            openEvent = null;
        }
    }

    @Override
    public void sendEvents() throws Exception {
        if (openEvent != null) {
                throw new Exception("There is an event that is currently open");
        }
        if (active && events != null) {
            for (Event ev : events) {
                try {
                    LOG.debug("Sending event {}", ev);
                    choxJmsEventSender.send(ev);
                } catch (Exception ex) {
                    LOG.error("Error sending CHOX event: {}\n", ex.getMessage(), ex);
                    throw ex;
                } finally {
                    events = null; openEvent = null;
                }

            }
        }
    }
}
