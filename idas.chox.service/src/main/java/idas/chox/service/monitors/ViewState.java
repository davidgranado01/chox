package idas.chox.service.monitors;

/**
 *
 * @author Emmanuel
 */
public class ViewState {
    private final long maxAge;
    private Long birth = System.currentTimeMillis();
    private final Object lock = new Object();
    
    public ViewState(long maxAge) {
        this.maxAge = maxAge+2000;
    }

    public boolean isExpired() {
        boolean hasExpired;
        synchronized (lock) {            
            hasExpired = System.currentTimeMillis() - birth > maxAge;
        }
        return hasExpired;
    }

    public void refresh() {
       synchronized (lock) {
            birth = System.currentTimeMillis();
       }
    }
}
