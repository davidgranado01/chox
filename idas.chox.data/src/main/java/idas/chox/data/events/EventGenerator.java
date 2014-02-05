package idas.chox.data.events;

import idas.chox.core.model.Claim;
import idas.chox.events.EventRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class EventGenerator {
    private static Logger LOG = LoggerFactory.getLogger(EventGenerator.class);
    private EventRegister eventRegister;

    public void setEventRegister(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }

    // Utility function
    public void startEvent(Claim claim, String name, int id, boolean insurerOnly, boolean choOnly) throws Exception {
        int claimId = -1;
        if (claim.getId() != null) {
            claimId = claim.getId().intValue();
        }
        int insurerId = 0;
        if (!choOnly) {
            insurerId = claim.getInsurer().getId().intValue();
        }
        int choId = 0;
        if (!insurerOnly) {
            choId = claim.getChorganisation().getId().intValue();
        }
        eventRegister.startEvent(name, id, insurerId, choId, claimId, claim.getClaimType().ordinal());
        eventRegister.addParameter("insurerName", claim.getInsurer().getName());
        eventRegister.addParameter("choName", claim.getChorganisation().getName());
        eventRegister.addParameter("choReference", claim.getChoReference());
        eventRegister.addParameter("claimNumber", claim.getClaimNumber());
    }
    public void startEvent(Claim claim, String name, int id) throws Exception {
        startEvent(claim, name, id, false, false);
    }
    public void addParameter(String name, Object value) {
        eventRegister.addParameter(name, value);
    }
    
    public void completeEvent(Claim claim) throws Exception {
        eventRegister.addParameter("claimStatus", claim.getStatus());
        eventRegister.addParameter("claimType", claim.getClaimType().toString());
        eventRegister.completeEvent();
    }
    
    public void sendEvents() throws Exception {
        eventRegister.sendEvents();
    }
}
