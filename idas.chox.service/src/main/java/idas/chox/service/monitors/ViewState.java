package idas.chox.service.monitors;


/**
 *
 * @author Emmanuel
 */
public class ViewState {
    private static final long maxAge = 1000 * 6;
    private Long birth = System.currentTimeMillis();
    private final Object lock = new Object();
    
    public ViewState() {
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
            //System.out.println("Refreshed to " + birth.toString());
       }
    }
}
