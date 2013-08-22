package idas.chox.events;

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
    public static final String CLAIM_ROUTED_EVENT= "ClaimRoutedEvent";
    private List<Event> events;
    private Event openEvent;
    private ChoxJmsEventSender choxJmsEventSender;

    public void setChoxJmsEventSender(ChoxJmsEventSender choxJmsEventSender) {
        this.choxJmsEventSender = choxJmsEventSender;
    }

    @Override
    public void startEvent(String name, int insurerId, int choId, int claimId, int claimType) throws Exception {
        if (openEvent != null) {
            throw new Exception("Event already started.");
        }

        openEvent = new Event(name, insurerId, choId, claimId, claimType);
    }

    @Override
    public void addParameter(String paramName, Object paramValue) {
        openEvent.addParameter(paramName, paramValue);
    }

    @Override
    public void completeEvent() throws Exception {
        if (openEvent == null) {
            throw new Exception("No event has been started.");
        }
        if (events == null) {
            events = new ArrayList(3);
        }

        events.add(openEvent);
        openEvent = null;
    }

    @Override
    public void sendEvents() throws Exception {
        if (events != null) {
            for (Event ev : events) {
                try {
                    choxJmsEventSender.send(ev);
                } catch (Exception ex) {
                    LOG.error("Error sending CHOX event: {}\n", ex.getMessage(), ex);
                    throw ex;
                }

            }
        }
    }
}
