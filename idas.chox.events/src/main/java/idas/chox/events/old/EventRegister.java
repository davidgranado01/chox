package idas.chox.events.old;

/**
 *
 * @author John
 */
public interface  EventRegister {
    public void startEvent(String name, int id, int insurerId, int choId, int claimId, int claimType) throws Exception;
    public void addParameter(String paramName, Object paramValue);
    public void completeEvent() throws Exception;
    public void sendEvents() throws Exception;
}
