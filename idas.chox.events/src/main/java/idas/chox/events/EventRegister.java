package idas.chox.events;

/**
 *
 * @author John
 */
public interface  EventRegister {
    public void startEvent(String name, int insurerId, int choId, int claimId, int claimType) throws Exception;
    public void addParameter(String paramName, Object paramValue);
    public void completeEvent() throws Exception;
    public void sendEvents() throws Exception;
}
