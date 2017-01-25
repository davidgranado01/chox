package idas.chox.service.events;

/**
 *
 * @author john
 */
public class TestEvent {
    private final String message;
    
    public TestEvent(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
